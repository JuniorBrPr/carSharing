package carsharing;

import carsharing.data.CompanyDAO;
import carsharing.data.Database;
import carsharing.models.Company;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Database database = new Database(args.length > 0 ? args[1] : "carSharing");
        CompanyDAO companyDAO = new CompanyDAO(database);

        Scanner in = new Scanner(System.in);
        String option = "";

        companyDAO.createTable();
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
                                    System.out.println("Company list:");
                                    companies.forEach(System.out::println);
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
