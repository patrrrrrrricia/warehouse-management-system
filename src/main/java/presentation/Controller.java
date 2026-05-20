package presentation;

import bll.*;
import model.*;
import javax.swing.*;

/**
 * Controller - clasa pentru gestionarea interactiunii dintre interfata (view)
 * si logica de business (bll). preluarea actiunilor de la butoane,
 * extragerea datelor din interfata, apelarea metodelor din logica si actualizarea interfetei.
 */
public class Controller
{
    private final View view;
    // instantierea claselor de logica pentru clienti, produse si comenzi
    private final ClientBLL clientBLL = new ClientBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final OrderBLL orderBLL = new OrderBLL();

    /**
     * constructor pentru initializarea legaturii dintre interfata si logica.
     * definirea listener-elor pentru ferestrele secundare.
     * @param view instanta ferestrei principale.
     */
    public Controller(View view)
    {
        this.view = view;

        // setarea actiunilor pentru deschiderea ferestrelor secundare
        view.getBtnClients().addActionListener(e -> initClientWindow());
        view.getBtnProducts().addActionListener(e -> initProductWindow());
        view.getBtnOrders().addActionListener(e -> initOrderWindow());
    }

    /**
     * initializarea si afisarea ferestrei pentru gestionarea clientilor.
     * configurarea actiunilor pentru operatiile de adaugare, stergere si modificare.
     */
    private void initClientWindow()
    {
        ClientView cv = new ClientView();
        // incarcarea datelor initiale in tabel
        cv.refreshTable(clientBLL.findAll());

        // definirea actiunii butonului adaugare
        cv.getBtnAdd().addActionListener(ev ->
        {
            try
            {
                Client c = new Client();
                c.setName(cv.getNameInput());
                c.setAddress(cv.getAddressInput());
                c.setEmail(cv.getEmailInput());
                // apelarea logicii pentru inserare si actualizarea tabelului
                clientBLL.insertClient(c);
                cv.refreshTable(clientBLL.findAll());
                JOptionPane.showMessageDialog(cv, "client adaugat cu succes!");
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(cv, "eroare la adaugare: " + ex.getMessage());
            }
        });

        // definirea actiunii butonului stergere
        cv.getBtnDelete().addActionListener(ev ->
        {
            try
            {
                String idStr = cv.getIdInput();
                if (idStr.isEmpty()) throw new Exception("campul id este gol!");
                // stergerea inregistrarii dupa id si reincarcarea tabelului
                clientBLL.deleteClient(Integer.parseInt(idStr));
                cv.refreshTable(clientBLL.findAll());
                JOptionPane.showMessageDialog(cv, "client sters cu succes!");
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(cv, "eroare la stergere: " + ex.getMessage());
            }
        });

        // definirea actiunii butonului actualizare
        cv.getBtnUpdate().addActionListener(ev ->
        {
            try
            {
                String idStr = cv.getIdInput();
                if (idStr.isEmpty()) throw new Exception("campul id este gol!");
                Client c = new Client();
                c.setId(Integer.parseInt(idStr));
                c.setName(cv.getNameInput());
                c.setAddress(cv.getAddressInput());
                c.setEmail(cv.getEmailInput());
                // salvarea modificarilor in baza de date
                clientBLL.updateClient(c);
                cv.refreshTable(clientBLL.findAll());
                JOptionPane.showMessageDialog(cv, "client actualizat cu succes!");
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(cv, "eroare la actualizare: " + ex.getMessage());
            }
        });
        cv.setVisible(true);
    }

