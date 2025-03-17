package it.itsincom.webdevd.services;

import it.itsincom.webdevd.repositories.VisitRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;



public class NewVisitService {

    public static final String OPERATION_SUCCESS="success";
    private static final String CSV_FILE_VISITOR = "data/visitor.csv";
    private static final String[] HEADER_VISITOR = {"id", "nome", "cognome", "telefono", "email"};

    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER_VISITOR)
            .setSkipHeaderRecord(true)
            .get();

    private static final Logger logger = Logger.getLogger(VisitRepository.class);

    public String registerUser(String email) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_VISITOR), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            for (CSVRecord record : csvParser) {
                if (record.get(3).equals(email)) {
                    return OPERATION_SUCCESS;
                }
            }
            return "Errore durante la lettura del file.";
        }
    }
}
