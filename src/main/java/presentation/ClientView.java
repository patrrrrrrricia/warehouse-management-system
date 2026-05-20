package presentation;

import model.Client;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ClientView - interfata grafica pentru gestionarea clientilor.
 * aceasta clasa construieste fereastra principala pentru operatii cu clienti,
 * incluzand tabelul de vizualizare si panourile de introducere a datelor.
 */
public class ClientView extends JFrame
{
    // pt culoarea folosita la butoane
    private final Color PINK_ACCENT = new Color(218, 131, 147);

    // componentele grafice principale ale ferestrei
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JButton btnEdit, btnDelete, btnAdd;
    private JTextField txtId, txtName, txtAddress, txtEmail;

    /**
     * constructorul initializeaza layout-ul si toate elementele vizuale (tabel, butoane, campuri).
     */
    public ClientView()
    {
        setTitle("client management");
        setSize(800, 500);
        // setam inchiderea ferestrei fara a opri intreaga aplicatie
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // definire structura tabelului cu 4 coloane specifice clientului
        tableModel = new DefaultTableModel(new String[] {"id", "name", "address", "email"}, 0);
        clientTable = new JTable(tableModel);

        // adaugare listener pe modelul de selectie al tabelului
        // scopul este ca la click pe un rand, datele sa fie copiate automat in campurile de jos
        clientTable.getSelectionModel().addListSelectionListener(e ->
        {
            // verificam daca actiunea de selectie s-a finalizat si daca avem un rand valid selectat
            if (!e.getValueIsAdjusting() && clientTable.getSelectedRow() != -1)
            {
                int row = clientTable.getSelectedRow();

                // extragem informatiile din fiecare coloana a randului selectat
                Object id = tableModel.getValueAt(row, 0);
                Object name = tableModel.getValueAt(row, 1);
                Object address = tableModel.getValueAt(row, 2);
                Object email = tableModel.getValueAt(row, 3);

                // actualizam textul din campurile de input cu valorile extrase
                txtId.setText(id != null ? id.toString() : "");
                txtName.setText(name != null ? name.toString() : "");
                txtAddress.setText(address != null ? address.toString() : "");
                txtEmail.setText(email != null ? email.toString() : "");
            }
        });

        // adaugam tabelul intr-un scroll pane pentru a permite derularea daca lista e lunga
        add(new JScrollPane(clientTable), BorderLayout.CENTER);

        // cream un panou pentru butoanele de editare si stergere
        JPanel pTop = new JPanel(new FlowLayout());
        pTop.setBackground(Color.WHITE);
        btnEdit = createStyledButton("edit/update");
        btnDelete = createStyledButton("delete client");

        // adaugam butoanele in panou
        pTop.add(btnEdit);
        pTop.add(btnDelete);
        add(pTop, BorderLayout.NORTH);

        // cream un panou inferior pentru introducerea datelor noi (campuri text si buton adaugare)
        JPanel pInput = new JPanel(new GridLayout(1, 5, 10, 10));
        pInput.setBackground(Color.WHITE);
        pInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // initializam campurile text
        txtId = new JTextField();
        txtName = new JTextField();
        txtAddress = new JTextField();
        txtEmail = new JTextField();
        btnAdd = createStyledButton("add");

        // adaugam borduri cu titlu pentru fiecare camp ca sa fie clar ce trebuie introdus
        txtId.setBorder(BorderFactory.createTitledBorder("id"));
        txtName.setBorder(BorderFactory.createTitledBorder("name"));
        txtAddress.setBorder(BorderFactory.createTitledBorder("address"));
        txtEmail.setBorder(BorderFactory.createTitledBorder("email"));

        // adaugam componentele in panoul de input
        pInput.add(txtId);
        pInput.add(txtName);
        pInput.add(txtAddress);
        pInput.add(txtEmail);
        pInput.add(btnAdd);

        // adaugam acest panou in partea de jos a ferestrei
        add(pInput, BorderLayout.SOUTH);

        // centram fereastra pe ecranul utilizatorului
        setLocationRelativeTo(null);
    }

    /**
     * metoda pentru actualizarea tabelului cu date noi din baza de date.
     * primeste o lista de clienti si ii adauga rand cu rand in tabel.
     * @param clients lista de obiecte de tip client
     */
    public void refreshTable(List<Client> clients)
    {
        // golim continutul actual al tabelului inainte de a adauga noile date
        tableModel.setRowCount(0);
        // parcurgem lista si adaugam fiecare client ca un nou rand in tabel
        clients.forEach(c -> tableModel.addRow(new Object[] {c.getId(), c.getName(), c.getAddress(), c.getEmail()}));
    }

    /**
     * metoda helper pentru crearea butoanelor cu un design unitar.
     * @param text textul care va fi afisat pe buton.
     * @return instanta butonului configurat.
     */
    private JButton createStyledButton(String text)
    {
        JButton btn = new JButton(text);
        btn.setBackground(PINK_ACCENT); // setam culoarea de fundal definita la inceput
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);           //opacitatea pentru a vedea culoarea de fundal
        btn.setBorderPainted(false);
        return btn;
    }

    // sectiunea de getters pentru a oferi controller-ului acces la componente

    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnUpdate() { return btnEdit; }

    // getters pentru preluarea textului introdus de utilizator in interfata
    public String getIdInput() { return txtId.getText(); }
    public String getNameInput() { return txtName.getText(); }
    public String getAddressInput() { return txtAddress.getText(); }
    public String getEmailInput() { return txtEmail.getText(); }
}