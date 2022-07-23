package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection dbConnection=null;
    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/vehicle_service_management_system", "root", "1234");
    }
    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {

        return (dbConnection==null)?dbConnection= new DBConnection():dbConnection;

    }

    public Connection getConnection(){
        return connection;
    }

}
