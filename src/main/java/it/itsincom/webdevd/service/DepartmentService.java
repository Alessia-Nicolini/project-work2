package it.itsincom.webdevd.service;

import jakarta.enterprise.context.ApplicationScoped;
import it.itsincom.webdevd.model.Visita;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class DepartmentService {
    private static final String VISITE_FILE = "src/main/resources/visit.csv";

    public List<Visita> getVisiteByDate(LocalDate data) {
        List<Visita> visite = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(VISITE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Visita visita = Visita.fromCSV(line);


                if (visita != null && visita.date.equals(data)) {
                    visite.add(visita);
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        return visite;
    }

}
