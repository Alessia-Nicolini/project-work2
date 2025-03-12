package it.itsincom.webdevd.service;

import jakarta.enterprise.context.ApplicationScoped;
import it.itsincom.webdevd.model.Visit;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class DepartmentService {
    private static final String VISITE_FILE = "src/main/resources/visit.csv";

    public List<Visit> getVisiteByDate(LocalDate data) {
        List<Visit> visite = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(VISITE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Visit visit = Visit.fromCSV(line);


                if (visit != null && visit.date.equals(data)) {
                    visite.add(visit);
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        return visite;
    }

}
