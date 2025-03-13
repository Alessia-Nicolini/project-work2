package it.itsincom.webdevd.repositories;

import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.models.Visit;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VisitRepository {

    private static final Logger logger = Logger.getLogger(VisitRepository.class);
    private static final String CSV_FILE = "data/visits.csv";
    private static final String[] HEADER = {"id", "visitor_name", "employee_name", "start", "expected_duration", "end", "badge_code", "status"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .setSkipHeaderRecord(true)
            .get();

    private static final CSVFormat CSV_FORMAT_WRITE = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .get();

    public List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                visits.add(parseRecord(record));
            }
        } catch (IOException e) {
            logger.error("Error reading CSV file: " + e.getMessage(), e);
        }
        return visits;
    }

    public Optional<Visit> getVisitById(int id) {
        return getAllVisits().stream().filter(v -> v.getId() == id).findFirst();
    }

    public void updateVisit(Visit updatedVisit) {
        List<Visit> visits = getAllVisits();
        boolean found = false;
        for (int i = 0; i < visits.size(); i++) {
            if (visits.get(i).getId() == updatedVisit.getId()) {
                visits.set(i, updatedVisit);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Visit with id " + updatedVisit.getId() + " not found.");
        }
        writeAllVisits(visits);
    }

    public void addVisit(Visit newVisit) {
        List<Visit> visits = getAllVisits();
        visits.add(newVisit);
        writeAllVisits(visits);
    }

    private void writeAllVisits(List<Visit> visits) {
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
                        visit.getBadgeCode() != null ? visit.getBadgeCode() : "",
                        visit.getStatus().name()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            logger.error("Error writing CSV file: " + e.getMessage(), e);
        }
    }

    private Visit parseRecord(CSVRecord record) {
        int id = Integer.parseInt(record.get("id"));
        String visitorName = record.get("visitor_name");
        String employeeName = record.get("employee_name");
        LocalDateTime start = LocalDateTime.parse(record.get("start"), DATE_TIME_FORMATTER);
        int expectedDuration = Integer.parseInt(record.get("expected_duration"));
        String endStr = record.get("end");
        LocalDateTime end = (endStr == null || endStr.isEmpty()) ? null : LocalDateTime.parse(endStr, DATE_TIME_FORMATTER);
        String badgeCode = record.get("badge_code");
        if (badgeCode != null && badgeCode.isEmpty()) {
            badgeCode = null;
        }
        Status status = Status.valueOf(record.get("status").toUpperCase());
        return new Visit(id, visitorName, employeeName, start, expectedDuration, end, badgeCode, status);
    }
}
