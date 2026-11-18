
import java.sql.*;
import java.util.*;

public class A3DAO {

    static final String URL = "jdbc:mysql://localhost:3306/college";
    static final String USER = "root";
    static final String PASSWORD = "your_password";

    static class Student {
        int id;
        String name;
        String department;
        String email;

        Student(int id, String name, String department, String email) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.email = email;
        }

        public String toString() {
            return id + " " + name + " " + department + " " + email;
        }
    }

    interface Dao<T, K> {
        void insert(T obj) throws SQLException;
        T findById(K id) throws SQLException;
        List<T> findAll() throws SQLException;
        void update(T obj) throws SQLException;
        void delete(K id) throws SQLException;
    }

    static class StudentDao implements Dao<Student, Integer> {

        Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }

        public void insert(Student s) throws SQLException {
            String sql = "INSERT INTO student VALUES (?, ?, ?, ?)";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, s.id);
                ps.setString(2, s.name);
                ps.setString(3, s.department);
                ps.setString(4, s.email);

                ps.executeUpdate();
                System.out.println("Student inserted.");
            }
        }

        public Student findById(Integer id) throws SQLException {
            String sql = "SELECT * FROM student WHERE id = ?";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getString("email")
                        );
                    }
                }
            }

            return null;
        }

        public List<Student> findAll() throws SQLException {
            List<Student> students = new ArrayList<>();
            String sql = "SELECT * FROM student";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("email")
                    );

                    students.add(s);
                }
            }

            return students;
        }

        public void update(Student s) throws SQLException {
            String sql = "UPDATE student SET name=?, department=?, email=? WHERE id=?";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, s.name);
                ps.setString(2, s.department);
                ps.setString(3, s.email);
                ps.setInt(4, s.id);

                ps.executeUpdate();
                System.out.println("Student updated.");
            }
        }

        public void delete(Integer id) throws SQLException {
            String sql = "DELETE FROM student WHERE id=?";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
                System.out.println("Student deleted.");
            }
        }
    }

    public static void main(String[] args) {
        StudentDao dao = new StudentDao();

        try {
            Student s = new Student(
                101, "Karthik", "AI and DS", "karthik@gmail.com"
            );

            dao.insert(s);

            Student result = dao.findById(101);

            if (result != null) {
                System.out.println("Student Found:");
                System.out.println(result);
            } else {
                System.out.println("Student not found.");
            }

            System.out.println("\nAll Students:");

            for (Student student : dao.findAll()) {
                System.out.println(student);
            }

            Student updated = new Student(
                101, "Karthik Reddy", "AI and DS", "karthik@gmail.com"
            );

            dao.update(updated);

            // Uncomment to delete the student:
            // dao.delete(101);

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
