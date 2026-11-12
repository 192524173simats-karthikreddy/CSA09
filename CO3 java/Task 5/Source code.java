
public class SeatBooking {

    private final boolean[] booked;
    private int bookingsMade = 0;

    SeatBooking(int seats) {
        booked = new boolean[seats];
    }

    void book(int seat) {

        if (!booked[seat]) {

            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            booked[seat] = true;
            bookingsMade++;

            System.out.println(
                "Seat " + seat + " booked by " +
                Thread.currentThread().getName()
            );
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        SeatBooking screen = new SeatBooking(10);

        Thread[] users = new Thread[100];

        for (int i = 0; i < users.length; i++) {

            final int seat = i % 10;

            users[i] = new Thread(() -> screen.book(seat));

            users[i].start();
        }

        for (Thread t : users) {
            t.join();
        }

        System.out.println("\nSeats sold: " +
                           screen.bookingsMade);
    }
}
