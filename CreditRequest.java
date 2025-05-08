public class CreditRequest {
    private Customer customer;
    private double amount;
    private boolean approved;

    public CreditRequest(Customer customer, double amount) {
        this.customer = customer;
        this.amount = amount;
        this.approved = false;
    }

    public Customer getCustomer() { return customer; }
    public double getAmount() { return amount; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) {
        this.approved = approved;
        customer.setCreditApproved(approved);
    }

    @Override
    public String toString() {
        return "Request by: " + customer.getName() + ", Amount: ₹" + amount + ", Approved: " + approved;
    }
}