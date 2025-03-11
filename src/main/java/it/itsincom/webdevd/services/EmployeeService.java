package it.itsincom.webdevd.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


@ApplicationScoped
public class EmployeeService {

    private static final String FILE_PATH = "data/employees.csv";


    public String[] authenticate(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return new String[]{"400", "Per favore, inserisci sia l'email che la password."};
        }

        try (Reader reader = new FileReader(FILE_PATH)) {
            CSVFormat format = CSVFormat.Builder.create()
                    .setHeader()
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .get();
            CSVParser parser = CSVParser.parse(reader, format);

            for (CSVRecord record : parser) {
                String storedEmail = record.get("email");
                String storedPassword = record.get("password");
                String department = record.get("department");

                if (storedEmail.equals(email) && storedPassword.equals(password)) {
                    return new String[]{"200", department};
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new String[]{"500", "Errore interno del server."};
        }

        return new String[]{"401", "Email o password errati. Riprova."};
    }
}
