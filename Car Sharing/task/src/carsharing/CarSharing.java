package carsharing;

import carsharing.data.CarDAO;
import carsharing.data.CompanyDAO;
import carsharing.data.CustomerDAO;
import carsharing.data.Database;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarSharing {
    private Database database;
    private CompanyDAO companyDAO;
    private CustomerDAO customerDAO;
    private CarDAO carDAO;
    private Scanner in = new Scanner(System.in);

    public CarSharing(String databaseName) {
        this.database = new Database(databaseName);
        this.companyDAO = new CompanyDAO(database);
        this.customerDAO = new CustomerDAO(database);
        this.carDAO = new CarDAO(database);
    }

    public void start() {
        String option = "";
        companyDAO.createTable();
        carDAO.createTable();
        customerDAO.createTable();
        while (true) {
            switch (option) {
                case "1" -> {
                    adminMenu();
                    option = "";
                }
                case "2" -> {
                    customerMenu();
                    option = "";
                }
                case "3" -> {
                    createCustomer();
                    option = "";
                }
                case "0" -> {
                    database.closeConnection();
                    return;
                }
                default -> {
                    System.out.println("""
                            1. Log in as a manager
                            2. Log in as a customer
                            3. Create a customer
                            0. Exit""");
                    option = in.nextLine();
                }
            }
        }
    }

    private void customerMenu() {
        boolean returnedCar = false;
        ArrayList<Customer> customers = customerDAO.getAll();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            boolean isBack = false;
            while (!isBack) {
                String customerOption = "";
                System.out.println("\nChoose a customer:");
                for (int i = 0; i < customers.size(); i++) {
                    System.out.println((i + 1) + ". " + customers.get(i).getName());
                }
                System.out.println("0. Back");
                customerOption = in.nextLine();
                if (customerOption.equals("0")) {
                    isBack = true;
                } else if (customerOption.matches("\\d+")) {
                    int id = Integer.parseInt(customerOption);
                    Customer customer = customers.stream()
                            .filter(c -> c.getId() == id).findFirst().orElse(null);
                    if (customer != null) {
                        while (!customerOption.equals("0")) {
                            System.out.println("\nCustomer name: " + customer.getName());
                            System.out.println("1. Rent a car");
                            System.out.println("2. Return a rented car");
                            System.out.println("3. My rented car");
                            System.out.println("0. Back");
                            customerOption = in.nextLine();
                            switch (customerOption) {
                                case "1" -> {
                                    customerOption = "";
                                    if (customer.getRentedCarId() != 0){
                                        System.out.println("\nYou've already rented a car!");
                                        break;
                                    }
                                    List<Company> companies = companyDAO.getAll();
                                    if (companies.isEmpty()) {
                                        System.out.println("The company list is empty!");
                                    } else {
                                        System.out.println("\nChoose a company:");
                                        for (int i = 0; i < companies.size(); i++) {
                                            System.out.println((i + 1) + ". " + companies.get(i).getName());
                                        }
                                        System.out.println("0. Back");
                                        String companyOption = in.nextLine();
                                        if (companyOption.equals("0")) {
                                            customerOption = "0";
                                        } else if (companyOption.matches("\\d+")) {
                                            int companyId = Integer.parseInt(companyOption);
                                            Company company = companies.stream()
                                                    .filter(c -> c.getId() == companyId).findFirst()
                                                    .orElse(null);
                                            if (company != null) {
                                                List<Car> cars = carDAO.getAllFor(company);
                                                if (cars.isEmpty()) {
                                                    System.out.println("The car list is empty!");
                                                } else {
                                                    System.out.println("\nChoose a car:");
                                                    for (int i = 0; i < cars.size(); i++) {
                                                        System.out.println((i + 1) + ". " + cars.get(i).getName());
                                                    }
                                                    System.out.println("0. Back");
                                                    String carOption = in.nextLine();
                                                    if (carOption.equals("0")) {
                                                        customerOption = "0";
                                                    } else if (carOption.matches("\\d+")) {
                                                        int carId = Integer.parseInt(carOption);
                                                        Car car = cars.stream()
                                                                .filter(c -> c.getId() == carId)
                                                                .findFirst().orElse(null);
                                                        if (car != null) {
                                                            customerDAO.rentCar(customer, car);
                                                            customer.setRentedCarId(car.getId());
                                                        } else {
                                                            System.out.println("Wrong car id!");
                                                        }
                                                    } else {
                                                        System.out.println("Wrong input!");
                                                    }
                                                }
                                            } else {
                                                System.out.println("Wrong company id!");
                                            }
                                        } else {
                                            System.out.println("Wrong input!");
                                        }
                                    }
                                }
                                case "2" -> {
                                    customerOption = "";
                                    if (customer.getRentedCarId() == 0) {
                                        System.out.println("You didn't rent a car!");
                                    } else {
                                        customerDAO.returnCar(customer);
                                        customer.setRentedCarId(0);
                                        System.out.println("\nYou've returned a rented car!");
                                    }
                                }
                                case "3" -> {
                                    customerOption = "";
                                    if (customer.getRentedCarId() == 0) {
                                        System.out.println("You didn't rent a car!");
                                    } else {
                                        Car car = carDAO.getRentedCar(customer);
                                        System.out.println("\nYour rented car:");
                                        System.out.println(car.getName());
                                        System.out.println("Company:");
                                        System.out.println(companyDAO.getAll().stream()
                                                .filter(c -> c.getId() == car.getCompanyId()).findFirst()
                                                .orElse(null));
                                    }
                                }
                                case "0" -> {
                                    customerOption = "0";
                                }
                                default -> {
                                    System.out.println("Incorrect option! Try again.");
                                }
                            }
                        }
                    } else {
                        System.out.println("There is no customer with id " + id);
                    }
                } else {
                    System.out.println("Incorrect option! Try again.");
                }
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = in.nextLine();
        customerDAO.insert(new Customer(name));
    }

    private void adminMenu() {
        System.out.println("""
                
                1. Company list
                2. Create a company
                0. Back
                """);
        String option = in.nextLine();

        boolean isBack = false;

        while (!isBack)
            switch (option) {
                case "1" -> {
                    option = "";
                    List<Company> companies = companyDAO.getAll();
                    if (companies.isEmpty()) {
                        System.out.println("The company list is empty!");
                    } else {
                        boolean isBack2 = false;
                        while (!isBack2) {
                            String companyOption = "";
                            System.out.println("Choose a company:");
                            companies.forEach(System.out::println);
                            System.out.println("0. Back");
                            companyOption = in.nextLine();
                            if (companyOption.equals("0")) {
                                isBack2 = true;
                            } else if (companyOption.matches("\\d+")) {
                                int id = Integer.parseInt(companyOption);
                                Company company = companies.stream()
                                        .filter(c -> c.getId() == id).findFirst().orElse(null);
                                if (company != null) {
                                    while (!companyOption.equals("0")) {
                                        System.out.println("\nCompany name: " + company.getName());
                                        System.out.println("1. Car list");
                                        System.out.println("2. Create a car");
                                        System.out.println("0. Back");
                                        companyOption = in.nextLine();
                                        switch (companyOption) {
                                            case "1" -> {
                                                List<Car> cars = carDAO.getAllFor(company);
                                                if (cars.isEmpty()) {
                                                    System.out.println("The car list is empty!");
                                                } else {
                                                    System.out.println("Car list:");
                                                    int i = 1;
                                                    for (Car car : cars) {
                                                        System.out.println(i + ". " + car.getName());
                                                        i++;
                                                    }
                                                }
                                            }
                                            case "2" -> {
                                                System.out.println("Enter the car name:");
                                                carDAO.insert(new Car(in.nextLine(), company.getId()));
                                            }
                                            case "0" -> isBack2 = true;
                                            default -> {
                                                System.out.println("\n1. Car list");
                                                System.out.println("2. Create a car");
                                                System.out.println("0. Back");
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("Company not found!");
                                }
                            } else {
                                System.out.println("Company not found!");
                            }
                        }
                    }
                }
                case "2" -> {
                    System.out.println("Enter the company name:");
                    companyDAO.insert(new Company(in.nextLine()));
                    option = "";
                }
                case "0" -> {
                    option = "";
                    isBack = true;
                }
                default -> {
                    System.out.println("""
                            
                            1. Company list
                            2. Create a company
                            0. Back
                            """);
                    option = in.nextLine();
                }
            }
    }
}


