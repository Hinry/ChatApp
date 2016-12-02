package sample;

import java.sql.*;

/**
 * Created by mrhri on 02.12.2016.
 */
public class Connector {

    private static final String url = "jdbc:mysql://localhost:3306/chater";
    private static final String user = "root";
    private static final String password = "";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private static String dateFormat;

    private static String getCurrentTimeStamp() {
        Date today = null;
        return dateFormat.format(String.valueOf(today.getTime()));
    }
    public static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(url, user,password);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
    public static void main(String[] args) throws SQLException {

        con = getDBConnection();
        stmt = con.createStatement();
        String query = "select * from chat_users WHERE login='Vladimir'";

        rs = stmt.executeQuery(query);

        while (rs.next()){
            System.out.println(rs.getString("password"));
        }

    }
}
