package connection;

import java.sql.*;
import java.util.logging.Logger;

/**
 * CONNECTIONFACTORY - clasa pentru gestionarea conexiunilor la baza de date mysql
 *                   - furnizeaza un punct centralizat de acces pentru initierea conexiunilor jdbc
 * catre baza de date 'warehousedb', asigurand reutilizarea configurarilor de conectare
 */

public class ConnectionFactory
{
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());

    // configuratii de conectare
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String db_url = "jdbc:mysql://localhost:3306/warehousedb";
    private static final String user = "root";
    private static final String pass = "";

    /**
     * instanta unica a clasei (singleton pattern).
     */
    private static ConnectionFactory singleinstance = new ConnectionFactory();

    /**
     * constructor privat pentru a preveni instantierea externa
     * incarca driver-ul jdbc necesar pentru mysql
     */
    private ConnectionFactory()
    {
        try
        {
            Class.forName(driver);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * stabileste si returneaza o conexiune activa la baza de date
     * @return un obiect {@link connection} catre baza de date sau {@code null} in caz de eroare
     */
    public static Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(db_url, user, pass);
        }
        catch (SQLException e)
        {
            logger.severe("eroare la conectare jdbc!");
            return null;
        }
    }

    /**
     * inchide in mod sigur orice resursa jdbc (connection, statement, resultset)
     * @param resource resursa care implementeaza {@link AutoCloseable} ce trebuie inchisa
     */
    public static void close(AutoCloseable resource)
    {
        try
        {
            if (resource != null) resource.close();
        }
        catch (Exception e)
        {
            logger.warning("eroare la inchiderea resursei!");
        }
    }
}