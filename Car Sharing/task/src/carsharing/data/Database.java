package carsharing.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";


    private String databaseFileName;
    private Connection connection;

    public Database(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + this.databaseFileName);
            connection.setAutoCommit(true);
        } catch(Exception se) {
            se.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch(SQLException se){
            se.printStackTrace();
        }
    }
}