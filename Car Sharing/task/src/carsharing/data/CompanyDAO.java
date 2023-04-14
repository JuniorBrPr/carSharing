package carsharing.data;

import carsharing.models.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO extends DAO<Company> {

    public CompanyDAO(Database database) {
        super(database);
    }

    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = """
                    CREATE TABLE IF NOT EXISTS Company (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name           VARCHAR(100) UNIQUE NOT NULL
                    );
                    """;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void insert(Company company) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "INSERT INTO COMPANY (name) VALUES ('%s')".formatted(company.getName());
            statement.executeUpdate(sql);
            System.out.println("The company was created!");
        } catch (SQLException e) {
            System.out.println("Error while creating company! :" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Company> getAll() {
        ArrayList<Company> companies = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sql = """
                    SELECT * FROM COMPANY;
                    """;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Company company = new Company();
                company.setId(resultSet.getInt("id"));
                company.setName(resultSet.getString("name"));
                companies.add(company);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return companies;
    }
}
