package controller;

import model.*;
import model.service.BuyerService;
import model.service.DeviceService;
import model.service.SellerService;
import view.BuyerMenu;
import view.Menu;
import view.SellerMenu;
import view.Validation;

public class ElectronicDeviceController extends Menu<String> {
    private static String[] options = {
            "Enter Buyer ID",
            "Enter Seller ID",
            "Register a Buyer ID",
            "Exit"
    };

    private UserRepository userRepository;
    private BuyerService buyerService;
    private SellerService sellerService;
    private DeviceService deviceService;

    public ElectronicDeviceController(String title, String[] options) {
        super(title, options);
        this.userRepository = new UserRepository();
        this.deviceService = new DeviceService(new DeviceRepository());
        this.buyerService = new BuyerService(userRepository);
        this.sellerService = new SellerService(userRepository);
    }

    @Override
    public void execute(int choice) {
        switch (choice) {
            case 1 -> handleBuyerLogin();
            case 2 -> handleSellerLogin();
            case 3 -> registerBuyer();
            case 4 -> stop();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void registerBuyer() {
        String userID = Validation.getString("Enter a new User ID: ");
        if (userRepository.findUserByID(userID) != null) {
            System.out.println("User ID already exists. Please choose a different ID.");
            return;
        }

        String name = Validation.getString("Enter your name: ");
        String email = Validation.getString("Enter your email: ");
        String phoneNumber = Validation.getString("Enter your phone number: ");

        Buyer newBuyer = new Buyer(userID, name, email, phoneNumber);
        userRepository.addUser(newBuyer);
        System.out.println("Buyer registered successfully with User ID: " + userID);
    }

    private void handleSellerLogin() {
        String userID = Validation.getString("Enter Seller ID: ");
        User user = userRepository.findUserByID(userID);

        if (user instanceof Seller seller) {
            runSellerMenu(seller);
        } else {
            System.out.println("Seller not found. Please try again.");
        }
    }

    private void handleBuyerLogin() {
        String userID = Validation.getString("Enter Buyer ID: ");
        User user = userRepository.findUserByID(userID);

        if (user instanceof Buyer buyer) {
            runBuyerMenu(buyer);
        } else {
            System.out.println("Buyer not found. Please try again.");
        }
    }

    private void runSellerMenu(Seller seller) {
        String[] options = {"Add Device for Sale", "Remove Device", "View All Devices", "Register New Seller","Display all userID","Exit"};
        SellerMenu sellerMenu = new SellerMenu("Seller Menu", options, seller, deviceService, sellerService, userRepository);
        sellerMenu.run();
    }

    private void runBuyerMenu(Buyer buyer) {
        String[] options = {"View Available Devices", "Search Device by Name", "Purchase Device", "Exit"};
        BuyerMenu buyerMenu = new BuyerMenu("Buyer Menu", options, buyer, deviceService, buyerService);
        buyerMenu.run();
    }

    public static void main(String[] args) {
        new ElectronicDeviceController("Electronic Device Management System", options).run();
    }
}
