package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.enums.Department;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    public List<Employee> getNoReceptionEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                if (!record.get("department").equals(Department.PORTINERIA.toString())) {
                    employees.add(parseRecord(record));
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return employees;
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

    private Employee parseRecord(CSVRecord record) {
        int id = Integer.parseInt(record.get("id"));
        String firstName = record.get("first_name");
        String lastName = record.get("last_name");
        String email = record.get("email");
        String password = record.get("password");
        String department = record.get("department");
        return new Employee(id, firstName, lastName, email, password, Department.valueOf(department));
    }
}
