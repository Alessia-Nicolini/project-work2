package it.itsincom.webdevd.repositories;

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
import java.util.Optional;
@ApplicationScoped
public class EmployeeRepository {
    private static final String FILE_PATH = "data/employees.csv";
    private static final String[] HEADER = {"id", "first_name", "last_name", "email", "password","deparment"};

    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .setSkipHeaderRecord(true)
            .get();


    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                employees.add(parseRecord(record));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return employees;
    }

    public Employee getEmployeeById(int id) {
        Optional<Employee> employee = getAllEmployees().stream().filter(v -> v.getId() == id).findFirst();
        return employee.orElse(null);
    }

    private Employee parseRecord(CSVRecord record) {
        int id = Integer.parseInt(record.get("id"));
        String firstName = record.get("first_name");
        String lastName = record.get("last_name");
        String email = record.get("email");
        String password = record.get("password");
        Department department = Department.valueOf(record.get("deparment"));
        return new Employee(id, firstName, lastName, email, password, department);
    }
}

