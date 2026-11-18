import java.io.*;
import java.util.*;

class Hostel implements Serializable {
    private static final long serialVersionUID = 1L;

    ArrayList<Room> rooms = new ArrayList<>();
}

class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    ArrayList<Resident> residents = new ArrayList<>();
}

class Resident implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    String registerNo;

    transient String password;
    transient java.sql.Connection connection;

    Resident(String name, String registerNo, String password) {
        this.name = name;
        this.registerNo = registerNo;
        this.password = password;
    }
}

public class A4Serialization {
    public static void main(String[] args) {

        Hostel hostel = new Hostel();

        Room room = new Room();

        Resident resident = new Resident(
            "Karthik", "101", "password123"
        );

        room.residents.add(resident);
        hostel.rooms.add(room);

        try {
            ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream("hostel.dat")
                );

            out.writeObject(hostel);
            out.close();

            System.out.println("Hostel data saved successfully.");

            ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream("hostel.dat")
                );

            Hostel savedHostel = (Hostel) in.readObject();
            in.close();

            Resident savedResident =
                savedHostel.rooms.get(0).residents.get(0);

            System.out.println("Hostel data loaded successfully.");
            System.out.println("Name: " + savedResident.name);
            System.out.println("Register Number: " +
                savedResident.registerNo);
            System.out.println("Password: " +
                savedResident.password);
            System.out.println("Connection: " +
                savedResident.connection);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}