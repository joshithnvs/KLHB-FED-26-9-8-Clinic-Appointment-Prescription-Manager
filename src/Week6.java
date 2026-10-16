public class Week6 {
    static int findSlot(String[] slots, String requestedSlot) {
        for (int index = 0; index < slots.length; index++) {
            if (slots[index].equals(requestedSlot)) return index;
        }
        return -1;
    }

    static int findPatient(String[] patients, String requestedPatient) {
        for (int index = 0; index < patients.length; index++) {
            if (patients[index].equalsIgnoreCase(requestedPatient)) return index;
        }
        return -1;
    }

    public static void main(String[] args) {
        String[] slots = {"10:00", "10:30"};
        String[] patients = {"Asha", "Ravi"};
        int asha = findSlot(slots, "10:00");
        int ravi = findPatient(patients, "Ravi");
        int missing = findSlot(slots, "11:00");
        System.out.println("Find slot 10:00 -> " + patients[asha]);
        System.out.println("Find patient Ravi -> " + slots[ravi]);
        System.out.println("Find slot 11:00 -> " + (missing == -1 ? "NOT FOUND" : patients[missing]));
    }
}