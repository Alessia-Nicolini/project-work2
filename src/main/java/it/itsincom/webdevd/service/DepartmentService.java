package it.itsincom.webdevd.service;

import jakarta.enterprise.context.ApplicationScoped;
import it.itsincom.webdevd.model.Visita;
import java.time.format.DateTimeFormatter;


@ApplicationScoped
public class DepartmentService {
    private static final String VISITE_FILE = "src/main/resources/visit.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");



}
