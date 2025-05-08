import java.util.*;

public class BankSystem {
    private List<Customer> customers = new ArrayList<>();
    private List<CreditRequest> creditRequests = new ArrayList<>();

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Customer findCustomer(String email, String password) {
        for (Customer c : customers) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }

    public void addCreditRequest(CreditRequest request) {
        creditRequests.add(request);
    }

    public List<CreditRequest> getCreditRequests() {
        return creditRequests;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}