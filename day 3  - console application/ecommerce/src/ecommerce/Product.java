package ecommerce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Product {
	private int productId;
    private String productName;
    private int categoryId;
    private String categoryType;
    private double price;
    
    
    public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	// Method to add a new product to the database

    public void addNewProduct(Connection connection, Scanner scanner) throws SQLException {

        scanner.nextLine(); 
        System.out.print("Enter product name: ");
        setProductName(scanner.nextLine());

        System.out.print("Enter category ID: ");
        setCategoryId(scanner.nextInt());

        System.out.print("Enter category type: ");
        scanner.nextLine(); // Consume the leftover newline
        setCategoryType(scanner.nextLine());

        System.out.print("Enter price: ");
        setPrice(scanner.nextDouble());

        // Insert new product record into the database
        String insertProductQuery = "INSERT INTO product (product_name, category_id, category_type, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductQuery)) {
            preparedStatement.setString(1, getProductName());
            preparedStatement.setInt(2, getCategoryId());
            preparedStatement.setString(3, getCategoryType());
            preparedStatement.setDouble(4, getPrice());
            preparedStatement.executeUpdate();
            System.out.println("New product added successfully!");
            System.out.println("*.-.-.-.-.-.-.-.*");
        }
    }

    // Method to retrieve product details by product ID
    public static void viewProduct(Connection connection) throws SQLException {
        String selectProductsQuery = "SELECT * FROM product";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectProductsQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("Product ID: " + resultSet.getInt("product_id"));
                System.out.println("Product Name: " + resultSet.getString("product_name"));
                System.out.println("Category ID: " + resultSet.getInt("category_id"));
                System.out.println("Category Type: " + resultSet.getString("category_type"));
                System.out.println("Price: " + resultSet.getDouble("price"));
                System.out.println("*.-.-.-.-.-.-.-.*");
            }
        }
    }
}
