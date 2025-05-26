package model;
// BankSystem.java
import java.util.*;
import model.BankSystem;

public class BankSystem {
    private Map<String, Customer> customers;
    private List<CreditRequest> creditRequests;

    public BankSystem() {
        customers = new HashMap<>();
        creditRequests = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getUsername(), customer);
    }

    public Customer getCustomerByUsername(String username) {
        return customers.get(username);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers.values());
    }

    public void addCreditRequest(CreditRequest request) {
        creditRequests.add(request);
    }

    public List<CreditRequest> getCreditRequests() {
        return creditRequests;
    }
}
