package bll;

import dao.ProductDAO;
import model.Client;
import model.Product;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * BUSINESS LOGIC LAYER(BLL) pentru gestionarea entitatilor de tip product
 * ProductBLL - preia datele din DATA ACCESS LAYER(DAO) si le pregateste pentru VIEW
 */
public class ProductBLL
{
    private final ProductDAO productDAO;

    /**
     * constructor care initializeaza DAO pentru produse
     */
    public ProductBLL()
    {
        this.productDAO = new ProductDAO();
    }

    /**
     * returneaza o lista cu toate produsele din baza de date
     * @return lista de obiecte product
     */
    public List<Product> findAll()
    {
        return productDAO.findAll();
    }

    /**
     * gaseste un produs dupa id
     * @param id id-ul produsului cautat
     * @return produsul gasit
     * @throws NoSuchElementException daca produsul nu exista
     */
    public Product findProductById(int id)
    {
        //ofNullable - gestioneaza posibilitatea unui client sa fie NULL
        return Optional.ofNullable(productDAO.findById(id))
                //daca e client -> il returneaza
                //daca e null -> arunca NoSuchElementException
                .orElseThrow(() -> new NoSuchElementException("produsul cu id " + id + " nu exista!"));
    }

    /**
     * insereaza un produs nou in baza de date
     * @param product produsul care trebuie adaugat
     */
    public void insertProduct(Product product)
    {
        try
        {

            productDAO.insert(product);
        }
        catch (Exception e)
        {
            throw new RuntimeException("eroare la inserarea produsului: " + e.getMessage());
        }
    }

    /**
     * sterge un produs din baza de date dupa id
     * @param id id-ul produsului de sters.
     */
    public void deleteProduct(int id)
    {
        try
        {
            productDAO.delete(id);
        }
        catch (Exception e)
        {
            throw new RuntimeException("eroare la stergerea produsului: " + e.getMessage());
        }
    }

    /**
     * actualizeaza datele unui produs existent
     * @param product produsul cu datele actualizate (include id ul)
     */
    public void updateProduct(Product product)
    {
        try
        {
            productDAO.update(product, product.getId());
        }
        catch (Exception e)
        {
            throw new RuntimeException("eroare la actualizarea produsului: " + e.getMessage());
        }
    }
}