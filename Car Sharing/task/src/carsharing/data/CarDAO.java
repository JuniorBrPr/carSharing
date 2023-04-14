package carsharing.data;

import carsharing.models.Car;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDAO extends DAO<Car> {
    public CarDAO(Database database) {
        super(database);
    }

    @Override
    public void createTable() {
        try{
            Statement statement = this.connection.createStatement();
            String sql = """
                    CREATE TABLE IF NOT EXISTS Car (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name           VARCHAR(100) UNIQUE NOT NULL,
                        company_id     INT NOT NULL,
                        FOREIGN KEY (company_id) REFERENCES Company(id)
                    );
                    """;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Car car) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "INSERT INTO Car (name, company_id) VALUES ('%s', %d)".formatted(car.getName(),
                    car.getCompanyId());
            statement.executeUpdate(sql);
            System.out.println("The car was created!");
        } catch (SQLException e) {
            System.out.println("Error while creating car! :"+ e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Car> getAllFor(int companyId) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * FROM Car WHERE company_id = %d".formatted(companyId);
            ResultSet resultSet = statement.executeQuery(sql);
            ArrayList<Car> cars = new ArrayList<>();
            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setName(resultSet.getString("name"));
                car.setCompanyId(resultSet.getInt("company_id"));
                cars.add(car);
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
