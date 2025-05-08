public class Customer extends User {
    private boolean hasRequestedCredit = false;
    private boolean isCreditApproved = false;

    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    public CreditRequest requestCredit(double amount) {
        hasRequestedCredit = true;
        return new CreditRequest(this, amount);
    }

    public void setCreditApproved(boolean approved) {
        isCreditApproved = approved;
    }

    public boolean isCreditApproved() {
        return isCreditApproved;
    }

    @Override
    public String toString() {
        return name + " (" + email + ") - Credit Approved: " + isCreditApproved;
    }
}