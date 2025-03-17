package it.itsincom.webdevd.repositories;

import it.itsincom.webdevd.models.Visit;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jboss.logging.Logger;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class NewVisitRepository {
    private static final Logger logger = Logger.getLogger(VisitRepository.class);
    private static final String CSV_FILE = "data/visits.csv";
    private static final String[] HEADER = {"id", "visitor_id", "employee_id", "start", "expected_duration", "end", "badge_code", "status"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final CSVFormat CSV_FORMAT_WRITE = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .get();

    public static String writeVisit(List<Visit> visits) {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSV_FORMAT_WRITE)) {
            for (Visit visit : visits) {
                csvPrinter.printRecord(
                        visit.getId(),
                        visit.getVisitorName(),
                        visit.getEmployeeName(),
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
        return "";
    }
}


