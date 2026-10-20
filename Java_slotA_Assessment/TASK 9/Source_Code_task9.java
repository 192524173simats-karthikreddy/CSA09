
package karthik;

interface Chargeable {
    double chargingFee(int kWh);

    default void showChargingInfo() {
        System.out.println("Electric vehicle charging");
    }

    static double getRate() {
        return 12.0;
    }
}

abstract class Vehicle {
    private final String regNo;

    Vehicle(String regNo) {
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    abstract double ratePerHour();

    public double calculateFee(int minutes) {
        int hours = (minutes + 59) / 60;
        return hours * ratePerHour();
    }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    double ratePerHour() {
        return 10.0;
    }
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo);
    }

    @Override
    double ratePerHour() {
        return 30.0;
    }
}

class ElectricCar extends Car implements Chargeable {
    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double chargingFee(int kWh) {
        return kWh * Chargeable.getRate();
    }

    @Override
    public void showChargingInfo() {
        System.out.println("Electric vehicle charging");
    }
}

public class task9 {
    public static void main(String[] args) {
        Vehicle v1 = new TwoWheeler("TN01A1");
        Vehicle v2 = new Car("TN02B2");
        ElectricCar ev = new ElectricCar("TN03C3");

        System.out.println("TwoWheeler fee: Rs " +
                v1.calculateFee(130));

        System.out.println("Car fee: Rs " +
                v2.calculateFee(130));

        double total = ev.calculateFee(130) + ev.chargingFee(10);
        System.out.println("ElectricCar total: Rs " + total);

        ev.showChargingInfo();

        System.out.println("Charging rate: Rs " +
                Chargeable.getRate() + " per kWh");
    }
}
