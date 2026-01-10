

import java.util.ArrayList;
import java.io.*; 
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter; 
import java.time.Duration; 

public class SystemmManager {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    private final String USER_FILE = "users.txt";
    private final String RES_FILE = "reservations.txt";
    private final double FIXED_FEE = 500.0;

    public SystemmManager() {
        loadData(); 
    }

    // USER ACCOUNT MANAGEMENT
    
    public void registerUser(String fullName, String phone, String password, String question, String answer) {
        if (phone.length() < 10) {
            System.out.println("❌ ERROR: Phone number incomplete! Please enter 10 digits.");
            return;
        }
        if (findUser(phone) != null) { 
            System.out.println("❌ ERROR: This phone number is already registered!");
            return;
        }
        if (isUserNameTaken(fullName)) {
            System.out.println("❌ ERROR: This Full Name is already taken. Please choose another name.");
            return;
        }

        User newUser = new User(fullName, phone, password, question, answer);
        users.add(newUser);
        System.out.println("✅ Registration Successful: " + fullName + ". Don't forget your security answer!");
        saveData(); 
    }

    public User findUser(String phone) {
        for (User u : users) {
            if (u.getPhone().equals(phone)) return u;
        }
        return null;
    }

    public boolean forgotPassword(String phone, String inputAnswer, String newPassword) {
        User user = findUser(phone);

        if (user == null) {
            System.out.println("❌ ERROR: This phone number is not registered.");
            return false;
        }

        if (user.checkSecurityAnswer(inputAnswer)) {
            user.setPassword(newPassword);
            saveData(); 
            System.out.println("✅ Password updated successfully!");
            return true;
        } else {
            System.out.println("❌ ERROR: Security answer is incorrect.");
            return false;
        }
    }
    
    public User loginUser(String phone, String password) {
        if (phone.length() < 7) { 
            System.out.println("❌ ERROR: Please enter a valid phone number.");
            return null;
        }
        
        User user = findUser(phone);
        if (user != null && user.checkPassword(password)) {
            System.out.println("Login Successful. Welcome " + user.getFullName());
            return user;
        } else {
            System.out.println("❌ Incorrect phone number or password.");
            return null;
        }
    }
    
    public void increaseUserBalance(User user, double amount) {
        user.increaseBalance(amount);
        saveData(); 
    }

    // RESERVATION MANAGEMENT
    
    public void createReservation(User customer, String date, String timeSlot, double fee) {
        if (date == null || date.trim().isEmpty() || date.equals("0") ||
            timeSlot == null || timeSlot.trim().isEmpty() || timeSlot.equals("0")) {
            System.out.println("❌ ERROR: Please enter a valid date and time.");
            return;
        }
        
        if (isSlotBooked(date, timeSlot)) {
            System.out.println("⚠️ This time slot is already booked!");
            return;
        }
        if (customer.makePayment(fee)) {
            Reservation newReservation = new Reservation(date, timeSlot, customer.getFullName(), fee);
            reservations.add(newReservation);
            System.out.println("✅ RESERVATION APPROVED!");
            saveData(); 
        } else {
            System.out.println("❌ Insufficient Balance!");
        }
    }
    
    public void cancelReservation(User user, String date, String timeSlot) {
         if (date.equals("0") || timeSlot.equals("0")) {
            System.out.println("❌ ERROR: Please enter a valid date and time.");
            return;
        }
        
        Reservation reservationToDelete = null;
        
        String cleanDate = date.replace('.', '-'); 
        String cleanTime = timeSlot.replace('.', ':').replace(' ', ' ');
        
        for(Reservation r : reservations) {
            if(r.getDate().equals(cleanDate) && r.getTimeSlot().equals(cleanTime)) {
                if(r.getUserName().equals(user.getFullName())) {
                    reservationToDelete = r;
                    break;
                } else {
                    System.out.println("⛔ ERROR: You cannot cancel someone else's reservation!");
                    return;
                }
            }
        }
        
        if(reservationToDelete != null) {
            try {
                // Preparation for time calculation
                String startTime = timeSlot.split("-")[0].trim().replace('.', ':'); 
                String matchTimeStr = date.replace('-', '.') + " " + startTime; // Convert to dd.MM.yyyy HH:mm format
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                LocalDateTime matchTime = LocalDateTime.parse(matchTimeStr, formatter);
                LocalDateTime now = LocalDateTime.now(); 

                // RULE 1: PAST DATE CHECK
                if (matchTime.isBefore(now)) {
                    System.out.println("⛔ TRANSACTION DENIED: Past reservations cannot be cancelled!");
                    return; 
                }

                // RULE 2: 12-HOUR CHECK
                long hoursDifference = Duration.between(now, matchTime).toHours();
                System.out.println("ℹ️ Time remaining until match: " + hoursDifference + " hours.");

                if (hoursDifference < 12) {
                    reservations.remove(reservationToDelete);
                    saveData();
                    System.out.println("⚠️ NO REFUND ISSUED (Less than 12 hours remaining).");
                } else {
                    reservations.remove(reservationToDelete);
                    user.increaseBalance(FIXED_FEE); // Refund
                    saveData();
                    System.out.println("✅ Cancellation successful. 500 TL refunded.");
                }

            } catch (Exception e) {
                System.out.println("❌ ERROR: Invalid date format! Please use 'Day.Month.Year Hour:Minute'.");
            }
        } else {
            System.out.println("❌ No reservation found for the specified date under your name.");
        }
    }

