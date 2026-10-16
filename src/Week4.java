public class Week4 {
    public static void main(String[] args) {
        String[] patients = {"Meera", "Ravi"};
        String[] slots = {"09:30", "10:00"};
        int index = 0;

        System.out.println("=== CLINIC SCHEDULE MENU ===");
        System.out.println("1. Book appointment");
        System.out.println("2. View schedule");
        System.out.println("3. Exit");
        while (index < patients.length) {
            System.out.println("Booked: " + patients[index] + " at " + slots[index]);
            index++;
        }
        System.out.println("Confirmed appointments: " + index);
    }
}