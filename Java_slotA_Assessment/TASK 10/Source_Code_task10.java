
package karthik;

class ParkingSlot {
    private int slotNumber;
    private Integer duration;

    public ParkingSlot(int slotNumber, Integer duration) {
        setSlotNumber(slotNumber);
        setDuration(duration);
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        if (slotNumber <= 0) {
            throw new IllegalArgumentException(
                "Slot number must be positive");
        }
        this.slotNumber = slotNumber;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        if (duration == null || duration < 0) {
            throw new IllegalArgumentException(
                "Duration cannot be negative or null");
        }
        this.duration = duration;
    }
}

public class task10 {
    public static void main(String[] args) {
        ParkingSlot slot = new ParkingSlot(5, 120);

        System.out.println("Slot Number: " + slot.getSlotNumber());
        System.out.println("Duration: " + slot.getDuration());

        slot.setDuration(180);
        System.out.println("Updated Duration: " + slot.getDuration());

        // Wrapper class and autoboxing
        Integer a = 100;
        int b = a;
        System.out.println("Autoboxing value: " + a);
        System.out.println("Unboxed value: " + b);

        // Validation example
        try {
            slot.setDuration(-10);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
