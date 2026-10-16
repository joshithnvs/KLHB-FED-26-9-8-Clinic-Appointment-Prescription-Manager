public class Week8 {
    static void printRecursively(String[] appointments, int index) {
        if (index == appointments.length) return;
        System.out.println("[" + index + "] " + appointments[index]);
        printRecursively(appointments, index + 1);
    }

    static int countRecursively(String[] appointments, int index) {
        return index == appointments.length ? 0 : 1 + countRecursively(appointments, index + 1);
    }

    public static void main(String[] args) {
        String[] appointments = {"09:30 | Asha", "10:00 | Ravi"};
        System.out.println("RECURSIVE REPORT");
        printRecursively(appointments, 0);
        System.out.println("RECURSIVE COUNT: " + countRecursively(appointments, 0));
    }
}