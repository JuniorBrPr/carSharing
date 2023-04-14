package carsharing.data;

import carsharing.models.Car;
import carsharing.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDAO extends DAO<Customer>{
    public CustomerDAO(Database database) {
        super(database);
    }

    @Override
    public void createTable() {
        try{
            Statement statement = this.connection.createStatement();
            String sql = """
                    CREATE TABLE IF NOT EXISTS Customer (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name           VARCHAR(100) UNIQUE NOT NULL,
                        rented_car_id  INT DEFAULT NULL,
                        FOREIGN KEY (rented_car_id) REFERENCES Car(id)
                    );
                    """;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Customer customer) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "INSERT INTO Customer (name) VALUES ('%s')".formatted(customer.getName());
            statement.executeUpdate(sql);
            System.out.println("The customer was created!");
        } catch (SQLException e) {
            System.out.println("Error while creating customer! :"+ e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Customer> getAll() {
        try {
            ArrayList<Customer> customers = new ArrayList<>();
            Statement statement = this.connection.createStatement();
            String sql = """
                    SELECT * FROM Customer;
                    """;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setRentedCarId(resultSet.getInt("rented_car_id"));
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnCar(Customer customer) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "UPDATE Customer SET rented_car_id = NULL WHERE id = %d".formatted(customer.getId());
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error while returning car! :"+ e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void rentCar(Customer customer, Car car) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "UPDATE Customer SET rented_car_id = %d WHERE id = %d".formatted(car.getId(),
                    customer.getId());
            statement.executeUpdate(sql);
            System.out.println("\nYou rented \'" + car.getName() + "\'");
        } catch (SQLException e) {
            System.out.println("Error while renting car! :"+ e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
