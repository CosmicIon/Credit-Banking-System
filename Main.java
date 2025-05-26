// Main.java


import model.*;
import ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankSystem bankSystem = new BankSystem();
            Admin admin = new Admin("harsh", "hashtag");
            Customer sampleCustomer = new Customer("prakshi", "artist");
            bankSystem.addCustomer(sampleCustomer);

            new MainFrame(bankSystem, admin);
        });
    }
}
