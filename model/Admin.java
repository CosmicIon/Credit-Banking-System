package model;

// Admin.java
public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void imposePenalty(Customer customer, double amount) {
        customer.addPenalty(amount);
    }

    public void viewCustomerTransactions(Customer customer) {
        System.out.println("Transactions for " + customer.getUsername() + ":");
        for (Transaction t : customer.getTransactions()) {
            System.out.println(t);
        }
}

    public void viewCreditRequests(BankSystem bankSystem) {
        for (CreditRequest cr : bankSystem.getCreditRequests()) {
            System.out.println(cr);
        }
    }

    public void approveCreditRequest(CreditRequest request) {
        request.approve();
    }
}