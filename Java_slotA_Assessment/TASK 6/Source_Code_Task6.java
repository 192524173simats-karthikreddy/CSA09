
class Vehicle {
    protected String regNo;
    protected double rate;

    public Vehicle(String regNo, double rate) {
        this.regNo = regNo;
        this.rate = rate;
        System.out.println("Vehicle constructor");
    }

    public double calculateFee(int hours) {
        return hours * rate;
    }

    public void display() {
        System.out.println("Registration Number: " + regNo);
        System.out.println("Hourly Rate: Rs " + rate);
    }
}

class TwoWheeler extends Vehicle {

    public TwoWheeler(String regNo) {
        super(regNo, 10);
        System.out.println("TwoWheeler constructor");
    }
}

class Car extends Vehicle {

    public Car(String regNo) {
        super(regNo, 30);
        System.out.println("Car constructor");
    }
}

class ElectricCar extends Car {

    public ElectricCar(String regNo) {
        super(regNo);
        System.out.println("ElectricCar constructor");
    }
}

class Student {
    protected String name;

    public Student(String name) {
        this.name = name;
    }
}

class Staff extends Student {

    public Staff(String name) {
        super(name);
    }
}

class Visitor extends Student {

    public Visitor(String name) {
        super(name);
    }
}

public class Task6 {
    public static void main(String[] args) {

        System.out.println("Creating TwoWheeler:");
        TwoWheeler bike = new TwoWheeler("TN01AB1234");
        bike.display();
        System.out.println("Fee for 2 hours: Rs "
                + bike.calculateFee(2));

        System.out.println();

        System.out.println("Creating Car:");
        Car car = new Car("TN02CD5678");
        car.display();
        System.out.println("Fee for 2 hours: Rs "
                + car.calculateFee(2));

        System.out.println();

        System.out.println("Creating ElectricCar:");
        ElectricCar ev = new ElectricCar("TN03EF9012");
        ev.display();
        System.out.println("Fee for 2 hours: Rs "
                + ev.calculateFee(2));

        System.out.println();

        System.out.println("Creating Staff:");
        Staff staff = new Staff("Ravi");
        System.out.println("Staff name: " + staff.name);

        System.out.println("Creating Visitor:");
        Visitor visitor = new Visitor("Arun");
        System.out.println("Visitor name: " + visitor.name);
    }
}
