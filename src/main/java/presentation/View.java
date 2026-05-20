package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * View - clasa principala de interfata grafica, oferind acces catre
 * modulele de gestionare a clientilor, produselor si comenzilor prin
 * butoane
 */
public class View extends JFrame
{
    private final Color BG_WHITE = Color.WHITE;
    private final Color PINK_ACCENT = new Color(218, 131, 147);
    private final Color TEXT_BLACK = new Color(30, 30, 30);

    private JButton btnClients, btnProducts, btnOrders;

    /**
     * constructor care initializeaza meniul principal si layout ul ferestrei
     */
    public View()
    {
        setTitle("orders management system");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_WHITE);
        setLayout(new BorderLayout());

        // header - titlu
        JLabel lblTitle = new JLabel("orders management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("sansserif", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_BLACK);
        lblTitle.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // panelul central pt butoane cu dispunere tip grid
        JPanel pButtons = new JPanel(new GridLayout(3, 1, 20, 20));
        pButtons.setBackground(BG_WHITE);
        pButtons.setBorder(new EmptyBorder(20, 60, 60, 60));

        // initializam butoanele principale
        btnClients = createStyledButton("manage clients");
        btnProducts = createStyledButton("manage products");
        btnOrders = createStyledButton("manage order");

        // adaugam butoanele in panel
        pButtons.add(btnClients);
        pButtons.add(btnProducts);
        pButtons.add(btnOrders);

        add(pButtons, BorderLayout.CENTER);

        // pt fereastra in centrul ecranului
        setLocationRelativeTo(null);
    }

    /**
     * metoda utilitara pentru a crea butoane cu design personalizat
     * @param text textul afisat pe buton
     * @return butonul configurat
     */
    private JButton createStyledButton(String text)
    {
        // cream instanta butonului cu textul primit
        JButton btn = new JButton(text);

        // setam fontul, culoarea textului si culoarea de fundal
        btn.setFont(new Font("sansserif", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(PINK_ACCENT);

        // setam proprietatile pentru desenarea zonei butonului si opacitatea
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        // dezactivam conturul de focus si schimbam cursorul la trecerea  cursorului
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // adaugam margini interioare pentru spatiere
        btn.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // efect schimbare culoare
        // adaugare listener care urmareste schimbarile de stare ale butonului
        btn.addChangeListener(e ->
        {
            // verificare daca butonul este apasat
            if (btn.getModel().isPressed())
            {
                // schimbare culoare de fundal si textul
                btn.setBackground(new Color(254, 225, 227));
                btn.setForeground(Color.WHITE);
            }
            // verificare cursor (hover)
            else if (btn.getModel().isRollover())
            {
                // schimbam culoarea pt efectul vizual la trecerea cursorului
                btn.setBackground(new Color(254, 225, 227, 255));
                btn.setForeground(Color.WHITE);
            }
            // daca nu, revine la culoarea principala
            else
            {
                btn.setBackground(PINK_ACCENT);
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    // gettere necesare pentru controller
    public JButton getBtnClients() { return btnClients; }
    public JButton getBtnProducts() { return btnProducts; }
    public JButton getBtnOrders() { return btnOrders; }
}