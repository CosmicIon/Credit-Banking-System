import java.util.*;

public class ConsoleUI {
    private static Scanner scanner = new Scanner(System.in);
    private static BankSystem bank = new BankSystem();
    private static Admin admin = new Admin("Harsh", "harsh@bank.com", "harsh123");

    public static void start() {
        while (true) {
            System.out.println("\n1. Login as Customer\n2. Login as Admin\n3. Exit");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> customerLogin();
                case 2 -> adminLogin();
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid choice");
            }
        }
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
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter credit amount: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    if (amount <= 0) {
                        System.out.println("Invalid credit amount.");
                        break;
                    }
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
            System.out.println("\n1. Register Customer\n2. View Credit Requests\n3. Approve Request\n4. View All Customers\n5. Logout");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> registerCustomer();
                case 2 -> admin.viewCreditRequests(bank);
                case 3 -> {
                    System.out.print("Enter customer email to approve: ");
                    String email = scanner.nextLine();
                    for (CreditRequest request : bank.getCreditRequests()) {
                        if (request.getCustomer().getEmail().equalsIgnoreCase(email)) {
                            admin.approveCreditRequest(request);
                            System.out.println("Approved!");
                            break;
                        }
                    }
                }
                case 4 -> {
                    for (Customer c : bank.getCustomers()) {
                        System.out.println(c);
                    }
                }
                case 5 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void registerCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        System.out.print("Enter customer password: ");
        String password = scanner.nextLine();

        Customer customer = new Customer(name, email, password);
        bank.addCustomer(customer);
        System.out.println("Customer registered successfully by Admin!");
    }
}
