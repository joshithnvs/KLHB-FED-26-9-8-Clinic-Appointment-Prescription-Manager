public class Week9 {
    static class Patient {
        private final String name;
        Patient(String name) { this.name = name; }
    }

    static class Doctor {
        private final String name;
        Doctor(String name) { this.name = name; }
    }

    static class Appointment {
        private final String slot;
        private final Patient patient;
        private final Doctor doctor;
        private final String status;

        Appointment(String slot, Patient patient, Doctor doctor, String status) {
            this.slot = slot;
            this.patient = patient;
            this.doctor = doctor;
            this.status = status;
        }

        @Override
        public String toString() {
            return "Appointment{slot=" + slot + ", patient=" + patient.name + ", doctor="
                + doctor.name + ", status=" + status + "}";
        }
    }

    public static void main(String[] args) {
        Appointment appointment = new Appointment("10:00", new Patient("Asha"),
            new Doctor("Dr Iqbal"), "CONFIRMED");
        System.out.println(appointment);
    }
}