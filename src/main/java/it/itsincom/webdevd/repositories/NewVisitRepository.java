package it.itsincom.webdevd.repositories;

import it.itsincom.webdevd.models.Visit;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class NewVisitRepository {
    private static final String CSV_FILE = "data/visits.csv";
    private static final String[] HEADER = {"id", "visitor_id", "employee_id", "start", "expected_duration", "end", "badge_code", "status"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final CSVFormat CSV_FORMAT_WRITE = CSVFormat.Builder.create()
            .get();

    public String writeVisit(List<Visit> visits) {
        boolean fileExists = Files.exists(Paths.get(CSV_FILE));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSV_FORMAT_WRITE)) {
            if (!fileExists) {
                csvPrinter.printRecord(HEADER);
            }
            for (Visit visit : visits) {
                csvPrinter.printRecord(
                        visit.getId(),
                        visit.getVisitorId(),
                        visit.getEmployeeId(),
                        visit.getStart().format(DATE_TIME_FORMATTER),
                        visit.getExpectedDuration(),
                        visit.getEnd() != null ? visit.getEnd().format(DATE_TIME_FORMATTER) : "",
                        "",
                        ""
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            return "Errore nella scrittura del file.";
        }
        return "Scrittura completata con successo.";
    }
}


