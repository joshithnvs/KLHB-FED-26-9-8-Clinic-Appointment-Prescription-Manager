/**
 * KLHB-FED-26-9-8 - Clinic Appointment & Prescription Manager - FIXED VERSION
 * Authors: Kaushal Palla (2620040065), Joshith NVS (2620030096)
 * Compile: javac ClinicAppointmentManagerFixed.java
 * Run: java ClinicAppointmentManagerFixed
 */
import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

public class ClinicAppointmentManagerFixed {

    static List<Patient> patients = new ArrayList<>();
    static List<Doctor> doctors = new ArrayList<>();
    static List<Appointment> appointments = new ArrayList<>();
    static List<Prescription> prescriptions = new ArrayList<>();
    static List<Billing> billings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String DATA_DIR = "clinic_data";
    static DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    static class Patient implements Serializable {
        String id, name, gender, phone, address, registeredOn;
        int age; int totalVisits = 0;
        Patient(String id, String name, int age, String gender, String phone, String address) {
            this.id = id; this.name = name; this.age = age;
            this.gender = gender; this.phone = phone; this.address = address;
            this.registeredOn = LocalDate.now().format(DATE_FMT);
        }
    }
    static class TimeSlot implements Serializable {
        int id; String time;
        TimeSlot(int id, String time) { this.id = id; this.time = time; }
    }
    static class Doctor implements Serializable {
        String id, name, specialty; double fee; List<TimeSlot> slots;
        Doctor(String id, String name, String specialty, double fee, List<TimeSlot> slots) {
            this.id = id; this.name = name; this.specialty = specialty; this.fee = fee; this.slots = slots;
        }
    }
    static class Appointment implements Serializable {
        String id, token, patientId, patientName, doctorId, doctorName, slotTime, date, displayDate, status, diagnosis;
        int slotId; double fee;
        Appointment(String id, String token, String patientId, String patientName, String doctorId, String doctorName, int slotId, String slotTime, double fee) {
            this.id = id; this.token = token; this.patientId = patientId; this.patientName = patientName;
            this.doctorId = doctorId; this.doctorName = doctorName; this.slotId = slotId;
            this.slotTime = slotTime; this.fee = fee;
            this.date = LocalDate.now().toString();
            this.displayDate = LocalDate.now().format(DATE_FMT);
            this.status = "Scheduled";
        }
    }
    static class Medication implements Serializable {
        String drug, dosage, frequency, duration, instruction;
        Medication(String drug, String dosage, String frequency, String duration, String instruction) {
            this.drug = drug; this.dosage = dosage; this.frequency = frequency; this.duration = duration; this.instruction = instruction;
        }
    }
    static class Prescription implements Serializable {
        String id, token, patientId, patientName, doctorName, date, displayDate, diagnosis, symptoms, advice;
        List<Medication> medications;
        Prescription(String id, String token, String patientId, String patientName, String doctorName, String diagnosis, String symptoms, List<Medication> meds, String advice) {
            this.id = id; this.token = token; this.patientId = patientId; this.patientName = patientName;
            this.doctorName = doctorName; this.diagnosis = diagnosis; this.symptoms = symptoms;
            this.medications = meds; this.advice = advice;
            this.date = LocalDate.now().toString();
            this.displayDate = LocalDate.now().format(DATE_FMT);
        }
    }
    static class Billing implements Serializable {
        String invoiceNo, token, patientId, paymentStatus, date, displayDate;
        double consultationFee, registrationFee, total;
        Billing(String invoiceNo, String token, String patientId, double consultationFee) {
            this.invoiceNo = invoiceNo; this.token = token; this.patientId = patientId;
            this.consultationFee = consultationFee; this.registrationFee = 100.0;
            this.total = consultationFee + registrationFee;
            this.paymentStatus = "Paid (Cash)";
            this.date = LocalDate.now().toString();
            this.displayDate = LocalDate.now().format(DATE_FMT);
        }
    }

