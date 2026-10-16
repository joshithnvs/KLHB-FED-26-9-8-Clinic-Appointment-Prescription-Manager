public class Week5 {
    static String normalizeSlot(String slot) {
        return slot.trim();
    }

    static boolean isSlotAvailable(String requested, String[] reservedSlots, int count) {
        for (int index = 0; index < count; index++) {
            if (reservedSlots[index].equals(normalizeSlot(requested))) return false;
        }
        return true;
    }

    static String book(String patient, String slot, String[] reservedSlots, int count) {
        return isSlotAvailable(slot, reservedSlots, count)
            ? "CONFIRMED"
            : "REJECTED: slot unavailable";
    }

    public static void main(String[] args) {
        String[] reservedSlots = new String[2];
        int count = 0;
        String asha = book("Asha", "10:00", reservedSlots, count);
        if (asha.equals("CONFIRMED")) reservedSlots[count++] = "10:00";
        System.out.println("Book Asha 10:00 -> " + asha);
        System.out.println("Book Ravi 10:00 -> " + book("Ravi", "10:00", reservedSlots, count));
    }
}