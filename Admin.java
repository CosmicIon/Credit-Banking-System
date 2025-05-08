public class Admin extends User {
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    public void viewCreditRequests(BankSystem bank) {
        for (CreditRequest request : bank.getCreditRequests()) {
            System.out.println(request);
        }
    }

    public void approveCreditRequest(CreditRequest request) {
        request.setApproved(true);
    }
}