package it.itsincom.webdevd.repositories;

import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.models.Visit;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VisitRepository {
    private static final String FILE_PATH = "data/visits.csv";
    private static final String[] HEADER = {
            "id", "visitor_id", "employee_id", "start", "expected_duration", "end", "badge_code", "status"
    };
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
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                visits.add(parseRecord(record));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return visits;
    }

    public Visit getVisitById(int id) {
        Optional<Visit> visit = getAllVisits().stream().filter(v -> v.getId() == id).findFirst();
        return visit.orElse(null);
    }

    public int getLastVisitId() {
        int id = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                id = Integer.parseInt(record.get("id"));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return id;
    }

    public void updateVisit(Visit updatedVisit) {
        List<Visit> visits = getAllVisits();
        for (int i = 0; i < visits.size(); i++) {
            if (visits.get(i).getId() == updatedVisit.getId()) {
                visits.set(i, updatedVisit);
                break;
            }
        }
        writeAllVisits(visits);
    }

    public void addVisit(Visit newVisit) {
        List<Visit> visits = getAllVisits();
        visits.add(newVisit);
        writeAllVisits(visits);
    }

    public void writeAllVisits(List<Visit> visits) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSV_FORMAT_WRITE)) {
            for (Visit visit : visits) {
                csvPrinter.printRecord(
                        visit.getId(),
                        visit.getVisitorId(),
                        visit.getEmployeeId(),
                        visit.getStart().format(DATE_TIME_FORMATTER),
                        visit.getExpectedDuration(),
                        visit.getEnd() != null ? visit.getEnd() : "",
                        visit.getBadgeCode() != null ? visit.getBadgeCode() : "",
                        visit.getStatus().name()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private Visit parseRecord(CSVRecord record) {
        int id = Integer.parseInt(record.get("id"));
        int visitorId = Integer.parseInt(record.get("visitor_id"));
        int employeeId = Integer.parseInt(record.get("employee_id"));
        LocalDateTime start = LocalDateTime.parse(record.get("start"), DATE_TIME_FORMATTER);
        int expectedDuration = Integer.parseInt(record.get("expected_duration"));
        String endStr = record.get("end");
        String end = (endStr == null || endStr.isEmpty()) ? null : endStr;
        String badgeCode = record.get("badge_code");
        if (badgeCode != null && badgeCode.isEmpty()) {
            badgeCode = null;
        }
        Status status = Status.valueOf(record.get("status").toUpperCase());
        return new Visit(id, visitorId, employeeId, start, expectedDuration, end, badgeCode, status);
    }
}
