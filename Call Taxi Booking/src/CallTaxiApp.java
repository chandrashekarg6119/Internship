import java.util.*;

class Booking {
    int bookingId;
    int customerId;
    char from;
    char to;
    int pickupTime;
    int dropTime;
    int amount;

    Booking(int bookingId, int customerId, char from, char to, int pickupTime, int dropTime, int amount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.from = from;
        this.to = to;
        this.pickupTime = pickupTime;
        this.dropTime = dropTime;
        this.amount = amount;
    }
}
class Taxi {
    int taxiId;
    char currentPoint = 'A';
    int freeTime = 0;
    int totalEarnings = 0;
    List<Booking> trips = new ArrayList<>();

    Taxi(int id) {
        this.taxiId = id;
    }
}

class TaxiService {
    static int bookingCounter = 1;
    static List<Taxi> taxis = new ArrayList<>();

    // initialize taxis
    static void createTaxis(int n) {
        for (int i = 1; i <= n; i++) {
            taxis.add(new Taxi(i));
        }
    }

    static int distance(char a, char b) {
        return Math.abs(a - b) * 15; // 15 km between points
    }

    static int travelTime(char a, char b) {
        return Math.abs(a - b); // 1 hour between points
    }

    static int calculateFare(int km) {
        if (km <= 5) return 100;
        return 100 + (km - 5) * 10;
    }

    static Taxi findTaxi(char pickup, int pickupTime) {
        Taxi best = null;
        int minDistance = Integer.MAX_VALUE;

        for (Taxi t : taxis) {
            if (t.freeTime <= pickupTime) {
                int d = Math.abs(t.currentPoint - pickup);

                if (d < minDistance) {
                    minDistance = d;
                    best = t;
                } else if (d == minDistance && best != null && t.totalEarnings < best.totalEarnings) {
                    best = t;
                }
            }
        }
        return best;
    }

    static void bookTaxi(int customerId, char from, char to, int pickupTime) {
        Taxi taxi = findTaxi(from, pickupTime);

        if (taxi == null) {
            System.out.println("Booking rejected. No taxi available.");
            return;
        }

        int tripKm = distance(from, to);
        int fare = calculateFare(tripKm);
        int dropTime = pickupTime + travelTime(from, to);

        Booking b = new Booking(bookingCounter++, customerId, from, to, pickupTime, dropTime, fare);
        taxi.trips.add(b);

        taxi.totalEarnings += fare;
        taxi.currentPoint = to;
        taxi.freeTime = dropTime;

        System.out.println("Taxi can be allotted.");
        System.out.println("Taxi-" + taxi.taxiId + " is allotted");
    }

    static void printTaxiDetails() {
        for (Taxi t : taxis) {
            System.out.println("\nTaxi-" + t.taxiId + " Total Earnings: Rs." + t.totalEarnings);
            System.out.println("BookingID CustomerID From To Pickup Drop Amount");
            for (Booking b : t.trips) {
                System.out.println(b.bookingId + "\t" + b.customerId + "\t" + b.from + "\t" + b.to + "\t" + b.pickupTime + "\t" + b.dropTime + "\t" + b.amount);
            }
        }
    }
}
public class CallTaxiApp{
    public static void main(String[] args) {
        TaxiService.createTaxis(4); // works for any number

        // Sample bookings from question
        TaxiService.bookTaxi(1, 'A', 'B', 9);
        TaxiService.bookTaxi(2, 'B', 'D', 9);
        TaxiService.bookTaxi(3, 'B', 'C', 12);

        TaxiService.printTaxiDetails();
    }
}
// =============================
// QUESTION 2 — SIMPLE BLOG WEBSITE (JAVA CONSOLE DESIGN)
// =============================

class Blog {
    int id;
    String title;
    String content;
    String author;
    String category;
    Date date;

    Blog(int id, String title, String content, String author, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.date = new Date();
    }
}

class BlogService {
    List<Blog> blogs = new ArrayList<>();
    int idCounter = 1;

    void addPost(String title, String content, String author, String category) {
        blogs.add(new Blog(idCounter++, title, content, author, category));
        System.out.println("Blog added");
    }

    void editPost(int id, String newContent) {
        for (Blog b : blogs) {
            if (b.id == id) {
                b.content = newContent;
                System.out.println("Updated");
            }
        }
    }

    void deletePost(int id) {
        blogs.removeIf(b -> b.id == id);
        System.out.println("Deleted");
    }

    void viewAll() {
        for (Blog b : blogs) {
            System.out.println(b.id + " " + b.title + " by " + b.author + " [" + b.category + "]");
        }
    }

    void viewById(int id) {
        for (Blog b : blogs) {
            if (b.id == id) {
                System.out.println(b.title + "\n" + b.content);
            }
        }
    }

    void searchByTitle(String key) {
        for (Blog b : blogs) {
            if (b.title.toLowerCase().contains(key.toLowerCase())) {
                System.out.println("Found: " + b.title);
            }
        }
    }

    void filterByCategory(String cat) {
        for (Blog b : blogs) {
            if (b.category.equalsIgnoreCase(cat)) {
                System.out.println(b.title);
            }
        }
    }
}

class BlogAppDemo {
    public static void main(String[] args) {
        BlogService bs = new BlogService();

        // Admin adds posts
        bs.addPost("Java Basics", "Intro to Java", "Admin", "Programming");
        bs.addPost("Spring Guide", "Spring Boot", "Admin", "Framework");

        // Users read
        bs.viewAll();
        bs.viewById(1);
        bs.searchByTitle("java");
        bs.filterByCategory("Programming");
    }
}