
public class Week2 {
}
    public static void main(String[] args) {
        String requested = "10:30 AM", reserved = "11:00 AM";
        System.out.println(requested.equals(reserved) ? "Slot unavailable; choose another time." : "Slot confirmed: " + requested);
    }