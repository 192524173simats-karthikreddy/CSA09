
package karthik;

interface Chargeable {
    double chargingFee(int kWh);
}

abstract class Vehicle {
    private final String regNo;

    protected Vehicle(String regNo) {
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    protected abstract double ratePerHour();

    // Runtime polymorphism
    public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException(
                "Minutes cannot be negative"
            );
        }

        int hours = minutes / 60;

        if (minutes % 60 != 0) {
            hours++;
        }

        return hours * ratePerHour();
    }

    // Compile-time polymorphism: method overloading
    public double calculateFee(int hours, int extraMinutes) {
        if (hours < 0 || extraMinutes < 0 || extraMinutes >= 60) {
            throw new IllegalArgumentException(
                "Invalid parking duration"
            );
        }

        int totalMinutes = hours * 60 + extraMinutes;
        return calculateFee(totalMinutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Vehicle)) {
            return false;
        }

        Vehicle other = (Vehicle) o;
        return regNo.equals(other.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 10;
    }
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 30;
    }
}

class ElectricCar extends Car implements Chargeable {
    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double chargingFee(int kWh) {
        if (kWh < 0) {
            throw new IllegalArgumentException(
                "Charging units cannot be negative"
            );
        }

        return kWh * 12.0;
    }
}

public class task8 {
    public static void main(String[] args) {

        Vehicle[] parked = {
            new TwoWheeler("TN01A1"),
            new Car("TN02B2"),
            new ElectricCar("TN03C3")
        };

        for (Vehicle v : parked) {
            double fee = v.calculateFee(130);

            if (v instanceof Chargeable) {
                Chargeable c = (Chargeable) v;
                fee += c.chargingFee(10);
            }

            System.out.println(
                v.getClass().getSimpleName() + " "
                + v.getRegNo() + " -> Rs " + fee
            );
        }

        System.out.println(
            new Car("TN02B2").equals(
                new ElectricCar("TN02B2")
            )
        );

        // Demonstrate compile-time polymorphism
        Vehicle car = new Car("TN04D4");

        System.out.println(
            "Overloaded fee: Rs "
            + car.calculateFee(2, 10)
        );
    }
}
