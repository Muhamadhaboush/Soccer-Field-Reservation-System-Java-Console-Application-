

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SystemmManager manager = new SystemmManager();

        while (true) {
            System.out.println("\n=====================================");
            System.out.println("   ⚽ Soccer Field RESERVATION SYSTEM ⚽");
            System.out.println("=====================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Forgot Password");
            System.out.println("9. Admin Panel");
            System.out.println("0. Exit System");
            System.out.print("Your Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 0) {
                System.out.println("System shutting down...");
                break;
            } 
            else if (choice == 2) {
                // REGISTRATION SCREEN
                System.out.println("\n--- NEW USER REGISTRATION ---");
                System.out.print("Full Name: ");
                String name = scanner.nextLine();
                System.out.print("Phone Number (10 digits): ");
                String phone = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                System.out.print("Security Question (e.g., Name of your first pet?): ");
                String question = scanner.nextLine();
                System.out.print("Your Answer: ");
                String answer = scanner.nextLine();
                
                manager.registerUser(name, phone, password, question, answer);
            } 
            else if (choice == 1) {
                // LOGIN
                System.out.print("\nPhone: ");
                String phone = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                User activeUser = manager.loginUser(phone, password);

                if (activeUser != null) {
                    userMenu(scanner, manager, activeUser);
                }
            }
            else if (choice == 3) {
                // FORGOT PASSWORD SCREEN
                System.out.println("\n--- RESET PASSWORD ---");
                System.out.print("Phone Number: ");
                String phone = scanner.nextLine();
                
                User user = manager.findUser(phone);
                
                if (user != null) {
                    System.out.println("Security Question: " + user.getSecurityQuestion());
                    System.out.print("Your Answer: ");
                    String answer = scanner.nextLine();

                    System.out.print("New Password: ");
                    String newPassword = scanner.nextLine();
                    
                    manager.forgotPassword(phone, answer, newPassword);
                } else {
                    System.out.println("❌ ERROR: This phone number is not registered.");
                }

            }
            else if (choice == 9) {
                // ADMIN LOGIN
                System.out.print("Admin Password: ");
                if (scanner.nextLine().equals("admin123")) {
                    adminMenu(scanner, manager);
                } else {
                    System.out.println("❌ Incorrect Password!");
                }
            }
            else {
                System.out.println("Invalid Choice!");
            }
        }
    }

    public static void userMenu(Scanner scanner, SystemmManager manager, User user) {
        while (true) {
            System.out.println("\n--- WELCOME " + user.getFullName().toUpperCase() + " ---");
            System.out.println("Current Balance: " + user.getBalance() + " TL");
            System.out.println("----------------------------");
            System.out.println("1. Book a Reservation");
            System.out.println("2. View Booked Slots");
            System.out.println("3. Top-up Balance");
            System.out.println("4. Cancel Reservation (Refund)");
            System.out.println("5. View My Reservations");
            System.out.println("0. Logout");
            System.out.print("Select Operation: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 0) break; 
            else if (choice == 1) {
                System.out.print("Date (e.g., 17.11.2025): ");
                String date = scanner.nextLine();
                System.out.print("Time Slot (e.g., 20:00-21:00): ");
                String timeSlot = scanner.nextLine();
                manager.createReservation(user, date, timeSlot, 500.0);
            } 
            else if (choice == 2) {
                manager.showBookedSlots();
            } 
            else if (choice == 3) {
                System.out.print("Top-up Amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                manager.increaseUserBalance(user, amount);
            }
            else if (choice == 4) {
                System.out.println("\n--- CANCEL RESERVATION ---");
                System.out.print("Date to Cancel (e.g., 17.11.2025): ");
                String date = scanner.nextLine();
                System.out.print("Time Slot to Cancel (e.g., 20:00-21:00): ");
                String timeSlot = scanner.nextLine();
                
                manager.cancelReservation(user, date, timeSlot);
            }
            else if (choice == 5) {
                manager.showUserReservations(user);
            }
            else {
                System.out.println("Invalid Choice!");
            }
        }
    }
    
    // ADMIN MENU
    public static void adminMenu(Scanner scanner, SystemmManager manager) {
        while (true) {
            System.out.println("\n--- ADMIN AUTHORIZATION ---");
            System.out.println("1. System Check (Reports)");
            System.out.println("2. Delete Reservation");
            System.out.println("0. Logout (Main Menu)");
            System.out.print("Select Operation: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) break;
            else if (choice == 1) {
                manager.getSystemReport();
            } 
            else if (choice == 2) {
                manager.showBookedSlots(); 
                System.out.println("--- DELETE RESERVATION SCREEN ---");
                System.out.print("Date to Delete (e.g., 17.11.2025): ");
                String date = scanner.nextLine();
                System.out.print("Time Slot to Delete (e.g., 20:00-21:00): ");
                String timeSlot = scanner.nextLine();
                
                manager.adminDeleteReservation(date, timeSlot);
            }
            else {
                System.out.println("Invalid Choice!");
            }
        }
    }
}