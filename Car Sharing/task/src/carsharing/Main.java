package carsharing;

import carsharing.data.CarDAO;
import carsharing.data.CompanyDAO;
import carsharing.data.Database;
import carsharing.models.Car;
import carsharing.models.Company;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Database database = new Database(args.length > 0 ? args[1] : "carSharing");
        CompanyDAO companyDAO = new CompanyDAO(database);
        CarDAO carDAO = new CarDAO(database);

        Scanner in = new Scanner(System.in);
        String option = "";

        companyDAO.createTable();
        carDAO.createTable();
        while (true) {
            switch (option) {
                case "1" -> {
                    System.out.println("""
                            1. Company list
                            2. Create a company
                            0. Back
                            """);
                    option = in.nextLine();
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
                                    while (!isBack2){
                                        String companyOption = "";
                                        System.out.println("Choose a company:");
                                        companies.forEach(System.out::println);
                                        System.out.println("0. Back");
                                        companyOption = in.nextLine();
                                        if (companyOption.equals("0")){
                                            isBack2 = true;
                                        } else if (companyOption.matches("\\d+")){
                                            int id = Integer.parseInt(companyOption);
                                            Company company = companies.stream()
                                                    .filter(c -> c.getId() == id).findFirst().orElse(null);
                                            if (company != null){
                                                while (!companyOption.equals("0")) {
                                                    System.out.println("Company name: " + company.getName());
                                                    System.out.println("1. Car list");
                                                    System.out.println("2. Create a car");
                                                    System.out.println("0. Back");
                                                    companyOption = in.nextLine();
                                                    switch (companyOption) {
                                                        case "1" -> {
                                                            List<Car> cars = carDAO.getAllFor(company.getId());
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
                                                            System.out.println("1. Car list");
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
                case "0" -> {
                    database.closeConnection();
                    return;
                }
                default -> {
                    System.out.println("1. Log in as a manager");
                    System.out.println("0. Exit");
                    option = in.nextLine();
                }
            }
        }
    }
}
