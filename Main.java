public class Main {
    public static void main(String[] args) {
        BankSystem system = new BankSystem();
        Admin admin = new Admin("harsh", "hashtag");
        Customer cust1 = new Customer("prakshi", "artist");
        system.addCustomer(cust1);
        ConsoleUI ui = new ConsoleUI(system, admin);
        ui.start();
    }
}