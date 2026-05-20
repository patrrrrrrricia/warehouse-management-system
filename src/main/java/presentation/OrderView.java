package presentation;

import model.Client;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * OrderView - interfata grafica pentru gestionarea comenzilor.
 * constructia ferestrei swing pentru selectia clientilor, produselor,
 * introducerea cantitatii si vizualizarea log-urilor de facturare.
 */
public class OrderView extends JFrame
{
    private final Color PINK_ACCENT = new Color(218, 131, 147);

    private JComboBox<String> cbClients, cbProducts;
    private JTextField txtQuantity;
    private JButton btnPlaceOrder;
    private DefaultTableModel tableModel;

    /**
     * constructor pentru initializarea componentelor vizuale ale ferestrei.
     */
    public OrderView()
    {
        setTitle("order management system");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // panel superior pentru input-uri
        JPanel pInput = new JPanel(new GridLayout(4, 2, 10, 10));
        pInput.setBackground(Color.WHITE);
        pInput.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // initializarea elementelor de selectie si a campurilor de text
        cbClients = new JComboBox<>();
        cbProducts = new JComboBox<>();
        txtQuantity = new JTextField();
        btnPlaceOrder = createStyledButton("place order");

        // adaugarea etichetelor si componentelor in grila
        pInput.add(new JLabel("select client:"));
        pInput.add(cbClients);
        pInput.add(new JLabel("select product:"));
        pInput.add(cbProducts);
        pInput.add(new JLabel("quantity:"));
        pInput.add(txtQuantity);
        pInput.add(new JLabel(""));
        pInput.add(btnPlaceOrder);

        add(pInput, BorderLayout.NORTH);

        // tabel pentru log-urile facturilor cu definirea coloanelor
        tableModel = new DefaultTableModel(new String[] {"id", "client", "product", "qty", "total"}, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // pozitionarea ferestrei pe centrul ecranului
        setLocationRelativeTo(null);
    }

    /**
     * popularea combobox-urilor prin stream api pentru procesarea listelor.
     * @param clients lista de clienti din baza de date.
     * @param products lista de produse din baza de date.
     */
    public void populateCombos(List<Client> clients, List<Product> products)
    {
        // extragerea numelor si adaugarea in meniurile dropdown
        clients.stream().map(Client::getName).forEach(cbClients::addItem);
        products.stream().map(Product::getName).forEach(cbProducts::addItem);
    }

    /**
     * metoda utilitara pentru crearea butoanelor cu design personalizat.
     * @param text textul de pe buton.
     * @return configurarea butonului.
     */
    private JButton createStyledButton(String text)
    {
        JButton btn = new JButton(text);
        btn.setBackground(PINK_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }

    // gettere pentru accesul controller-ului la componente
    public JComboBox<String> getCbClients() { return cbClients; }
    public JComboBox<String> getCbProducts() { return cbProducts; }
    public JTextField getTxtQuantity() { return txtQuantity; }
    public JButton getBtnPlaceOrder() { return btnPlaceOrder; }
    public DefaultTableModel getTableModel() { return tableModel; }
}