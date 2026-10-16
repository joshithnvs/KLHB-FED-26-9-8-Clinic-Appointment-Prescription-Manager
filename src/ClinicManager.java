import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/** Final release: a teaching demo for scheduling, not medical advice or prescription management. */
public class ClinicManager {
    static class Appointment {
        final String patient;
        final String doctor;
        final String slot;
        final String status;
        final String followUpNote;

        Appointment(String patient, String doctor, String slot, String followUpNote) {
            this.patient = patient;
            this.doctor = doctor;
            this.slot = slot;
            this.status = "CONFIRMED";
            this.followUpNote = followUpNote;
        }
    }

    private final List<Appointment> appointments = new ArrayList<>();

    private String book(String patient, String doctor, String slot, String followUpNote) {
        if (patient == null || patient.trim().isEmpty()) return "REJECTED: patient name is required";
        for (Appointment appointment : appointments) {
            if (appointment.slot.equals(slot)) return "REJECTED: " + slot + " already booked";
        }
        appointments.add(new Appointment(patient, doctor, slot, followUpNote));
        return "CONFIRMED";
    }

    private void printReport() {
        appointments.sort(Comparator.comparing(appointment -> appointment.slot));
        for (Appointment appointment : appointments) {
            System.out.println(appointment.slot + " | " + appointment.patient + " | " + appointment.doctor
                + " | " + appointment.status);
        }
        Map<String, Long> doctorTotals = appointments.stream().collect(Collectors.groupingBy(
            appointment -> appointment.doctor,
            Collectors.counting()
        ));
        System.out.println("TOTAL CONFIRMED: " + appointments.size());
        doctorTotals.entrySet().stream().sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
    }

    private static void runInteractive() {
        ClinicManager clinic = new ClinicManager();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Patient name: ");
            String patient = scanner.nextLine();
            System.out.print("Doctor name: ");
            String doctor = scanner.nextLine();
            System.out.print("Slot (HH:MM): ");
            String slot = scanner.nextLine().trim();
            System.out.println("Booking result: " + clinic.book(patient, doctor, slot, "Administrative follow-up"));
            clinic.printReport();
        }
    }

    private static void runDemo() {
        ClinicManager clinic = new ClinicManager();
        System.out.println("CLINIC APPOINTMENT & PRESCRIPTION MANAGER - FINAL RELEASE");
        System.out.println("Book Asha 09:30 -> " + clinic.book("Asha", "Dr Iqbal", "09:30", "Initial review"));
        System.out.println("Book Ravi 10:30 -> " + clinic.book("Ravi", "Dr Iqbal", "10:30", "Follow-up"));
        System.out.println("Book Meera 11:00 -> " + clinic.book("Meera", "Dr Sen", "11:00", "Administrative review"));
        System.out.println("Conflict test -> " + clinic.book("Nila", "Dr Sen", "09:30", "Duplicate slot check"));
        clinic.printReport();
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("interactive")) runInteractive();
        else runDemo();
    }
}