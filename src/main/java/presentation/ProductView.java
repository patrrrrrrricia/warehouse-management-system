package presentation;

import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ProductView - interfata grafica pentru gestionarea produselor
 * - construieste fereastra de tip swing care permite adaugarea,
 * vizualizarea, actualizarea si stergerea inregistrarilor din tabelul produselor
 */
public class ProductView extends JFrame
{
    private final Color PINK_ACCENT = new Color(218, 131, 147);
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton btnUpdate, btnDelete, btnAdd;
    private JTextField txtId, txtName, txtPrice, txtStock;

    /**
     * constructor care initializeaza componentele vizuale ale ferestrei
     */
    public ProductView()
    {
        setTitle("product management");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // definim modelul tabelului
        tableModel = new DefaultTableModel(new String[] {"id", "name", "price", "stock"}, 0);
        productTable = new JTable(tableModel);

        // click pe rand -> completeaza automat campurile din jos
        productTable.getSelectionModel().addListSelectionListener(e ->
        {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1)
            {
                int row = productTable.getSelectedRow();
                // extragem valorile din randul selectat
                Object id = tableModel.getValueAt(row, 0);
                Object name = tableModel.getValueAt(row, 1);
                Object price = tableModel.getValueAt(row, 2);
                Object stock = tableModel.getValueAt(row, 3);

                // populam campurile de editare
                txtId.setText(id != null ? id.toString() : "");
                txtName.setText(name != null ? name.toString() : "");
                txtPrice.setText(price != null ? price.toString() : "");
                txtStock.setText(stock != null ? stock.toString() : "");
            }
        });

        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // panel butoane (update/delete) sus
        JPanel pTop = new JPanel(new FlowLayout());
        pTop.setBackground(Color.WHITE);
        btnUpdate = createStyledButton("edit/update");
        btnDelete = createStyledButton("delete product");
        pTop.add(btnUpdate);
        pTop.add(btnDelete);
        add(pTop, BorderLayout.NORTH);

        // panel input (add) jos
        JPanel pInput = new JPanel(new GridLayout(1, 5, 10, 10));
        pInput.setBackground(Color.WHITE);
        pInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtId = new JTextField();
        txtName = new JTextField();
        txtPrice = new JTextField();
        txtStock = new JTextField();
        btnAdd = createStyledButton("add");

        // pt titlu
        txtId.setBorder(BorderFactory.createTitledBorder("id"));
        txtName.setBorder(BorderFactory.createTitledBorder("name"));
        txtPrice.setBorder(BorderFactory.createTitledBorder("price"));
        txtStock.setBorder(BorderFactory.createTitledBorder("stock"));

        pInput.add(txtId);
        pInput.add(txtName);
        pInput.add(txtPrice);
        pInput.add(txtStock);
        pInput.add(btnAdd);
        add(pInput, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    /**
     * reimprospateaza tabelul cu lista actualizata de produse din baza de date
     * @param products lista de obiecte product de afisat
     */
    public void refreshTable(List<Product> products)
    {
        // golim tabelul si adaugam noile date
        tableModel.setRowCount(0);
        products.forEach(p -> tableModel.addRow(new Object[] {p.getId(), p.getName(), p.getPrice(), p.getStock()}));
    }

    /**
     * metoda utilitara pentru crearea butoanelor
     * @param text textul afisat pe buton
     * @return butonul configurat
     */
    private JButton createStyledButton(String text)
    {
        JButton btn = new JButton(text);
        btn.setBackground(PINK_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    // metodele necesare pentru controller
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnUpdate() { return btnUpdate; }

    public String getIdInput() { return txtId.getText(); }
    public String getNameInput() { return txtName.getText(); }
    public String getPriceInput() { return txtPrice.getText(); }
    public String getStockInput() { return txtStock.getText(); }
}