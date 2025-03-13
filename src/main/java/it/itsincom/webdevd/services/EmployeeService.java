package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.Employee;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


@ApplicationScoped
public class EmployeeService {

    private static final String FILE_PATH = "data/employees.csv";
    private static final String[] HEADER = {"id", "first_name", "last_name", "email", "password", "department"};

    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .setSkipHeaderRecord(true)
            .get();

    public String[] authenticate(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return new String[]{"400", "Per favore, inserisci sia l'email che la password."};
        }
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser parser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : parser) {
                Employee employee = getEmployeeFromStringArray(record.values());
                if (employee.getEmail().equals(email) && employee.getPassword().equals(password)) {
                    return new String[]{"200", employee.toString()};
                }
            }
        } catch (IOException e) {
            return new String[]{"500", "Errore interno del server."};
        }
        return new String[]{"401", "Email o password errati. Riprova."};
    }

    public Employee getEmployeeFromStringArray(String[] array) {
        return new Employee(
                array[0],
                array[1],
                array[2],
                array[3],
                array[4],
                array[5]
        );
    }
}
