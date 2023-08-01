package ecommerce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

interface Orders{
	public void viewOrder() throws SQLException;
}

abstract class OrderAbstract{
	abstract public void placeOrder(Connection connection,Scanner scanner)  throws SQLException;
}

public class Order extends OrderAbstract implements Orders{


	public void placeOrder(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();

        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        // Retrieve product details by product ID to calculate total price
        Product product = new Product();
        double price = getProductPrice(connection, productId);
        if (price == 0) {
            System.out.println("Product with ID " + productId + " not found.");
            System.out.println("*.-.-.-.-.-.-.-.*");
            return;
        }

        double totalPrice = price * quantity;

        // Insert new order record into the database
        String insertOrderQuery = "INSERT INTO orders (cust_id, product_id, quantity, total_price, order_date) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrderQuery)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, totalPrice);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order placed successfully!");
                System.out.println("*.-.-.-.-.-.-.-.*");
            } else {
                System.out.println("Failed to place order.");
                System.out.println("*.-.-.-.-.-.-.-.*");
            }
        }
    }
	
//delete order
	public static void deleteOrder(Connection connection, Scanner scanner) throws SQLException {
        
		System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        
        String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteOrderQuery)) {
            preparedStatement.setInt(1, orderId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order with ID " + orderId + " deleted successfully!");
                System.out.println("*.-.-.-.-.-.-.-.*");
            } else {
                System.out.println("Order with ID " + orderId + " not found.");
                System.out.println("*.-.-.-.-.-.-.-.*");
            }
        }
    }
	
	
    // Method to retrieve product price by product ID
    private double getProductPrice(Connection connection, int productId) throws SQLException {
        String selectProductQuery = "SELECT price FROM product WHERE product_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectProductQuery)) {
            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("price");
                }
            }
        }
        return 0;
    }

        // Method to update an existing order in the database
    public void updateOrder(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();

        // Retrieve order details by order ID to display current information
        String selectOrderQuery = "SELECT * FROM orders WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectOrderQuery)) {
            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Current Order Details:");
                    System.out.println("*.-.-.-.-.-.-.-.*");
                    System.out.println("Order ID: " + resultSet.getInt("order_id"));
                    System.out.println("Customer ID: " + resultSet.getInt("cust_id"));
                    System.out.println("Product ID: " + resultSet.getInt("product_id"));
                    System.out.println("Current Quantity: " + resultSet.getInt("quantity"));
                    System.out.println("Current Total Price: " + resultSet.getDouble("total_price"));
                    System.out.println("*.-.-.-.-.-.-.-.*");

                    // Prompt the user to update the quantity and total price
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();

                    // Retrieve product details by product ID to calculate new total price
                    int productId = resultSet.getInt("product_id");
                    double price = getProductPrice(connection, productId);
                    if (price == 0) {
                        System.out.println("Product with ID " + productId + " not found.");
                        return;
                    }

                    double newTotalPrice = price * newQuantity;

                    // Update the order record in the database
                    String updateOrderQuery = "UPDATE orders SET quantity = ?, total_price = ? WHERE order_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateOrderQuery)) {
                        updateStatement.setInt(1, newQuantity);
                        updateStatement.setDouble(2, newTotalPrice);
                        updateStatement.setInt(3, orderId);
                        int rowsAffected = updateStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Order updated successfully!");
                            System.out.println("*.-.-.-.-.-.-.-.*");
                        } else {
                            System.out.println("Failed to update order.");
                            System.out.println("*.-.-.-.-.-.-.-.*");
                        }
                    }
                } else {
                    System.out.println("Order with ID " + orderId + " not found.");
                    System.out.println("*.-.-.-.-.-.-.-.*");
                }
            }
        }
    }
	// Method to view order details for a customer
    public void viewOrder(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();

        String selectOrderQuery = "SELECT * FROM orders WHERE cust_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectOrderQuery)) {
            preparedStatement.setInt(1, customerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("Order Details:");
                    System.out.println("*.-.-.-.-.-.-.-.*");
                    System.out.println("Order ID: " + resultSet.getInt("order_id"));
                    System.out.println("Customer ID: " + resultSet.getInt("cust_id"));
                    System.out.println("Product ID: " + resultSet.getInt("product_id"));
                    System.out.println("Quantity: " + resultSet.getInt("quantity"));
                    System.out.println("Total Price: " + resultSet.getDouble("total_price"));
                    System.out.println("Order Date: " + resultSet.getString("order_date"));
                    System.out.println("*.-.-.-.-.-.-.-.*");
                }
            }
        }
    }

	@Override
	public void viewOrder() throws SQLException {
		String selectOrderQuery = "SELECT * FROM orders";
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "Achukuttu1*");
		try (PreparedStatement preparedStatement = connection.prepareStatement(selectOrderQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("Order Details:");
                    System.out.println("*.-.-.-.-.-.-.-.*");
                    System.out.println("Customer id: "+resultSet.getInt("cust_id"));
                    System.out.println("Order ID: " + resultSet.getInt("order_id"));
                    System.out.println("Customer ID: " + resultSet.getInt("cust_id"));
                    System.out.println("Product ID: " + resultSet.getInt("product_id"));
                    System.out.println("Quantity: " + resultSet.getInt("quantity"));
                    System.out.println("Total Price: " + resultSet.getDouble("total_price"));
                    System.out.println("Order Date: " + resultSet.getString("order_date"));
                    System.out.println("*.-.-.-.-.-.-.-.*");
                }
            }
        }
		
		
	}
}