    /**
     * initializarea si afisarea ferestrei pentru gestionarea produselor.
     * configurarea actiunilor pentru operatii specifice produselor.
     */
    private void initProductWindow()
    {
        ProductView pv = new ProductView();
        pv.refreshTable(productBLL.findAll());

        // definirea actiunii pentru adaugarea unui produs nou
        pv.getBtnAdd().addActionListener(ev ->
        {
            try
            {
                Product p = new Product();
                p.setName(pv.getNameInput());
                p.setPrice(Double.parseDouble(pv.getPriceInput()));
                p.setStock(Double.parseDouble(pv.getStockInput()));
                productBLL.insertProduct(p);
                pv.refreshTable(productBLL.findAll());
                JOptionPane.showMessageDialog(pv, "produs adaugat cu succes!");
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(pv, "eroare: " + ex.getMessage());
            }
        });

        // definirea actiunii pentru stergerea unui produs
        pv.getBtnDelete().addActionListener(ev ->
        {
            try
            {
                String idStr = pv.getIdInput();
                if (idStr.isEmpty()) throw new Exception("campul id este gol!");
                productBLL.deleteProduct(Integer.parseInt(idStr));
                pv.refreshTable(productBLL.findAll());
                JOptionPane.showMessageDialog(pv, "produs sters cu succes!");
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(pv, "eroare la stergere: " + ex.getMessage());
            }
        });

        // definirea actiunii pentru actualizarea unui produs
        pv.getBtnUpdate().addActionListener(ev ->
        {
            try
            {
                String idStr = pv.getIdInput();
                if (idStr.isEmpty()) throw new Exception("campul id este gol!");
                Product p = new Product();
                p.setId(Integer.parseInt(idStr));
                p.setName(pv.getNameInput());
                p.setPrice(Double.parseDouble(pv.getPriceInput()));
                p.setStock(Double.parseDouble(pv.getStockInput()));
                productBLL.updateProduct(p);
                pv.refreshTable(productBLL.findAll());
                JOptionPane.showMessageDialog(pv, "produs actualizat cu succes!");
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(pv, "eroare la actualizare: " + ex.getMessage());
            }
        });
        pv.setVisible(true);
    }

    /**
     * initializarea si afisarea ferestrei pentru plasarea comenzilor.
     * gestionarea selectiei clientilor si produselor, calculul pretului si finalizarea comenzii.
     */
    private void initOrderWindow()
    {
        OrderView ov = new OrderView();
        // popularea listelor de selectie (combobox) cu date din baza
        ov.populateCombos(clientBLL.findAll(), productBLL.findAll());

        // definirea actiunii pentru finalizarea comenzii
        ov.getBtnPlaceOrder().addActionListener(ev ->
        {
            try
            {
                String clientName = (String) ov.getCbClients().getSelectedItem();
                String productName = (String) ov.getCbProducts().getSelectedItem();
                String qtyStr = ov.getTxtQuantity().getText();

                // validarea cantitatii introduse
                if (qtyStr.isEmpty()) throw new Exception("introduceti cantitatea!");

                int quantity = Integer.parseInt(qtyStr);

                // gasirea id-ului clientului selectat prin filtrarea listei
                int clientId = clientBLL.findAll().stream()
                        .filter(c -> c.getName() != null && c.getName().equals(clientName))
                        .findFirst().get().getId();

                // gasirea id-ului produsului selectat prin filtrarea listei
                int productId = productBLL.findAll().stream()
                        .filter(p -> p.getName() != null && p.getName().equals(productName))
                        .findFirst().get().getId();

                // apelarea logicii pentru finalizarea comenzii
                orderBLL.finalizeOrder(clientId, productId, quantity);

                // calcularea totalului si adaugarea unei linii in tabelul din interfata
                double price = productBLL.findProductById(productId).getPrice();
                double total = quantity * price;
                ov.getTableModel().addRow(new Object[] {"-", clientName, productName, quantity, total});

                JOptionPane.showMessageDialog(ov, "comanda finalizata!");
            }
            catch (Exception ex)
            {
                // afisarea erorii in cazul in care datele nu sunt valide sau stocul este insuficient
                JOptionPane.showMessageDialog(ov, "eroare: " + ex.getMessage(), "eroare", JOptionPane.ERROR_MESSAGE);
            }
        });
        ov.setVisible(true);
    }
}