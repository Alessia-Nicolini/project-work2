package it.itsincom.webdevd.repositories;
import it.itsincom.webdevd.model.Visitor;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NewVisitorRepository {
    private static final String FILE_PATH = "data/visitors.csv";
    private static final String[] HEADER = {
            "id", "name", "surname", "phone", "email"
    };
    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .setSkipHeaderRecord(true)
            .get();
    private static final CSVFormat CSV_FORMAT_WRITE = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .get();

    public List<Visitor> getAllVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        try (Reader Reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(Reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                Visitor visitor = parseRecord(record);
                visitors.add(visitor);
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return visitors;
    };

    public Visitor getVisitor(Visitor guest) {
        Optional<Visitor> visitor = getAllVisitors().stream().filter(v -> v.equals(guest)).findFirst();
        return visitor.orElse(null);
    }

    public void updateVisitor(Visitor updatedVisitor) {
        List<Visitor> visitors = getAllVisitors();

        for (int i = 0; i < visitors.size(); i++) {
            if (visitors.get(i).getId() == updatedVisitor.getId()) {
                visitors.set(i, updatedVisitor);
                writeAllVisitors(visitors);
                return;
            }
        }
    }

    public void addNewVisitor(Visitor newVisitor) {
        List<Visitor> visitors = getAllVisitors();
        int newId = visitors.isEmpty() ? 1 : visitors.get(visitors.size() - 1).getId() + 1;
        newVisitor.setId(newId);
        visitors.add(newVisitor);
        writeAllVisitors(visitors);
    }

    private void writeAllVisitors(List<Visitor> visitors) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADER))) {

            for (Visitor visitor : visitors) {
                csvPrinter.printRecord(visitor.getId(), visitor.getName(), visitor.getSurname(), visitor.getPhone(), visitor.getEmail());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Errore nella scrittura del file: " + e.getMessage());
        }
    }

    private Visitor parseRecord(CSVRecord record) {
       int id = Integer.parseInt(record.get("id"));
        String name = record.get("name");
        String surname = record.get("surname");
        String phone = record.get("phone");
        String email = record.get("email");
        return new Visitor(id, name, surname, phone, email);
    }
}

