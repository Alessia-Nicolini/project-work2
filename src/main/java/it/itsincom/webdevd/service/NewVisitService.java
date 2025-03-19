package it.itsincom.webdevd.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@ApplicationScoped
public class NewVisitService {

    private static final String FILE_PATH = "data/visitors.csv";

    public boolean registerUser(String email) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Divide la riga in base alla virgola

                if (parts.length > 3 && parts[3].equals(email)){
                    return true;}
        }
    }
        return false;
}
}
