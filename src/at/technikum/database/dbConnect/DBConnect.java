package at.technikum.database.dbConnect;
import at.technikum.tools.Tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnect extends Tools implements Cloneable, IDBConnect {


    private String databaseName;
    private String username;
    private String password;
    private String port;
    private String jdbcURL;
    protected Connection connection;
    private static DBConnect instance = null;


    /**
     *
     * @param databaseName Name     der Datenbank
     * @param username     Username der Datenbank
     * @param password     Password der Datenbank
     * @param port         Port     der Datenbank (STANDARD PORT: 5432)
     */

    /**
     * CONSTRUCTOR
     **/
    public DBConnect(String databaseName, String username, String password, String port) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver not found");
            e.printStackTrace();
        }

        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
        this.port = port;
        this.jdbcURL = "jdbc:postgresql://localhost:" + this.port + "/" + this.databaseName + "";
        this.startConnect();
    }

    /**
     * DEFAULT CONSTRUCTOR
     **/
    public DBConnect() {
        this("swe1db", "swe1user", "swe1pw", "5432");
    }

    /**
     * START CONNECTION
     **/
    private void startConnect() {
        try {
            this.connection = DriverManager.getConnection(this.jdbcURL, this.username, this.password);
            System.out.println(ANSI_GREEN + "CONNECT DB -- success" + ANSI_RESET);
        } catch (SQLException e) {
            System.out.println(ANSI_RED + "CONNECT DB -- failed" + ANSI_RESET);
            e.printStackTrace();
        }
    }

    /**
     * STOP CONNECTION
     **/
    public void stopConnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            this.connection = null;
        }
    }

    /**
     * Get CONNECTION
     **/
    public Connection getConnection() {
        if (connection == null) {
            this.startConnect();
        }
        return this.connection;
    }

    /**
     * Get CURRENT CONNECTION
     **/
    public static DBConnect getInstance() {
        if (instance == null)
            instance = new DBConnect();
        return instance;
    }

    static {
        DBConnect.instance = new DBConnect();
    }


}
