
class ParkingFacility {
    private String facilityName;
    private int occupiedSlots;

   
    private static int totalTickets = 1000;

    static {
        System.out.println("Parking System Initialized");
    }

    public ParkingFacility(String facilityName) {
        this.facilityName = facilityName;
        this.occupiedSlots = 0;
    }

    public static int getTotalTickets() {
        return totalTickets - 1000;
    }

    public void parkVehicle(ParkingTicket ticket) {
        occupiedSlots++;

        System.out.println(
                ticket.getTicketNo() + " issued at " + facilityName);
    }

    public void showOccupancy() {
        System.out.println(
                facilityName + " occupied slots: " + occupiedSlots);
    }


    public static int generateTicketNo() {
        return totalTickets++;
    }
}

class ParkingTicket {
    private int ticketNo;
    private String vehicleNo;

    public ParkingTicket(String vehicleNo) {
        this.ticketNo = ParkingFacility.generateTicketNo();
        this.vehicleNo = vehicleNo;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }
}

public class task5 {
    public static void main(String[] args) {

        ParkingFacility north = new ParkingFacility("North Gate");
        ParkingFacility south = new ParkingFacility("South Gate");

        ParkingTicket t1 = new ParkingTicket("TN01AB1234");
        north.parkVehicle(t1);

        ParkingTicket t2 = new ParkingTicket("TN02CD5678");
        south.parkVehicle(t2);

        ParkingTicket t3 = new ParkingTicket("TN03EF9012");
        north.parkVehicle(t3);

        System.out.println();

        north.showOccupancy();
        south.showOccupancy();

        System.out.println(
                "Total tickets issued: " +
                ParkingFacility.getTotalTickets());
    }
}
