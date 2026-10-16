public class Week3 {
    public static void main(String[] args) {
        String[] slots = {"09:30 Meera", "10:30 Arun", "11:00 Fatima"};
        for (String slot : slots) System.out.println(slot);
        System.out.println("Confirmed appointments: " + slots.length);
    }
}