// ConsoleUI.java
import java.util.*;

public class ConsoleUI {
    private Scanner scanner;
    private BankSystem bankSystem;
    private Admin admin;

    public ConsoleUI(BankSystem bankSystem, Admin admin) {
        this.bankSystem = bankSystem;
        this.admin = admin;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as Customer");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Admin username: ");
                    String aUser = scanner.nextLine();
                    System.out.print("Enter Admin password: ");
                    String aPass = scanner.nextLine();
                    if (admin.getUsername().equals(aUser) && admin.authenticate(aPass)) {
                        adminMenu(admin);
                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                    break;
                case 2:
                    System.out.print("Enter Customer username: ");
                    String cUser = scanner.nextLine();
                    System.out.print("Enter Customer password: ");
                    String cPass = scanner.nextLine();
                    Customer cust = bankSystem.getCustomerByUsername(cUser);
                    if (cust != null && cust.authenticate(cPass)) {
                        customerMenu(cust);
                    } else {
                        System.out.println("Invalid customer credentials.");
                    }
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Register Customer");
            System.out.println("2. View Credit Requests");
            System.out.println("3. Approve Credit Request");
            System.out.println("4. View All Customers");
            System.out.println("5. Impose Penalty");
            System.out.println("6. View Customer Transactions");
            System.out.println("7. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter customer username: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter customer password: ");
                    String password = scanner.nextLine();
                    Customer newCustomer = new Customer(name, password);
                    bankSystem.addCustomer(newCustomer);
                    System.out.println("Customer registered successfully.");
                    break;
                case 2:
                    admin.viewCreditRequests(bankSystem);
                    break;
                case 3:
                    System.out.print("Enter customer username to approve request: ");
                    String requestUser = scanner.nextLine();
                    for (CreditRequest cr : bankSystem.getCreditRequests()) {
                        if (cr.getCustomerUsername().equalsIgnoreCase(requestUser)) {
                            admin.approveCreditRequest(cr);
                            Customer approvedCust = bankSystem.getCustomerByUsername(requestUser);
                            if (approvedCust != null) approvedCust.setCreditApproved(true);
                            System.out.println("Credit request approved.");
                            break;
                        }
                    }
                    break;
                case 4:
                    for (Customer c : bankSystem.getCustomers()) {
                        System.out.println(c);
                    }
                    break;
                case 5:
                    System.out.print("Enter customer username: ");
                    String custUser = scanner.nextLine();
                    Customer customer = bankSystem.getCustomerByUsername(custUser);
                    if (customer != null) {
                        System.out.print("Enter penalty amount: ");
                        double amt = scanner.nextDouble();
                        scanner.nextLine();
                        admin.imposePenalty(customer, amt);
                        System.out.println("Penalty imposed.");
                    } else {
                        System.out.println("Customer not found.");
                    }
                    break;
                case 6:
                    System.out.print("Enter customer username: ");
                    String transUser = scanner.nextLine();
                    Customer transCustomer = bankSystem.getCustomerByUsername(transUser);
                    if (transCustomer != null) {
                        admin.viewCustomerTransactions(transCustomer);
                    } else {
                        System.out.println("Customer not found.");
                    }
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Request Credit");
            System.out.println("4. View Credit Status");
            System.out.println("5. View Transactions");
            System.out.println("6. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount: ");
                    double dep = scanner.nextDouble();
                    scanner.nextLine();
                    customer.deposit(dep);
                    break;
                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    double wdr = scanner.nextDouble();
                    scanner.nextLine();
                    customer.withdraw(wdr);
                    break;
                case 3:
                    System.out.print("Enter credit amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    if (amount <= 0.0) {
                        System.out.println("Invalid credit amount.");
                        break;
                    }
                    System.out.print("Enter reason: ");
                    String reason = scanner.nextLine();
                    CreditRequest request = new CreditRequest(customer.getUsername(), amount, reason);
                    bankSystem.addCreditRequest(request);
                    customer.getTransactions().add(new Transaction("Credit Request", amount, reason));
                    System.out.println("Credit request submitted.");
                    break;
                case 4:
                    System.out.println("Credit Approved: " + customer.isCreditApproved());
                    break;
                case 5:
                    for (Transaction t : customer.getTransactions()) {
                        System.out.println(t);
                    }
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}