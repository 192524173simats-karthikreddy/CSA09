
package karthik;

abstract class Vehicle {
    private final String regNo;

    public Vehicle(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid registration number");
        }
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    protected int getHours(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Invalid duration");
        }
        return (minutes + 59) / 60;
    }

    public abstract double calculateFee(int minutes, int entryHour);
}

class TwoWheeler extends Vehicle {
    public TwoWheeler(String regNo) {
        super(regNo);
    }

    public double calculateFee(int minutes, int entryHour) {
        return getHours(minutes) * 10.0;
    }
}

class Car extends Vehicle {
    public Car(String regNo) {
        super(regNo);
    }

    public double calculateFee(int minutes, int entryHour) {
        return getHours(minutes) * 30.0;
    }
}

class ElectricCar extends Vehicle {
    public ElectricCar(String regNo) {
        super(regNo);
    }

    public double calculateFee(int minutes, int entryHour) {
        return getHours(minutes) * 40.0;
    }
}

class EBike extends Vehicle {
    public EBike(String regNo) {
        super(regNo);
    }

    public double calculateFee(int minutes, int entryHour) {
        getHours(minutes);
        return 15.0;
    }
}

class DeliveryVan extends Vehicle {
    public DeliveryVan(String regNo) {
        super(regNo);
    }

    public double calculateFee(int minutes, int entryHour) {
        if (entryHour < 0 || entryHour > 23) {
            throw new IllegalArgumentException("Invalid entry hour");
        }

        double rate = 30.0;

        if (entryHour >= 9 && entryHour < 17) {
            rate = 60.0;
        }

        return getHours(minutes) * rate;
    }
}

class OneDayPass {
    private final String visitorId;
    private static final double FEE = 100.0;

    public OneDayPass(String visitorId) {
        if (visitorId == null || visitorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid visitor ID");
        }
        this.visitorId = visitorId;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public double getFee() {
        return FEE;
    }
}

class ParkingTicket {
    private final String id;
    private final Vehicle vehicle;
    private final OneDayPass pass;
    private final int floor;
    private final int slot;

    public ParkingTicket(Vehicle vehicle, int floor, int slot) {
        this.vehicle = vehicle;
        this.pass = null;
        this.id = vehicle.getRegNo();
        this.floor = floor;
        this.slot = slot;
    }

    public ParkingTicket(OneDayPass pass, int floor, int slot) {
        this.vehicle = null;
        this.pass = pass;
        this.id = pass.getVisitorId();
        this.floor = floor;
        this.slot = slot;
    }

    public String getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }

    public int getSlot() {
        return slot;
    }

    public double calculateFee(int minutes, int entryHour) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Invalid duration");
        }

        if (pass != null) {
            return pass.getFee();
        }

        return vehicle.calculateFee(minutes, entryHour);
    }
}

class ParkingFacility {
    private final ParkingTicket[][] slots;
    private double collection = 0.0;

    public ParkingFacility(int[] capacities) {
        if (capacities == null || capacities.length == 0) {
            throw new IllegalArgumentException("Invalid floor capacities");
        }

        slots = new ParkingTicket[capacities.length][];

        for (int i = 0; i < capacities.length; i++) {
            if (capacities[i] <= 0) {
                throw new IllegalArgumentException("Invalid slot capacity");
            }

            slots[i] = new ParkingTicket[capacities[i]];
        }
    }

    private void validateSlot(int floor, int slot) {
        if (floor < 1 || floor > slots.length) {
            throw new IllegalArgumentException("Invalid floor number");
        }

        if (slot < 1 || slot > slots[floor - 1].length) {
            throw new IllegalArgumentException("Invalid slot number");
        }
    }

    private ParkingTicket findTicket(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        for (ParkingTicket[] floor : slots) {
            for (ParkingTicket ticket : floor) {
                if (ticket != null &&
                    ticket.getId().equalsIgnoreCase(id)) {
                    return ticket;
                }
            }
        }

        return null;
    }

    private void checkEntry(String id, int floor, int slot) {
        validateSlot(floor, slot);

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if (findTicket(id) != null) {
            throw new IllegalArgumentException("Duplicate ID: " + id);
        }

        if (slots[floor - 1][slot - 1] != null) {
            throw new IllegalStateException("Slot is occupied");
        }
    }

    public void enter(String type, String id, int floor, int slot) {
        checkEntry(id, floor, slot);

        if (type == null) {
            throw new IllegalArgumentException("Unknown vehicle type");
        }

        Vehicle vehicle;

        switch (type.toUpperCase()) {
            case "BIKE":
                vehicle = new TwoWheeler(id);
                break;
            case "CAR":
                vehicle = new Car(id);
                break;
            case "EV":
                vehicle = new ElectricCar(id);
                break;
            case "EBIKE":
                vehicle = new EBike(id);
                break;
            case "VAN":
                vehicle = new DeliveryVan(id);
                break;
            default:
                throw new IllegalArgumentException(
                    "Unknown vehicle type: " + type);
        }

        slots[floor - 1][slot - 1] =
            new ParkingTicket(vehicle, floor, slot);

        System.out.println(type.toUpperCase() + " " + id
            + " entered floor " + floor + ", slot " + slot);
    }

    public void buyOneDayPass(String visitorId, int floor, int slot) {
        checkEntry(visitorId, floor, slot);

        OneDayPass pass = new OneDayPass(visitorId);

        slots[floor - 1][slot - 1] =
            new ParkingTicket(pass, floor, slot);

        System.out.println("One-day pass issued to " + visitorId);
    }

    public double exit(String id, int minutes, int entryHour) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Invalid duration");
        }

        if (entryHour < 0 || entryHour > 23) {
            throw new IllegalArgumentException("Invalid entry hour");
        }

        ParkingTicket ticket = findTicket(id);

        if (ticket == null) {
            throw new IllegalArgumentException("No active ticket: " + id);
        }

        double fee = ticket.calculateFee(minutes, entryHour);

        slots[ticket.getFloor() - 1][ticket.getSlot() - 1] = null;
        collection += fee;

        System.out.println(id + " exited; fee = Rs " + fee);
        return fee;
    }

    public double getCollection() {
        return collection;
    }

    public int getFloorCount() {
        return slots.length;
    }

    public int getSlotCount(int floor) {
        if (floor < 1 || floor > slots.length) {
            throw new IllegalArgumentException("Invalid floor");
        }

        return slots[floor - 1].length;
    }
}

public class task12 {
    public static void main(String[] args) {
        int[] capacities = {2, 3, 2, 4, 1};
        ParkingFacility parking = new ParkingFacility(capacities);

        System.out.println("Floors: " + parking.getFloorCount());

        for (int f = 1; f <= parking.getFloorCount(); f++) {
            System.out.println("Floor " + f + " slots: "
                + parking.getSlotCount(f));
        }

        System.out.println("\n=== New Requirement Tests ===");

        parking.enter("EBIKE", "EB101", 1, 1);
        parking.exit("EB101", 30, 10);

        parking.enter("VAN", "DV201", 2, 1);
        parking.exit("DV201", 120, 10);

        parking.enter("VAN", "DV202", 2, 2);
        parking.exit("DV202", 120, 18);

        parking.buyOneDayPass("VIS301", 5, 1);
        parking.exit("VIS301", 480, 9);

        System.out.println("\nTotal collection: Rs "
            + parking.getCollection());
    }
}
