package it.itsincom.webdevd.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Visit {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public String name;
    public String surname;
    public LocalDate date;
    public LocalTime start;
    public LocalTime end;

    public Visit(String name, String surname, LocalDate date, LocalTime start, LocalTime end) {
        this.name = name;
        this.surname = surname;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return name + "," + surname + "," + date.format(DATE_FORMATTER) + "," +
                start.format(TIME_FORMATTER) + "," + end.format(TIME_FORMATTER);
    }

    public static Visit fromCSV(String line) {
        String[] parts = line.split(",");

        try {

            LocalDate data = LocalDate.parse(parts[2].trim(), DATE_FORMATTER);
            LocalTime oraInizio = LocalTime.parse(parts[3].trim(), TIME_FORMATTER);
            LocalTime oraFine = LocalTime.parse(parts[4].trim(), TIME_FORMATTER);

            return new Visit(parts[0], parts[1], data, oraInizio, oraFine);
        } catch (DateTimeParseException e) {
            System.err.println("Errore nel parsing della data: " + parts[2]);
            e.printStackTrace();
            return null;
        }
    }


}