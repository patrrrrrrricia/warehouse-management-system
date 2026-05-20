package bll;

import dao.ClientDAO;
import model.Client;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * BUSINESS LOGIC LAYER(BLL) for client operations
 * ClientBLL - gestioneaza operatiunile logice pentru entitatile de tip client,
 * facand legatura intre CONTROLLER(interfata) si DATA ACCESS LAYER (DAO).
 */
public class ClientBLL
{
    private final ClientDAO clientDAO;

    /**
     * constructor care initializeaza obiectul pentru accesul la date
     */
    public ClientBLL()
    {
        this.clientDAO = new ClientDAO();
    }

    /**
     * returneaza lista completa de clienti din baza de date
     * @return o lista cu toti clientii.
     */
    public List<Client> findAll()
    {
        return clientDAO.findAll();
    }

    /**
     * gaseste un client dupa id
     * @param id id-ul clientului cautat
     * @return clientul gasit
     * @throws NoSuchElementException daca clientul nu exista
     */
    public Client findClientById(int id)
    {
        //ofNullable - gestioneaza posibilitatea unui client sa fie NULL
        return Optional.ofNullable(clientDAO.findById(id))
                //daca e client -> il returneaza
                //daca e null -> arunca NoSuchElementException
                .orElseThrow(() -> new NoSuchElementException("the client with id =" + id + " was not found!"));
    }

    /**
     * insereaza un client nou in baza de date(DAO)
     * @param client obiectul client de inserat
     */
    public void insertClient(Client client)
    {
        try
        {
            clientDAO.insert(client);
        }
        catch (Exception e)
        {
            throw new RuntimeException("eroare la inserarea clientului: " + e.getMessage());
        }
    }

    /**
     * sterge un client din baza de date dupa id
     * @param id id-ul clientului de sters
     */
    public void deleteClient(int id)
    {
        try
        {
            clientDAO.delete(id);
        }
        catch (Exception e)
        {
            throw new RuntimeException("eroare la stergerea clientului: " + e.getMessage());
        }
    }

    /**
     * actualizeaza datele unui client existent
     * @param client clientul cu datele actualizate (include id-ul)
     */
    public void updateClient(Client client)
    {
        try
        {
            clientDAO.update(client, client.getId());
        }
        catch (Exception e)
        {
            throw new RuntimeException("eroare la actualizarea clientului: " + e.getMessage());
        }
    }
}