
import java.util.Objects;

class Vehicle {
    private final String regNo;

    public Vehicle(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Registration number cannot be empty");
        }

        this.regNo = regNo;
    }

    public final String getRegNo() {
        return regNo;
    }

    public double calculateFee(int hours) {
        return 0.0;
    }

    @Override
    public String toString() {
        return "Registration Number: " + regNo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Vehicle)) {
            return false;
        }

        Vehicle other = (Vehicle) obj;
        return regNo.equals(other.regNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }
}

class TwoWheeler extends Vehicle {

    public TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int hours) {
        return hours * 10.0;
    }
}

class Car extends Vehicle {

    public Car(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int hours) {
        return hours * 30.0;
    }
}

class ElectricCar extends Car {

    public ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int hours) {
        return hours * 30.0 + 120.0;
    }
}

public class task7 {
    public static void main(String[] args) {

        Vehicle bike = new TwoWheeler("TN01AB1234");
        Vehicle car = new Car("TN02CD5678");
        Vehicle ev = new ElectricCar("TN03EF9012");

        System.out.println("TwoWheeler fee: Rs "
                + bike.calculateFee(2));

        System.out.println("Car fee: Rs "
                + car.calculateFee(2));

        System.out.println("ElectricCar fee: Rs "
                + ev.calculateFee(2));

        System.out.println();

        System.out.println(bike.toString());

        Vehicle v1 = new Car("TN02CD5678");
        Vehicle v2 = new ElectricCar("TN02CD5678");

        System.out.println("Vehicles equal: " + v1.equals(v2));
        System.out.println("Same hash code: "
                + (v1.hashCode() == v2.hashCode()));
    }
}
