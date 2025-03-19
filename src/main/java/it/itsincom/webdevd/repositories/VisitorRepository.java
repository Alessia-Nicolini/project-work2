package it.itsincom.webdevd.repositories;

import it.itsincom.webdevd.models.Visitor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VisitorRepository {
    private static final String FILE_PATH = "data/visitors.csv";
    private static final String[] HEADER = {"id", "first_name", "last_name", "email", "phone"};

    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .setSkipHeaderRecord(true)
            .get();

    private static final CSVFormat CSV_FORMAT_WRITE = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .get();

    public List<Visitor> getAllVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                visitors.add(parseRecord(record));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return visitors;
    }

    public Visitor getVisitorById(int id) {
        Optional<Visitor> visitor = getAllVisitors().stream().filter(v -> v.getId() == id).findFirst();
        return visitor.orElse(null);
    }

    public String getNameById(int id) {
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser parser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : parser) {
                if (record.get("id").equals(String.valueOf(id))) {
                    return record.get("first_name") + " " + record.get("last_name");
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void updateVisitor(Visitor updatedVisitor) {
        List<Visitor> visitors = getAllVisitors();
        for (int i = 0; i < visitors.size(); i++) {
            if (visitors.get(i).getId() == updatedVisitor.getId()) {
                visitors.set(i, updatedVisitor);
                break;
            }
        }
        writeAllVisitors(visitors);
    }

    public void addVisitor(Visitor newVisitor) {
        List<Visitor> visitors = getAllVisitors();
        visitors.add(newVisitor);
        writeAllVisitors(visitors);
    }

    private void writeAllVisitors(List<Visitor> visitors) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSV_FORMAT_WRITE)) {
            for (Visitor visitor : visitors) {
                csvPrinter.printRecord(
                        visitor.getId(),
                        visitor.getFirstName(),
                        visitor.getLastName(),
                        visitor.getEmail(),
                        visitor.getPhone()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private Visitor parseRecord(CSVRecord record) {
        int id = Integer.parseInt(record.get("id"));
        String firstName = record.get("first_name");
        String lastName = record.get("last_name");
        String email = record.get("email");
        String phone = record.get("phone");
        return new Visitor(id, firstName, lastName, email, phone);
    }
}
