package it.itsincom.webdevd.repositories;

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
public class BadgeRepository {

    private static final String FILE_PATH = "data/badges.csv";
    private static final String[] HEADER = {"code", "is_available"};

    private static final CSVFormat CSV_FORMAT_READ = CSVFormat.Builder.create()
            .setHeader(HEADER)
            .setSkipHeaderRecord(true)
            .get();

    public int countAvailableBadges() {
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
             CSVParser parser = CSVParser.parse(reader, CSV_FORMAT_READ)) {
            int availableBadges = 0;
            for (CSVRecord record : parser) {
                if (record.get("is_available").equals("1")) {
                    availableBadges++;
                }
            }
            return availableBadges;
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
}
