import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Week11 {
    private static final Path STORE = Path.of("appointments.csv");

    public static void main(String[] args) throws IOException {
        List<String> rows = List.of(
            "patient,doctor,slot,note",
            "Asha,Dr Iqbal,10:00,Initial review",
            "Ravi,Dr Sen,10:30,Hydration and review"
        );
        Files.write(STORE, rows, StandardCharsets.UTF_8);
        List<String> restored = Files.readAllLines(STORE, StandardCharsets.UTF_8);
        String restoredNote = restored.get(2).split(",", 4)[3];
        System.out.println("SAVED: " + (rows.size() - 1) + " appointments");
        System.out.println("RELOADED: " + (restored.size() - 1) + " appointments");
        System.out.println("FOLLOW-UP NOTE RESTORED: " + restoredNote);
    }
}