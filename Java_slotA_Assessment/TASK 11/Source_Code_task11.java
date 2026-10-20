
package karthik;

abstract class Vehicle {
    private final String regNo;

    public Vehicle(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Registration number cannot be empty");
        }
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public abstract double ratePerHour();

    public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException(
                "Parking duration cannot be negative");
        }

        int hours = (minutes + 59) / 60;
        return hours * ratePerHour();
    }
}

class TwoWheeler extends Vehicle {
    public TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    public double ratePerHour() {
        return 10.0;
    }
}

class Car extends Vehicle {
    public Car(String regNo) {
        super(regNo);
    }

    @Override
    public double ratePerHour() {
        return 30.0;
    }
}

class ElectricCar extends Vehicle {
    public ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double ratePerHour() {
        return 40.0;
    }
}

class ParkingTicket {
    private final Vehicle vehicle;
    private final int slotNumber;

    public ParkingTicket(Vehicle vehicle, int slotNumber) {
        this.vehicle = vehicle;
        this.slotNumber = slotNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}

class ParkingFacility {
    private final ParkingTicket[] activeTickets;
    private double dailyCollection;

    public ParkingFacility(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be positive");
        }
        activeTickets = new ParkingTicket[capacity];
        dailyCollection = 0.0;
    }

    private Vehicle createVehicle(String type, String regNo) {
        if (type == null) {
            throw new IllegalArgumentException(
                "Unknown vehicle type");
        }

        switch (type.toUpperCase()) {
            case "BIKE":
                return new TwoWheeler(regNo);
            case "CAR":
                return new Car(regNo);
            case "EV":
                return new ElectricCar(regNo);
            default:
                throw new IllegalArgumentException(
                    "Unknown vehicle type: " + type);
        }
    }

    private int findTicket(String regNo) {
        for (int i = 0; i < activeTickets.length; i++) {
            if (activeTickets[i] != null &&
                activeTickets[i].getVehicle().getRegNo()
                    .equalsIgnoreCase(regNo)) {
                return i;
            }
        }
        return -1;
    }

    public void enter(String type, String regNo, int slotNumber) {
        if (slotNumber < 1 ||
            slotNumber > activeTickets.length) {
            throw new IllegalArgumentException(
                "Invalid slot number: " + slotNumber);
        }

        if (findTicket(regNo) != -1) {
            throw new IllegalArgumentException(
                "Duplicate registration: " + regNo);
        }

        Vehicle vehicle = createVehicle(type, regNo);
        int index = slotNumber - 1;

        if (activeTickets[index] != null) {
            throw new IllegalStateException(
                "Slot " + slotNumber + " is occupied");
        }

        activeTickets[index] =
            new ParkingTicket(vehicle, slotNumber);

        System.out.println(regNo + " entered slot " + slotNumber);
    }

    public double exit(String regNo, int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException(
                "Parking duration cannot be negative");
        }

        int index = findTicket(regNo);

        if (index == -1) {
            throw new IllegalArgumentException(
                "No active ticket for: " + regNo);
        }

        ParkingTicket ticket = activeTickets[index];
        Vehicle vehicle = ticket.getVehicle();
        double fee = vehicle.calculateFee(minutes);

        dailyCollection += fee;
        activeTickets[index] = null;

        System.out.println(
            regNo + " exited slot " + ticket.getSlotNumber()
            + ", duration = " + minutes + " minutes, fee = Rs "
            + fee
        );

        return fee;
    }

    public double getDailyCollection() {
        return dailyCollection;
    }

    public int getFreeSlots() {
        int count = 0;

        for (ParkingTicket ticket : activeTickets) {
            if (ticket == null) {
                count++;
            }
        }

        return count;
    }
}

public class task11 {
    public static void main(String[] args) {
        ParkingFacility parking = new ParkingFacility(2);

        System.out.println("=== Normal and Boundary Tests ===");

        // Fill both slots: the second vehicle uses the last free slot.
        parking.enter("BIKE", "TN01A1", 1);
        parking.enter("CAR", "TN02B2", 2);
        System.out.println("Free slots: " + parking.getFreeSlots());

        // Invalid case: duplicate registration.
        try {
            parking.enter("EV", "TN01A1", 1);
        } catch (Exception e) {
            System.out.println("PASS - " + e.getMessage());
        }

        // Invalid case: unknown vehicle type.
        try {
            parking.enter("BUS", "TN04D4", 1);
        } catch (Exception e) {
            System.out.println("PASS - " + e.getMessage());
        }

        // Invalid case: invalid slot number.
        try {
            parking.enter("CAR", "TN05E5", 0);
        } catch (Exception e) {
            System.out.println("PASS - " + e.getMessage());
        }

        // Boundary case: zero-minute stay.
        parking.exit("TN01A1", 0);

        // The released slot can now be reused.
        parking.enter("EV", "TN03C3", 1);

        // Fee-slab boundary: exactly 60 minutes.
        parking.exit("TN02B2", 60);
        parking.exit("TN03C3", 60);

        System.out.println("Daily collection: Rs "
                + parking.getDailyCollection());
        System.out.println("Free slots at end: "
                + parking.getFreeSlots());
    }
}
