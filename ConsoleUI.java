import java.util.*;

public class ConsoleUI {
    private static Scanner scanner = new Scanner(System.in);
    private static BankSystem bank = new BankSystem();
    private static Admin admin = new Admin("Admin", "admin@bank.com", "admin123");

    public static void start() {
        while (true) {
            System.out.println("\n1. Register\n2. Login as Customer\n3. Login as Admin\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> customerLogin();
                case 3 -> adminLogin();
                case 4 -> System.exit(0);
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void register() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = new Customer(name, email, password);
        bank.addCustomer(customer);
        System.out.println("Customer registered successfully!");
    }

    private static void customerLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = bank.findCustomer(email, password);
        if (customer != null) {
            System.out.println("Welcome, " + customer.getName());
            customerMenu(customer);
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n1. Request Credit\n2. View Credit Status\n3. Logout");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter credit amount: ");
                    double amount = scanner.nextDouble();
                    CreditRequest request = customer.requestCredit(amount);
                    bank.addCreditRequest(request);
                    System.out.println("Credit request submitted.");
                }
                case 2 -> System.out.println("Credit Approved: " + customer.isCreditApproved());
                case 3 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void adminLogin() {
        System.out.print("Enter admin email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
            System.out.println("Welcome Admin");
            adminMenu();
        } else {
            System.out.println("Invalid admin credentials");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n1. View Credit Requests\n2. Approve Request\n3. View All Customers\n4. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> admin.viewCreditRequests(bank);
                case 2 -> {
                    System.out.print("Enter customer name to approve: ");
                    String name = scanner.nextLine();
                    for (CreditRequest request : bank.getCreditRequests()) {
                        if (request.getCustomer().getName().equalsIgnoreCase(name)) {
                            admin.approveCreditRequest(request);
                            System.out.println("Approved!");
                            break;
                        }
                    }
                }
                case 3 -> {
                    for (Customer c : bank.getCustomers()) {
                        System.out.println(c);
                    }
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
