package it.itsincom.webdevd.services;

import it.itsincom.webdevd.repositories.VisitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class NewVisitService {
    public static final String OPERATION_SUCCESS = "Success";

    private final VisitRepository visitRepository;

    public NewVisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

//    public String registerUser(String email) throws IOException {
//        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_VISITOR), StandardCharsets.UTF_8);
//             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
//            for (CSVRecord record : csvParser) {
//                if (record.get(3).equals(email)) {
//                    visitRepository.addVisit();
//                    return OPERATION_SUCCESS;
//                }
//            }
//            return "Errore durante la lettura del file.";
//        }
//    }
}
