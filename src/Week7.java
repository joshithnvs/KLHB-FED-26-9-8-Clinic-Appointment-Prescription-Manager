public class Week7 {
    static class AppointmentDesk {
        private String reservedSlot;
        private int count;

        String normalizeSlot(String slot) {
            return slot.trim();
        }

        String book(String patient, String slot) {
            if (patient == null || patient.trim().isEmpty()) return "REJECTED: patient name is required";
            String normalized = normalizeSlot(slot);
            if (normalized.equals(reservedSlot)) return "REJECTED: slot unavailable";
            reservedSlot = normalized;
            count++;
            return "CONFIRMED";
        }

        int count() { return count; }
    }

    public static void main(String[] args) {
        AppointmentDesk desk = new AppointmentDesk();
        System.out.println("BLANK PATIENT -> " + desk.book(" ", "09:30"));
        System.out.println("SLOT \" 10:00 \" -> NORMALIZED: " + desk.normalizeSlot(" 10:00 "));
        System.out.println("BOOK Asha 10:00 -> " + desk.book("Asha", " 10:00 "));
        System.out.println("BOOK Ravi 10:00 -> " + desk.book("Ravi", "10:00"));
        System.out.println("Confirmed appointments: " + desk.count());
    }
}