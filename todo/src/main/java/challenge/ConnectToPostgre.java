package challenge;

import java.sql.*;

/**
 * Class to connect to postgresql database using JDBC driver.
 */
public class ConnectToPostgre {
    public Connection conn = null;
    public Operations operations = null;

    public ConnectToPostgre(String host, String database, String user, String password) {
        //localhost:5432, "challenge", "postgres", ""
        conn = ConnectToDatabase(host, database, user, password);
        operations = new Operations(conn);

        //test1(conn);
    }

    public Connection ConnectToDatabase(String host, String database, String user, String password) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + host + "/" + database;
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+ ": " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Database Open");
        return conn;
    }

    public void closeDatabase(Connection conn) {
        try {
            conn.close();
            System.out.println("closed!");
        } catch(Exception e) {
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