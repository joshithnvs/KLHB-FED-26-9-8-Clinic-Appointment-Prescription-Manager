public class Week10 {
    static class InvalidSlotException extends Exception {
        InvalidSlotException(String message) { super(message); }
    }

    static String validateSlot(String slot) throws InvalidSlotException {
        if (!slot.matches("\\d{2}:\\d{2}")) throw new InvalidSlotException("slot must use HH:MM format");
        int hour = Integer.parseInt(slot.substring(0, 2));
        int minute = Integer.parseInt(slot.substring(3, 5));
        if (minute > 59 || hour < 9 || hour > 17) {
            throw new InvalidSlotException("slot must be between 09:00 and 17:00");
        }
        return slot;
    }

    static void book(String slot) {
        try {
            validateSlot(slot);
            System.out.println("BOOK " + slot + " -> CONFIRMED");
        } catch (InvalidSlotException exception) {
            System.out.println("BOOK " + slot + " -> REJECTED: " + exception.getMessage());
        }
    }

    public static void main(String[] args) {
        book("10:00");
        book("18:30");
        book("morning");
    }
}