
class Vehicle {
    private String regNo;
    private String type;
    private long entryTime;

    public Vehicle(String regNo, String type, long entryTime) {
        this.regNo = regNo;
        this.type = type;
        this.entryTime = entryTime;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getType() {
        return type;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void display() {
        System.out.println("Registration Number: " + regNo);
        System.out.println("Vehicle Type: " + type);
        System.out.println("Entry Time: " + entryTime);
    }
}

class ParkingSlot {
    private int floorNo;
    private int slotNo;
    private Vehicle vehicle;

    public ParkingSlot(int floorNo, int slotNo) {
        this.floorNo = floorNo;
        this.slotNo = slotNo;
        this.vehicle = null;
    }

    public boolean isFree() {
        return vehicle == null;
    }

    public boolean parkVehicle(Vehicle v) {
        if (isFree()) {
            vehicle = v;
            return true;
        }
        return false;
    }

    public Vehicle removeVehicle() {
        if (isFree()) {
            return null;
        }

        Vehicle removed = vehicle;
        vehicle = null;
        return removed;
    }

    public void display() {
        System.out.println("Floor: " + floorNo);
        System.out.println("Slot: " + slotNo);

        if (isFree()) {
            System.out.println("Status: Free");
        } else {
            System.out.println("Status: Occupied");
        }
    }
}

class ParkingTicket {
    private static int nextTicketNo = 1000;

    private int ticketNo;
    private Vehicle vehicle;
    private ParkingSlot slot;
    private long entryTime;

    public ParkingTicket(Vehicle vehicle, ParkingSlot slot) {
        this.ticketNo = nextTicketNo++;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = vehicle.getEntryTime();
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void displayTicket() {
        System.out.println("Ticket Number: " + ticketNo);
        System.out.println("Vehicle Number: " + vehicle.getRegNo());
        System.out.println("Vehicle Type: " + vehicle.getType());
        System.out.println("Entry Time: " + entryTime);
        slot.display();
    }
}

public class task1 {
    public static void main(String[] args) {

        Vehicle v = new Vehicle(
                "TN01AB1234",
                "Car",
                System.currentTimeMillis()
        );

        ParkingSlot slot = new ParkingSlot(1, 1);

        if (slot.parkVehicle(v)) {
            ParkingTicket ticket = new ParkingTicket(v, slot);

            System.out.println("Vehicle parked successfully.");
            ticket.displayTicket();
        } else {
            System.out.println("Parking slot is occupied.");
        }

        System.out.println();

        // Test whether the occupied slot rejects another vehicle
        Vehicle v2 = new Vehicle(
                "TN02CD5678",
                "TwoWheeler",
                System.currentTimeMillis()
        );

        if (slot.parkVehicle(v2)) {
            System.out.println("Second vehicle parked successfully.");
        } else {
            System.out.println("Slot is already occupied.");
        }

        // Release the first vehicle
        Vehicle removed = slot.removeVehicle();

        if (removed != null) {
            System.out.println("Vehicle exited: " + removed.getRegNo());
        }

        slot.display();
    }
}
