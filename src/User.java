

public class User {

    private String fullName;
    private String phone;
    private String password;
    private double balance;  
    private String securityQuestion;
    private String securityAnswer;

    public User(String fullName, String phone, String password, String securityQuestion, String securityAnswer) {
        this.fullName = fullName;
        this.phone = phone;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.balance = 0.0; 
    }

    // --- Getters ---
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }
    public String getPassword() { return password; }
    public String getSecurityQuestion() { return securityQuestion; }
    public String getSecurityAnswer() { return securityAnswer; }

    // --- Business Methods ---
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean checkSecurityAnswer(String answer) {
        return this.securityAnswer.equalsIgnoreCase(answer);
    }

    public void increaseBalance(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Dear " + fullName + ", " + amount + " TL has been loaded into your account.");
        }
    }

    public boolean makePayment(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true; 
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Customer: " + fullName + " | Phone: " + phone + " | Balance: " + balance + " TL";
    }
}