package challenge;

import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Class to connect to postgresql database using JDBC driver.
 */
public class ConnectToPostgre {
    public Connection conn = null;
    public Operations operations = null;
    private final static Logger logger = Logger.getLogger(ConnectToPostgre.class.getName());

    public ConnectToPostgre(String host, String database, String user, String password) {
        //localhost:5432, "challenge", "postgres", ""
        conn = ConnectToDatabase(host, database, user, password);
        operations = new Operations(conn);


        //test1(conn);
    }

    public Connection ConnectToDatabase(String host, String database, String user, String password) {
        Connection conn = null;
        logger.info("connecting to database");
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + host + "/" + database;
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.warning("error occurred while connecting to database. Error is: " + e.getClass().getName()+ ": " + e.getMessage());
            e.printStackTrace();
            System.err.println(e.getClass().getName()+ ": " + e.getMessage());
            return conn;
        }
        return conn;
    }

    public void closeDatabase(Connection conn) {
        try {
            logger.info("attempting to close connection with database.");
            conn.close();
        } catch(Exception e) {
            logger.warning("error occurred while attempting to close connection with database. Error message is: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void test1(Connection conn) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM todo");
            while (rs.next()) {
                System.out.println("either print once or never");

            }
            rs.close();
            st.close();
            closeDatabase(conn);
        } catch(SQLException se) {
            System.err.println("Threw a SQLException listing todos");
            System.err.println(se.getMessage());
        }
    }
    public static void main(String[] args) {
        new ConnectToPostgre("localhost:5432", "challenge", "postgres", "");

        System.out.println("done!!");
    }
}