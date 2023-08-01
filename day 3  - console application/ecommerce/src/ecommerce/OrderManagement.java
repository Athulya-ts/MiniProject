package ecommerce;
//
//public class OrderManagement {
//
//}
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class OrderManagement {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize database connection
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "Achukuttu1*")) {
            Customer customer = new Customer();
            Product product = new Product();
            Order order = new Order();

            while (true) {
                System.out.println("Welcome to EasyShop - Order Management System App");
                System.out.println();
                System.out.println("1. Place an order");
                System.out.println("2. View orders");
                System.out.println("3. Update order");
                System.out.println("4. Cancel order");
                System.out.println("5. Add a new customer");
                System.out.println("6. Add a new product");
                System.out.println("7. View customers");
                System.out.println("8. View products");
                System.out.println("9. View all orders");
                System.out.println("10. Exit");
                System.out.println();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                	case 1:
                		order.placeOrder(connection, scanner);
                		break;
                	case 2:
                		order.viewOrder(connection, scanner);
                		break;
                	case 3:
                		order.updateOrder(connection, scanner);
                		break;
                	case 4:
                		order.deleteOrder(connection, scanner);
                		break;
                    case 5:
                        customer.addNewCustomer(connection, scanner);
                        break;
                    case 6:
                        product.addNewProduct(connection, scanner);
                        break;
                    case 7:
                        customer.viewCustomers(connection);
                        break;
                    case 8:
                        product.viewProduct(connection);
                        break;
                    case 9:
                    	order.viewOrder();
                    	break;
                    case 10:
                        System.out.println("Exiting the app. Goodbye!");
                        System.out.println("-----------xxx-----------");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        System.out.println("-----------xxx-----------");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
