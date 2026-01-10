

public class Reservation {

    private String date;        
    private String timeSlot;  
    private String userName; 
    private double price;        

    public Reservation(String date, String timeSlot, String userName, double price) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.userName = userName;
        this.price = price;
    }

    // --- Getters ---
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getUserName() { return userName; }
    public double getPrice() { return price; }
    
    @Override
    public String toString() {
        return "DATE: " + date + " | TIME: " + timeSlot + 
               " | CUSTOMER: " + userName + " | PRICE: " + price + " TL";
    }
}