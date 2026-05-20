package dao;

import connection.ConnectionFactory;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * AbstractDAO - Clasa generica pentru accesul la date (Data Access Object)
 *             - implementeaza operatiile de baza folosind tehnica (Reflection)
 * pentru a mapa automat obiectele Java la tabelele din baza de date
 * * @param <T> Tipul entitatii gestionate (ex: Client, Product, Order)
 */
public abstract class AbstractDAO<T>
{
    // stocheaza tipul clasei generice pentru a putea lucra cu ea la runtime
    private final Class<T> type;

    /**
     * Constructor care determina tipul entitatii la runtime
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO()
    {
        // extrage argumentul de tip generic definit in clasa mostenita
        this.type = (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Returneaza numele coloanei corespunzatoare campului din clasa Java
     * @param field Campul din entitatea Java
     * @return Numele coloanei in baza de date
     */
    private String getColumnName(Field field)
    {
        // presupune ca numele campului din java este identic cu numele coloanei din baza de date
        return field.getName();
    }

    /**
     * Gaseste o inregistrare dupa id
     * @param id Identificatorul unic al inregistrarii
     * @return Obiectul gasit sau null daca nu exista
     */
    public T findById(int id)
    {
        // construieste query-ul sql bazandu-se pe numele clasei
        String query = "SELECT * FROM " + type.getSimpleName().toLowerCase() + " WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(query))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                // transforma rezultatul sql in obiect java
                List<T> result = createObjects(rs);
                return result.isEmpty() ? null : result.get(0);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returneaza toate inregistrarile din tabelul corespunzator entitatii
     * @return O lista cu toate obiectele gasite
     */
    public List<T> findAll()
    {
        String query = "SELECT * FROM " + type.getSimpleName().toLowerCase();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(query);
             ResultSet rs = st.executeQuery())
        {
            return createObjects(rs);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Insereaza un obiect nou in baza de date
     * @param t Obiectul de inserat
     * @return Obiectul inserat
     */
    public T insert(T t)
    {
        // filtreaza campurile pentru a exclude id-ul care este generat automat
        List<Field> fields = Arrays.stream(type.getDeclaredFields())
                .filter(f -> !f.getName().equals("id"))
                .collect(Collectors.toList());

        // construieste dinamic numele coloanelor si placeholder-ele pentru query
        String fieldNames = fields.stream().map(this::getColumnName).collect(Collectors.joining(", "));
        String placeholders = fields.stream().map(f -> "?").collect(Collectors.joining(", "));
        String query = "INSERT INTO " + type.getSimpleName().toLowerCase() + " (" + fieldNames + ") VALUES (" + placeholders + ")";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            // mapeaza valorile din obiectul java la parametrii prepared statement-ului
            IntStream.range(0, fields.size()).forEach(i ->
            {
                try
                {
                    fields.get(i).setAccessible(true);
                    st.setObject(i + 1, fields.get(i).get(t));
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Eroare la maparea campurilor", e);
                }
            });
            st.executeUpdate();

            // preia id-ul generat de mysql si il seteaza pe obiect
            try (ResultSet generatedKeys = st.getGeneratedKeys())
            {
                if (generatedKeys.next())
                {
                    Field idField = type.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(t, generatedKeys.getInt(1));
                }
            }

            return t;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sterge o inregistrare din baza de date dupa id
     * @param id Identificatorul inregistrarii de sters
     */
    public void delete(int id)
    {
        String query = "DELETE FROM " + type.getSimpleName().toLowerCase() + " WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(query))
        {
            st.setInt(1, id);
            st.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Actualizeaza informatiile unui obiect existent
     * @param t Obiectul cu noile date
     * @param id Id-ul obiectului care trebuie actualizat
     * @return Obiectul actualizat
     */
    public T update(T t, int id)
    {
        // identifica campurile care trebuie modificate
        List<Field> fields = Arrays.stream(type.getDeclaredFields())
                .filter(f -> !f.getName().equals("id"))
                .collect(Collectors.toList());

        // construieste clauza set pentru update
        String querySet = fields.stream().map(f -> getColumnName(f) + " = ?").collect(Collectors.joining(", "));
        String query = "UPDATE " + type.getSimpleName().toLowerCase() + " SET " + querySet + " WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(query))
        {
            // populeaza parametrii pentru update folosind reflection
            IntStream.range(0, fields.size()).forEach(i ->
            {
                try
                {
                    fields.get(i).setAccessible(true);
                    st.setObject(i + 1, fields.get(i).get(t));
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Eroare la maparea campurilor", e);
                }
            });
            // seteaza id-ul in clauza where
            st.setInt(fields.size() + 1, id);
            st.executeUpdate();
            return t;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Transforma ResultSet-ul primit de la baza de date in obiecte Java,
     * folosind exclusiv Stream uri
     * @param rs ResultSet ul cu datele din baza de date
     * @return O lista de obiecte de tip T
     */
    private List<T> createObjects(ResultSet rs)
    {
        List<T> result = new ArrayList<>();
        try
        {
            while (rs.next())
            {
                // creeaza o instanta noua pentru fiecare rand din tabel
                T instance = type.getDeclaredConstructor().newInstance();
                // populeaza campurile obiectului cu datele din result set
                Arrays.stream(type.getDeclaredFields()).forEach(field ->
                {
                    try
                    {
                        field.setAccessible(true);
                        field.set(instance, rs.getObject(getColumnName(field)));
                    }
                    catch (Exception e)
                    {
                        // ignoram campurile care nu corespund tabelului
                    }
                });
                result.add(instance);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}