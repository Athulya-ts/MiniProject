package ecommerce;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customer {
   
	
	//view Customers
	public static void viewCustomers(Connection connection) throws SQLException {
        String selectCustomersQuery = "SELECT * FROM customer";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectCustomersQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("Customer ID: " + resultSet.getInt("cust_id"));
                System.out.println("Customer Name: " + resultSet.getString("cust_name"));
                System.out.println("Contact No: " + resultSet.getLong("contact_no"));
                System.out.println("Address: " + resultSet.getString("address"));
                System.out.println("Email ID: " + resultSet.getString("email_id"));
                System.out.println("*.-.-.-.-.-.-.-.*");
            }
        }
    }
	
	//add new Customer
	public void addNewCustomer(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter customer name: ");
        String name = scanner.next();

        System.out.print("Enter contact number: ");
        long contactNumber = scanner.nextLong();

        System.out.print("Enter address: ");
        String address = scanner.next();

        System.out.print("Enter email ID: ");
        String emailId = scanner.next();

        System.out.print("Enter password: ");
        String password = scanner.next();

        String insertCustomerQuery = "INSERT INTO customer (cust_name, contact_no, address, email_id, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertCustomerQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, contactNumber);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, emailId);
            preparedStatement.setString(5, password);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("New customer added successfully!");
                System.out.println("*.-.-.-.-.-.-.-.*");
            } else {
                System.out.println("Failed to add new customer.");
                System.out.println("*.-.-.-.-.-.-.-.*");
            }
        }
    }
}
