package bll;

import dao.BillDAO;
import dao.ClientDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Bill;
import model.Client;
import model.Orders;
import model.Product;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * BUSINESS LOGIC LAYER(BLL) pentru procesarea comenzilor
 * OrderBLL - gestioneaza validarea stocului, actualizarea inventarului si generarea facturilor
 */
public class OrderBLL
{
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final BillDAO billDAO = new BillDAO();

    /**
     * finalizeaza procesul de achizitie
     * 1. verifica daca produsul si clientul exista
     * 2. verifica daca stocul este suficient
     * 3. actualizeaza stocul in baza de date
     * 4. salveaza comanda
     * 5. genereaza si salveaza factura
     *
     * @param clientid id-ul clientului care plaseaza comanda.
     * @param productid id-ul produsului dorit.
     * @param quantity cantitatea solicitata.
     */
    public void finalizeOrder(int clientid, int productid, int quantity)
    {
        // 1. verificare daca produsul si clientul exista in baza de date
        Product product = Optional.ofNullable(productDAO.findById(productid))
                .orElseThrow(() -> new NoSuchElementException("eroare: produsul cu id-ul " + productid + " nu exista!"));

        Client client = Optional.ofNullable(clientDAO.findById(clientid))
                .orElseThrow(() -> new NoSuchElementException("eroare: clientul cu id-ul " + clientid + " nu exista!"));

        // 2. verificare daca are destule produse pe stoc
        if (product.getStock() < quantity)
        {
            throw new IllegalArgumentException("eroare: stoc insuficient! produsul '" + product.getName() +
                    "' are doar " + product.getStock() + " unitati.");
        }

        // 3. scadere cantitatea comandata din stocul actual si salvare in baza de date(DAO)
        product.setStock(product.getStock() - quantity);
        productDAO.update(product, product.getId());

        // 4. creare comanda noua si salvare
        Orders neworder = new Orders();
        neworder.setClientId(clientid);
        neworder.setProductId(productid);
        neworder.setQuantity(quantity);
        orderDAO.insert(neworder);

        // 5. calculare pret total si generare factura pt client
        double totalamount = quantity * product.getPrice();
        Bill bill = new Bill(neworder.getId(), client.getName(), product.getName(), quantity, totalamount);
        billDAO.insert(bill);

        System.out.println("succes: comanda #" + neworder.getId() + " a fost finalizata pentru " + client.getName() + ".");
    }
}