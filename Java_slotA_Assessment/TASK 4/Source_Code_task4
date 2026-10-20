import java.time.LocalDateTime;

class ParkingTicket {
    private static int counter = 1000;

    private int ticketNumber;
    private String vehicleNo;
    private LocalDateTime entryTime;

    ParkingTicket() {
        this("Not Assigned", LocalDateTime.now());
    }

    ParkingTicket(String vehicleNo) {
        this(vehicleNo, LocalDateTime.now());
    }

    ParkingTicket(String vehicleNo, LocalDateTime entryTime) {
        ticketNumber = ++counter;
        this.vehicleNo = vehicleNo;
        this.entryTime = entryTime;
    }

    void display() {
        System.out.println("Ticket Number: " + ticketNumber);
        System.out.println("Vehicle Number: " + vehicleNo);
        System.out.println("Entry Time: " + entryTime);
        System.out.println();
    }
}

public class Task4 {
    public static void main(String[] args) {
        ParkingTicket t1 = new ParkingTicket();
        ParkingTicket t2 = new ParkingTicket("TN01AB1111");
        ParkingTicket t3 = new ParkingTicket(
                "TN02CD2222",
                LocalDateTime.of(2026, 10, 20, 9, 0));

        t1.display();
        t2.display();
        t3.display();
    }
}