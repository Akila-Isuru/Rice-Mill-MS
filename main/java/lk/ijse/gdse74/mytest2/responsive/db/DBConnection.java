package lk.ijse.gdse74.mytest2.responsive.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;

    private final Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ rice_mill ","root","mysql");
    }

    public DBConnection(Connection connection) {

        this.connection = connection;
    }

    public static DBConnection getInstance() throws SQLException {
        //if (dbConnection == null) {
        //  dbConnection = new DBConnection();
        //dbConnection;
        //}
        return dbConnection == null ? new DBConnection() : dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