    static void initDoctors() {
        if (!doctors.isEmpty()) return;
        doctors.add(new Doctor("D-101", "Dr. Ananya Rao", "Cardiology", 500,
            Arrays.asList(new TimeSlot(1, "10:00 AM - 10:30 AM"), new TimeSlot(2, "10:30 AM - 11:00 AM"), new TimeSlot(3, "11:00 AM - 11:30 AM"), new TimeSlot(4, "11:30 AM - 12:00 PM"), new TimeSlot(5, "12:00 PM - 12:30 PM"))));
        doctors.add(new Doctor("D-102", "Dr. Rajesh Kumar", "General Medicine", 400,
            Arrays.asList(new TimeSlot(1, "10:00 AM - 10:30 AM"), new TimeSlot(2, "10:30 AM - 11:00 AM"), new TimeSlot(3, "11:00 AM - 11:30 AM"), new TimeSlot(4, "11:30 AM - 12:00 PM"))));
        doctors.add(new Doctor("D-103", "Dr. Priya Singh", "Dermatology", 450,
            Arrays.asList(new TimeSlot(1, "02:00 PM - 02:30 PM"), new TimeSlot(2, "02:30 PM - 03:00 PM"), new TimeSlot(3, "03:00 PM - 03:30 PM"), new TimeSlot(4, "03:30 PM - 04:00 PM"))));
    }

    static boolean isValidPhone(String phone) { return Pattern.matches("^[6-9]\\d{9}$", phone); }
    static boolean isValidDosage(String freq) { return Pattern.matches("^[0-1]-[0-1]-[0-1]$", freq); }

    static String getNextPatientId() {
        if (patients.isEmpty()) return "P-1001";
        int max = 1000;
        for (Patient p : patients) { try { int num = Integer.parseInt(p.id.split("-")[1]); if (num > max) max = num; } catch (Exception e) {} }
        return "P-" + (max + 1);
    }
    static String getNextToken() {
        if (appointments.isEmpty()) return "C-1";
        int max = 0;
        for (Appointment a : appointments) { try { int num = Integer.parseInt(a.token.split("-")[1]); if (num > max) max = num; } catch (Exception e) {} }
        return "C-" + (max + 1);
    }
    static String getNextInvoice() { return String.format("INV-%d-%03d", LocalDate.now().getYear(), billings.size() + 1); }

