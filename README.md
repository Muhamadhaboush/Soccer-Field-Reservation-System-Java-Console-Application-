# Soccer Field Reservation System (Console-Based)

A console-based soccer field reservation system built with Java, focusing on Object-Oriented Programming (OOP) principles. Instead of a traditional database, it uses file I/O operations (`.txt` files) for data persistence.

## Features

- User Management: Registration, login, and a "Forgot Password" module (secured with a personal security question).
- Wallet/Balance System:Users can top up their account balance. Reservations have a fixed price (500 TL) deducted directly from this balance.
- Reservation Operations: Date and time-based booking. The system prevents double-booking for the same time slot.
- Smart Cancellation & Refunds: If a cancellation is made more than 12 hours before the match, the system automatically refunds the balance. If it's less than 12 hours, the reservation is canceled without a refund. Past reservations cannot be canceled.
- Admin Panel: Allows admins to view system reports (total members, total reservations, total revenue) and manually delete specific reservations when necessary.
- Data Persistence: User credentials and reservation data are safely stored in `users.txt` and `reservations.txt` respectively, ensuring data is not lost when the application closes.

## Project Structure

- Main.java: The entry point that handles the console UI menus and user flow.
- SystemmManager.java: The core logic of the system. Handles file I/O, registration, cancellation algorithms, and validation checks.
- User.java: The model class representing a user, handling their balance and payment operations.
- Reservation.java: The model class storing reservation details (date, time slot, price, booked by).

## Installation & Execution
