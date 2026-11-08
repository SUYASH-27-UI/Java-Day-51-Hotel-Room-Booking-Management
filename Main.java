import java.util.*;
import java.io.*;

class Booking {

    int bookingId;
    int roomNumber;
    String customerName;
    int days;

    Booking(int bookingId, int roomNumber,
            String customerName, int days) {

        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.customerName = customerName;
        this.days = days;
    }

    void display() {

        System.out.println("\nBooking ID: " + bookingId);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Customer: " + customerName);
        System.out.println("Days: " + days);
    }
}

public class Main {

    static ArrayList<Booking> bookings = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "bookings.txt";

    static final int TOTAL_ROOMS = 10;

    static final int ROOM_PRICE = 1500;

    static void loadBookings() {

        try (BufferedReader br =
                new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", 4);

                if (data.length == 4) {

                    bookings.add(new Booking(
                        Integer.parseInt(data[0]),
                        Integer.parseInt(data[1]),
                        data[2],
                        Integer.parseInt(data[3])
                    ));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("New booking database created.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading booking data.");
        }
    }

    static void saveBookings() {

        try (BufferedWriter bw =
                new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Booking b : bookings) {

                bw.write(
                    b.bookingId + "|" +
                    b.roomNumber + "|" +
                    b.customerName + "|" +
                    b.days
                );

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    static boolean isRoomAvailable(int room) {

        for (Booking b : bookings) {

            if (b.roomNumber == room) {
                return false;
            }
        }

        return true;
    }

    static void displayRooms() {

        System.out.println("\n===== ROOM AVAILABILITY =====");

        for (int room = 1; room <= TOTAL_ROOMS; room++) {

            String status = isRoomAvailable(room)
                    ? "Available" : "Booked";

            System.out.println(
                "Room " + room + " : " + status
            );
        }
    }

    static void bookRoom() {

        displayRooms();

        System.out.print("\nEnter Room Number: ");

        int room = Integer.parseInt(sc.nextLine());

        if (room < 1 || room > TOTAL_ROOMS) {
            System.out.println("Invalid room number!");
            return;
        }

        if (!isRoomAvailable(room)) {
            System.out.println("Room is already booked!");
            return;
        }

        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        if (name.isBlank() || name.contains("|")) {
            System.out.println("Invalid customer name!");
            return;
        }

        System.out.print("Enter Number of Days: ");
        int days = Integer.parseInt(sc.nextLine());

        if (days <= 0) {
            System.out.println("Invalid number of days!");
            return;
        }

        int id = 1001;

        for (Booking b : bookings) {
            if (b.bookingId >= id) {
                id = b.bookingId + 1;
            }
        }

        Booking booking = new Booking(
            id, room, name, days
        );

        bookings.add(booking);

        saveBookings();

        int bill = days * ROOM_PRICE;

        System.out.println("\nBooking successful!");
        System.out.println("Booking ID: " + id);
        System.out.println("Total Bill: Rs. " + bill);
    }

    static void displayBookings() {

        if (bookings.isEmpty()) {
            System.out.println("No bookings available!");
            return;
        }

        System.out.println("\n===== ALL BOOKINGS =====");

        for (Booking b : bookings) {
            b.display();
        }
    }

    static void searchBooking() {

        System.out.print("Enter Booking ID: ");

        int id = Integer.parseInt(sc.nextLine());

        for (Booking b : bookings) {

            if (b.bookingId == id) {
                b.display();
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    static void cancelBooking() {

        System.out.print("Enter Booking ID: ");

        int id = Integer.parseInt(sc.nextLine());

        boolean removed = bookings.removeIf(
            b -> b.bookingId == id
        );

        if (removed) {
            saveBookings();
            System.out.println("Booking cancelled successfully!");
        } else {
            System.out.println("Booking not found!");
        }
    }

    public static void main(String[] args) {

        loadBookings();

        int choice = 0;

        do {

            System.out.println("\n===== HOTEL BOOKING SYSTEM =====");
            System.out.println("1. Display Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Display Bookings");
            System.out.println("4. Search Booking");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");

            System.out.print("Enter Choice: ");

            try {

                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        displayRooms();
                        break;

                    case 2:
                        bookRoom();
                        break;

                    case 3:
                        displayBookings();
                        break;

                    case 4:
                        searchBooking();
                        break;

                    case 5:
                        cancelBooking();
                        break;

                    case 6:
                        System.out.println("Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter valid numbers!");
            }

        } while (choice != 6);

        sc.close();
    }
}