    @SuppressWarnings("unchecked")
    static void loadData() {
        try { new File(DATA_DIR).mkdirs(); } catch (Exception e) {}
        patients = (List<Patient>) loadObject(DATA_DIR + "/patients.dat"); if (patients == null) patients = new ArrayList<>();
        doctors = (List<Doctor>) loadObject(DATA_DIR + "/doctors.dat"); if (doctors == null) doctors = new ArrayList<>();
        appointments = (List<Appointment>) loadObject(DATA_DIR + "/appointments.dat"); if (appointments == null) appointments = new ArrayList<>();
        prescriptions = (List<Prescription>) loadObject(DATA_DIR + "/prescriptions.dat"); if (prescriptions == null) prescriptions = new ArrayList<>();
        billings = (List<Billing>) loadObject(DATA_DIR + "/billings.dat"); if (billings == null) billings = new ArrayList<>();
    }
    static Object loadObject(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) { return ois.readObject(); } catch (Exception e) { return null; }
    }
    static void saveData() {
        try { new File(DATA_DIR).mkdirs(); } catch (Exception e) {}
        saveObject(DATA_DIR + "/patients.dat", patients); saveObject(DATA_DIR + "/doctors.dat", doctors);
        saveObject(DATA_DIR + "/appointments.dat", appointments); saveObject(DATA_DIR + "/prescriptions.dat", prescriptions);
        saveObject(DATA_DIR + "/billings.dat", billings);
    }
    static void saveObject(String path, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) { oos.writeObject(obj); } catch (Exception e) { System.out.println("Save error: " + e.getMessage()); }
    }

    static void registerPatient() {
        System.out.println("\n============================================================");
        System.out.println("               REGISTER NEW PATIENT");
        System.out.println("============================================================");
        System.out.print("Enter Patient Name: ");
        String name = sc.nextLine().trim();
        while (name.length() < 2) { System.out.println("Error: Name must be at least 2 characters!"); System.out.print("Enter Patient Name: "); name = sc.nextLine().trim(); }
        System.out.print("Enter Age: ");
        String ageStr = sc.nextLine().trim();
        while (!ageStr.matches("\\d+") || Integer.parseInt(ageStr) <= 0 || Integer.parseInt(ageStr) >= 120) { System.out.println("Error: Enter valid age (1-119)!"); System.out.print("Enter Age: "); ageStr = sc.nextLine().trim(); }
        int age = Integer.parseInt(ageStr);
        System.out.print("Enter Gender (M/F/O): ");
        String gender = sc.nextLine().trim().toUpperCase();
        while (!Arrays.asList("M","F","O","MALE","FEMALE").contains(gender)) { System.out.println("Error: Enter M/F/O"); System.out.print("Enter Gender (M/F/O): "); gender = sc.nextLine().trim().toUpperCase(); }
        if (gender.equals("M") || gender.equals("MALE")) gender = "Male"; else if (gender.equals("F") || gender.equals("FEMALE")) gender = "Female"; else gender = "Other";
        System.out.print("Enter Phone (10 digits): ");
        String phone = sc.nextLine().trim();
        while (!isValidPhone(phone)) { System.out.println("Error: Invalid phone! Must be 10 digits starting with 6-9"); System.out.print("Enter Phone (10 digits): "); phone = sc.nextLine().trim(); }
        System.out.print("Enter Address: ");
        String address = sc.nextLine().trim();
        String pid = getNextPatientId();
        Patient p = new Patient(pid, name, age, gender, phone, address);
        patients.add(p); saveData();
        System.out.println("\nPatient Registered Successfully!");
        System.out.println("Patient ID : " + pid);
        System.out.println("Patient Name : " + name);
    }

    static void scheduleAppointment() {
        System.out.println("\n============================================================");
        System.out.println("               SCHEDULE AN APPOINTMENT");
        System.out.println("============================================================");
        System.out.print("Enter Patient ID: ");
        String patientId = sc.nextLine().trim().toUpperCase();
        Patient patient = null;
        for (Patient p : patients) if (p.id.equals(patientId)) patient = p;
        if (patient == null) {
            System.out.println("\nError: Patient ID '" + patientId + "' not found in database!");
            System.out.print("Would you like to register a new patient? (Y/N): ");
            String choice = sc.nextLine().trim().toUpperCase();
            if (choice.equals("Y")) { registerPatient(); if (!patients.isEmpty()) patient = patients.get(patients.size()-1); else return; patientId = patient.id; } else return;
        }
        System.out.println("Patient Name : " + patient.name);
        System.out.println("\nSelect Doctor:");
        for (int i = 0; i < doctors.size(); i++) { Doctor d = doctors.get(i); System.out.printf("[%d] %s (%s) - Fee: Rs.%.0f\n", i+1, d.name, d.specialty, d.fee); }
        System.out.print("Enter Doctor Number: ");
        int docIdx; try { docIdx = Integer.parseInt(sc.nextLine().trim()) - 1; } catch (Exception e) { System.out.println("Error: Invalid doctor selection!"); return; }
        if (docIdx < 0 || docIdx >= doctors.size()) { System.out.println("Error: Invalid doctor selection!"); return; }
        Doctor doctor = doctors.get(docIdx);
        System.out.println("\nSelected Doctor : " + doctor.name + " (" + doctor.specialty + ")");
        System.out.println("\nAvailable Time Slots:");
        String today = LocalDate.now().toString();
        for (TimeSlot slot : doctor.slots) {
            boolean booked = false;
            for (Appointment a : appointments) { if (a.doctorId.equals(doctor.id) && a.slotId == slot.id && a.date.equals(today) && !a.status.equals("Cancelled")) { booked = true; break; } }
            System.out.printf("[%d] %s (%s)\n", slot.id, slot.time, booked ? "Booked" : "Available");
        }
        System.out.print("\nSelect Slot ID: ");
        int slotId; try { slotId = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { System.out.println("Error: Invalid Slot ID!"); return; }
        boolean conflict = false;
        for (Appointment a : appointments) { if (a.doctorId.equals(doctor.id) && a.slotId == slotId && a.date.equals(today) && !a.status.equals("Cancelled")) { conflict = true; break; } }
        if (conflict) {
            String slotTime = ""; for (TimeSlot s : doctor.slots) if (s.id == slotId) slotTime = s.time;
            System.out.println("\nError: Selected time slot (" + slotTime + ") is already booked!");
            System.out.print("Please select an alternate available slot: ");
            try { slotId = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { System.out.println("Error: Invalid slot!"); return; }
            for (Appointment a : appointments) { if (a.doctorId.equals(doctor.id) && a.slotId == slotId && a.date.equals(today) && !a.status.equals("Cancelled")) { System.out.println("Error: Alternate slot also booked!"); return; } }
        }
        String slotTime = ""; for (TimeSlot s : doctor.slots) if (s.id == slotId) slotTime = s.time;
        if (slotTime.isEmpty()) { System.out.println("Error: Invalid Slot ID!"); return; }
        String token = getNextToken();
        String aptId = String.format("APT-%04d", appointments.size()+1);
        Appointment apt = new Appointment(aptId, token, patientId, patient.name, doctor.id, doctor.name, slotId, slotTime, doctor.fee);
        appointments.add(apt); saveData();
        System.out.println("\n------------------------------------------------------------");
        System.out.println("Appointment Confirmed! Token Number: " + token);
        System.out.println("Patient: " + patient.name + " | Doctor: " + doctor.name);
        System.out.println("Slot: " + slotTime);
        System.out.println("------------------------------------------------------------");
    }

    static void viewDailyQueue() {
        System.out.println("\n============================================================");
        System.out.println("              DAILY APPOINTMENT QUEUE");
        System.out.println("============================================================");
        String today = LocalDate.now().toString();
        List<Appointment> todayApts = new ArrayList<>();
        for (Appointment a : appointments) if (a.date.equals(today)) todayApts.add(a);
        if (todayApts.isEmpty()) { System.out.println("No appointments for today (" + LocalDate.now().format(DATE_FMT) + ")"); return; }
        System.out.println("Date: " + LocalDate.now().format(DATE_FMT) + "\n");
        System.out.printf("%-8s %-18s %-20s %-22s %s\n", "Token", "Patient", "Doctor", "Time", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        for (Appointment a : todayApts) {
            String pn = a.patientName.length()>17 ? a.patientName.substring(0,17) : a.patientName;
            String dn = a.doctorName.length()>19 ? a.doctorName.substring(0,19) : a.doctorName;
            System.out.printf("%-8s %-18s %-20s %-22s %s\n", a.token, pn, dn, a.slotTime, a.status);
        }
        long scheduled = 0, completed = 0; double revenue = 0;
        for (Appointment a : todayApts) { if (a.status.equals("Scheduled")) scheduled++; if (a.status.equals("Completed")) completed++; }
        for (Billing b : billings) if (b.date.equals(today)) revenue += b.total;
        System.out.println("\n============================================================");
        System.out.println("             DAILY CLINIC SUMMARY");
        System.out.println("============================================================");
        System.out.println("Total Registered Patients : " + patients.size());
        System.out.println("Appointments Scheduled    : " + todayApts.size());
        System.out.println("Consultations Completed   : " + completed);
        System.out.println("Pending Appointments      : " + scheduled);
        System.out.printf("Total Revenue Collected   : Rs.%.2f\n", revenue);
    }

    static void consultation() {
        System.out.println("\n============================================================");
        System.out.println("         DOCTOR CONSULTATION & PRESCRIPTION");
        System.out.println("============================================================");
        String today = LocalDate.now().toString();
        List<Appointment> scheduled = new ArrayList<>();
        for (Appointment a : appointments) if (a.date.equals(today) && a.status.equals("Scheduled")) scheduled.add(a);
        if (scheduled.isEmpty()) { System.out.println("No scheduled appointments for today!"); return; }
        System.out.println("\nScheduled Appointments Today:");
        for (Appointment a : scheduled) { System.out.println(a.token + " - " + a.patientName + " (" + a.patientId + ") with " + a.doctorName + " at " + a.slotTime); }
        System.out.print("\nEnter Token Number to Consult (e.g., C-1): ");
        String token = sc.nextLine().trim().toUpperCase();
        Appointment apt = null; for (Appointment a : scheduled) if (a.token.equals(token)) apt = a;
        if (apt == null) { System.out.println("Error: Token " + token + " not found or not scheduled!"); return; }
        System.out.println("\nConsulting: " + apt.patientName + " (ID: " + apt.patientId + ")");
        System.out.println("Doctor: " + apt.doctorName);
        System.out.print("Enter Diagnosis: ");
        String diagnosis = sc.nextLine().trim();
        while (diagnosis.isEmpty()) { System.out.println("Error: Diagnosis cannot be empty!"); System.out.print("Enter Diagnosis: "); diagnosis = sc.nextLine().trim(); }
        System.out.print("Enter Symptoms/Notes: ");
        String symptoms = sc.nextLine().trim();
        System.out.println("\n--- Prescription Entry ---");
        System.out.println("Enter medications (leave drug name empty to finish)");
        List<Medication> meds = new ArrayList<>();
        while (true) {
            System.out.print("\nDrug Name (e.g., Tab. Amlodipine): "); String drug = sc.nextLine().trim(); if (drug.isEmpty()) break;
            System.out.print("Dosage (mg, e.g., 5mg): "); String dosage = sc.nextLine().trim();
            System.out.print("Frequency (e.g., 1-0-0): "); String freq = sc.nextLine().trim();
            while (!isValidDosage(freq)) { System.out.println("Error: Frequency must be like 1-0-0, 0-0-1, 1-1-1"); System.out.print("Frequency (e.g., 1-0-0): "); freq = sc.nextLine().trim(); }
            System.out.print("Duration (e.g., 30 Days): "); String duration = sc.nextLine().trim();
            System.out.print("Instruction (After Food / Night): "); String instr = sc.nextLine().trim();
            meds.add(new Medication(drug, dosage, freq, duration, instr));
        }
        if (meds.isEmpty()) { System.out.println("No medications entered!"); return; }
        System.out.print("\nDoctor Advice (e.g., Low salt diet): "); String advice = sc.nextLine().trim();
        String rxId = String.format("RX-%04d", prescriptions.size()+1);
        Prescription rx = new Prescription(rxId, apt.token, apt.patientId, apt.patientName, apt.doctorName, diagnosis, symptoms, meds, advice);
        prescriptions.add(rx); apt.status = "Completed"; apt.diagnosis = diagnosis;
        for (Patient p : patients) if (p.id.equals(apt.patientId)) p.totalVisits++;
        String invNo = getNextInvoice(); Billing bill = new Billing(invNo, apt.token, apt.patientId, apt.fee); billings.add(bill); saveData();
        System.out.println("\n============================================================");
        System.out.println("              PRESCRIPTION ISSUED");
        System.out.println("============================================================");
        System.out.println("Patient ID : " + rx.patientId);
        System.out.println("Patient Name : " + rx.patientName);
        System.out.println("Doctor : " + rx.doctorName);
        System.out.println("Date : " + rx.displayDate);
        System.out.println("Diagnosis : " + diagnosis);
        System.out.println("\nPrescribed Medications:\n");
        for (Medication m : meds) { System.out.println(m.drug + " (" + m.dosage + ") - " + m.frequency + " - " + m.duration + " (" + m.instruction + ")"); }
        System.out.println("\nAdvice: " + advice);
        System.out.println("\nPrescription saved successfully!");
        System.out.println("Invoice Generated: " + invNo);
    }

    static void viewHistory() {
        System.out.println("\n============================================================");
        System.out.println("              PATIENT MEDICAL RECORD");
        System.out.println("============================================================");
        System.out.print("Enter Patient ID: "); String pid = sc.nextLine().trim().toUpperCase();
        Patient patient = null; for (Patient p : patients) if (p.id.equals(pid)) patient = p;
        if (patient == null) { System.out.println("\nError: Patient ID '" + pid + "' not found in database!"); return; }
        List<Prescription> hist = new ArrayList<>(); for (Prescription rx : prescriptions) if (rx.patientId.equals(pid)) hist.add(rx);
        System.out.println("\nPatient ID : " + patient.id);
        System.out.println("Name : " + patient.name + " | Age: " + patient.age + " | Gender: " + patient.gender);
        System.out.println("Phone: " + patient.phone);
        System.out.println("Total Visits : " + hist.size());
        System.out.println("\n------------------------------------------------------------");
        if (hist.isEmpty()) { System.out.println("No medical history found!"); System.out.println("\nVisit 1: " + patient.registeredOn + " | Diagnosis: New Registration"); return; }
        int idx = 1; for (Prescription rx : hist) {
            System.out.println("\nVisit " + idx + ": " + rx.displayDate + " | Diagnosis: " + rx.diagnosis);
            String medStr = ""; for (int i = 0; i < rx.medications.size(); i++) { Medication m = rx.medications.get(i); medStr += m.drug + " (" + m.dosage + ")"; if (i < rx.medications.size()-1) medStr += ", "; }
            System.out.println("Prescription: " + medStr);
            if (rx.advice != null && !rx.advice.isEmpty()) System.out.println("Advice: " + rx.advice);
            idx++;
        }
    }

    static void billingSummary() {
        System.out.println("\n============================================================");
        System.out.println("                 VISIT INVOICE");
        System.out.println("============================================================");
        System.out.print("Enter Patient ID for Billing Summary (or press Enter for all today): "); String pid = sc.nextLine().trim().toUpperCase();
        String today = LocalDate.now().toString();
        if (!pid.isEmpty()) {
            List<Billing> list = new ArrayList<>(); for (Billing b : billings) if (b.patientId.equals(pid)) list.add(b);
            if (list.isEmpty()) { System.out.println("No billing records for " + pid); return; }
            Billing bill = list.get(list.size()-1);
            System.out.println("\nInvoice No : " + bill.invoiceNo);
            System.out.println("Patient ID : " + bill.patientId);
            System.out.printf("Consultation : Rs.%.2f\n", bill.consultationFee);
            System.out.printf("Registration : Rs.%.2f\n", bill.registrationFee);
            System.out.printf("Total Payable : Rs.%.2f\n", bill.total);
            System.out.println("Payment Status : " + bill.paymentStatus);
        } else {
            List<Billing> todayBills = new ArrayList<>(); for (Billing b : billings) if (b.date.equals(today)) todayBills.add(b);
            if (todayBills.isEmpty()) { System.out.println("No billings for today!"); return; }
            System.out.println("\nDate: " + LocalDate.now().format(DATE_FMT));
            double total = 0; for (Billing b : todayBills) { System.out.println("\n" + b.invoiceNo + " | " + b.patientId + " | Rs." + String.format("%.2f", b.total) + " | " + b.paymentStatus); total += b.total; }
            System.out.printf("\nTotal Revenue Today: Rs.%.2f\n", total);
        }
        System.out.println("\n------------------------------------------------------------");
        System.out.println("Total Registered Patients : " + patients.size());
        long todayCount = 0; for (Appointment a : appointments) if (a.date.equals(today)) todayCount++;
        System.out.println("Appointments Today : " + todayCount);
    }

    public static void main(String[] args) {
        loadData(); initDoctors(); saveData();
        while (true) {
            System.out.println("\n============================================================");
            System.out.println("       CLINIC APPOINTMENT & PRESCRIPTION MANAGER");
            System.out.println("============================================================");
            System.out.println("\n------------------------ MAIN MENU ------------------------\n");
            System.out.println("1. Register New Patient");
            System.out.println("2. Schedule Appointment");
            System.out.println("3. View Daily Appointment Queue");
            System.out.println("4. Doctor Consultation & Prescription");
            System.out.println("5. View Patient Medical History");
            System.out.println("6. Billing Summary");
            System.out.println("7. Exit");
            System.out.println("\n------------------------------------------------------------");
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": registerPatient(); break;
                case "2": scheduleAppointment(); break;
                case "3": viewDailyQueue(); break;
                case "4": consultation(); break;
                case "5": viewHistory(); break;
                case "6": billingSummary(); break;
                case "7": System.out.println("\nThank you for using Clinic Appointment & Prescription Manager!"); System.out.println("Data saved to clinic_data/ folder"); return;
                default: System.out.println("\nError: Invalid choice! Please select 1-7");
            }
        }
    }
}
