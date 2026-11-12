
import java.io.IOException;

class ExamException extends Exception {

    ExamException(String message) {
        super(message);
    }

    ExamException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ExamClosedException extends ExamException {

    ExamClosedException(String message) {
        super(message);
    }
}

class StudentNotRegisteredException extends ExamException {

    StudentNotRegisteredException(String message) {
        super(message);
    }
}

class SubmissionStorageException extends ExamException {

    SubmissionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

class Storage {

    void save(String fileName, long sizeKb)
            throws SubmissionStorageException {

        try {
            if (sizeKb > 500) {
                throw new IOException("Disk quota exceeded");
            }

            System.out.println("Saved " + fileName);
        }
        catch (IOException e) {
            throw new SubmissionStorageException(
                "Could not store " + fileName, e
            );
        }
    }
}

class ExamService {

    Storage storage = new Storage();

    void submit(String fileName, long sizeKb,
                boolean examOpen, boolean registered)
                throws ExamException {

        if (!examOpen) {
            throw new ExamClosedException("Exam window is closed");
        }

        if (!registered) {
            throw new StudentNotRegisteredException(
                "Student is not registered for this course"
            );
        }

        storage.save(fileName, sizeKb);
    }
}

class ExamController {

    ExamService service = new ExamService();

    void submitAnswer(String fileName, long sizeKb,
                      boolean examOpen, boolean registered) {

        try {
            service.submit(fileName, sizeKb, examOpen, registered);
        }
        catch (ExamException e) {

            System.out.println("Error: " + e.getMessage());

            if (e.getCause() != null) {
                System.out.println("Cause: " +
                                   e.getCause().getMessage());

                System.out.println("\nFull Chained Stack Trace:");
                e.printStackTrace();
            }
        }
    }
}

public class StorageDemo {

    public static void main(String[] args) {

        ExamController controller = new ExamController();

        System.out.println("TEST 1: Successful Submission");
        controller.submitAnswer("answer01.pdf", 120, true, true);

        System.out.println("\nTEST 2: Exam Window Closed");
        controller.submitAnswer("answer02.pdf", 120, false, true);

        System.out.println("\nTEST 3: Student Not Registered");
        controller.submitAnswer("answer03.pdf", 120, true, false);

        System.out.println("\nTEST 4: File Size Exceeded");
        controller.submitAnswer("answer04.pdf", 800, true, true);
    }
}
