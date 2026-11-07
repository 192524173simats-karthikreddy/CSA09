import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
class BrokenStudent {
    String regNo;
    String name;
    BrokenStudent(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BrokenStudent))
            return false;
        BrokenStudent s = (BrokenStudent) o;
        return regNo.equals(s.regNo);
    }
}
class Student {
    private final String regNo;
    private final String name;
    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Student))
            return false;
        Student s = (Student) o;
        return regNo.equals(s.regNo);
    }
    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }
    @Override
    public String toString() {
        return regNo + " " + name;
    }
}
class MutableStudent {
    String regNo;
    MutableStudent(String regNo) {
        this.regNo = regNo;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MutableStudent))
            return false;
        return regNo.equals(((MutableStudent) o).regNo);
    }
    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }
}
public class Attendance {
    public static void main(String[] args) {
        System.out.println("TEST 1");
        Set<BrokenStudent> set1 = new HashSet<>();
        BrokenStudent s1 =
                new BrokenStudent("101", "Arun");
        set1.add(s1);
        set1.add(s1);
        set1.add(new BrokenStudent("102", "Divya"));
        System.out.println("Unique students: " + set1.size());
        Set<BrokenStudent> set2 = new HashSet<>();
        set2.add(new BrokenStudent("101", "Arun"));
        set2.add(new BrokenStudent("101", "Arun"));
        set2.add(new BrokenStudent("102", "Divya"));
        System.out.println("Students without hashCode: "
                + set2.size());
        System.out.println("\nTEST 3");
        Set<MutableStudent> set3 = new HashSet<>();
        MutableStudent s3 =
                new MutableStudent("101");
        set3.add(s3);
        System.out.println("Before change: "
                + set3.contains(s3));
        s3.regNo = "202";
        System.out.println("After change: "
                + set3.contains(s3));
        System.out.println("\nCORRECTED HASHSET");
        Set<Student> present = new HashSet<>();
        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));
        present.add(new Student("192311001", "Arun"));
        System.out.println("Unique students: "
                + present.size());
    }
}
