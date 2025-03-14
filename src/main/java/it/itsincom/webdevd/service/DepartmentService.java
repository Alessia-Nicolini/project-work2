package it.itsincom.webdevd.service;

import it.itsincom.webdevd.repositories.VisitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import it.itsincom.webdevd.model.Visit;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class DepartmentService {
    private static final String VISITE_FILE = "src/main/resources/visit.csv";
    private static final String[] HEADER_DEPARTMENT = {"id", "nome", "cognome", "telefono", "email"};
    private static final Logger logger = Logger.getLogger(VisitRepository.class);

    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER_DEPARTMENT)
            .setSkipHeaderRecord(true)
            .get();

    public List<Visit> getVisitsByDate(LocalDate data) {
        List<Visit> visits = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get(VISITE_FILE), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                Visit visit = Visit.fromCSV(String.valueOf(record));

                if (visit != null && visit.date.equals(data)) {
                    visits.add(visit);
                }
            }
        }  catch (IOException e) {
            logger.error("Error reading CSV file: " + e.getMessage(), e);
        }
        return visits;
    }

}