    // ADMIN / REPORTING
    
    public void adminDeleteReservation(String date, String timeSlot) {
        String cleanDate = date.replace('.', '-');
        String cleanTime = timeSlot.replace('.', ':').replace(' ', ' ');
        
        Reservation reservationToDelete = null;
        for (Reservation r : reservations) {
            if (r.getDate().equals(cleanDate) && r.getTimeSlot().equals(cleanTime)) {
                reservationToDelete = r;
                break;
            }
        }

        if (reservationToDelete != null) {
            reservations.remove(reservationToDelete);
            saveData();
            System.out.println("✅ ADMIN APPROVAL: Reservation for " + date + " " + timeSlot + " deleted.");
            System.out.println("   Customer: " + reservationToDelete.getUserName());
            System.out.println("⚠️ NOTE: This action does NOT change the customer's balance.");
        } else {
            System.out.println("❌ ERROR: No reservation found for the specified date.");
        }
    }

    public void getSystemReport() {
        System.out.println("\n📊 ADMIN REPORT SCREEN 📊");
        System.out.println("👥 Total Members: " + users.size());
        System.out.println("📅 Reservations Sold: " + reservations.size());
        double revenue = reservations.size() * FIXED_FEE;
        System.out.println("💰 Total Revenue: " + revenue + " TL");
    }

    public void showUserReservations(User user) {
        String fullName = user.getFullName();
        System.out.println("\n--- " + fullName.toUpperCase() + "'S PRIVATE RESERVATIONS ---");
        
        boolean found = false;
        for (Reservation r : reservations) {
            if (r.getUserName().equals(fullName)) {
                System.out.println(r.getDate() + " | " + r.getTimeSlot() + " (Price: " + r.getPrice() + " TL)");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("You have no registered reservations.");
        }
        System.out.println("-------------------------------------");
    }

    public void showBookedSlots() {
        System.out.println("\n--- BOOKED SLOTS ---");
        if (reservations.isEmpty()) System.out.println("The field is currently empty!");
        for (Reservation r : reservations) {
            System.out.println(r.getDate() + " | " + r.getTimeSlot() + " | " + r.getUserName());
        }
    }

    // --- HELPERS / FILE I/O ---

    private boolean isUserNameTaken(String name) {
        for (User u : users) {
            if (u.getFullName().equalsIgnoreCase(name)) { 
                return true;
            }
        }
        return false;
    }

    private boolean isSlotBooked(String date, String timeSlot) {
        for (Reservation r : reservations) {
            if (r.getDate().equals(date) && r.getTimeSlot().equals(timeSlot)) return true;
        }
        return false;
    }

    private void saveData() {
        try {
            // Save Users (6 fields)
            BufferedWriter userWriter = new BufferedWriter(new FileWriter(USER_FILE));
            for (User u : users) {
                userWriter.write(u.getFullName() + ";" + u.getPhone() + ";" + u.getPassword() + ";" + u.getBalance() + ";" + u.getSecurityQuestion() + ";" + u.getSecurityAnswer());
                userWriter.newLine();
            }
            userWriter.close();

            // Save Reservations
            BufferedWriter resWriter = new BufferedWriter(new FileWriter(RES_FILE));
            for (Reservation r : reservations) {
                resWriter.write(r.getDate() + ";" + r.getTimeSlot() + ";" + r.getUserName() + ";" + FIXED_FEE);
                resWriter.newLine();
            }
            resWriter.close();
        } catch (IOException e) { System.out.println("ERROR: " + e.getMessage()); }
    }

    private void loadData() {
        try {
            // Load Users
            File uFile = new File(USER_FILE);
            if (uFile.exists()) {
                BufferedReader userReader = new BufferedReader(new FileReader(USER_FILE));
                String line;
                while ((line = userReader.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length == 6) { 
                        User u = new User(data[0], data[1], data[2], data[4], data[5]); 
                        u.increaseBalance(Double.parseDouble(data[3])); // Load balance
                        users.add(u);
                    }
                }
                userReader.close();
            }
            // Load Reservations
            File rFile = new File(RES_FILE);
            if (rFile.exists()) {
                BufferedReader resReader = new BufferedReader(new FileReader(RES_FILE));
                String line;
                while ((line = resReader.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length == 4) {
                        reservations.add(new Reservation(data[0], data[1], data[2], Double.parseDouble(data[3])));
                    }
                }
                resReader.close();
            }
        } catch (Exception e) { }
    }
}