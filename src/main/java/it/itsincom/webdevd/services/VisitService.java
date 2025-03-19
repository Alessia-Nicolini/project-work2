package it.itsincom.webdevd.services;


import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.Visitor;
import it.itsincom.webdevd.models.enums.Department;
import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.BadgeRepository;
import it.itsincom.webdevd.repositories.EmployeeRepository;
import it.itsincom.webdevd.repositories.VisitRepository;
import it.itsincom.webdevd.repositories.VisitorsRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VisitService {
    public static final String OPERATION_SUCCESS = "Success";
    public static final LocalTime MAX_TIME = LocalTime.of(16, 0);
    public static final LocalTime MIN_TIME = LocalTime.of(8, 0);
    public static final int MAX_DURATION = 120;
    public static final int MIN_DURATION = 15;
    private static final int BADGE_MAX_LATE = 15;

    private final VisitRepository visitRepository;
    private final BadgeRepository badgeRepository;
    private final VisitorsRepository visitorsRepository;
    private final EmployeeRepository employeeRepository;
    private final Employee employee;
    private final Visit visit;

    public VisitService(VisitRepository visitRepository, BadgeRepository badgeRepository, EmployeeRepository employeeRepository, VisitorsRepository visitorsRepository, Employee employee, Visit visit) {
        this.visitRepository = visitRepository;
        this.badgeRepository = badgeRepository;
        this.visitorsRepository = visitorsRepository;
        this.employeeRepository = employeeRepository;
        this.employee = employee;
        this.visit = visit;
    }

    public List<Visit> getVisitsByDate(LocalDate date, List<Visit> visits) {
        return visits.stream()
                .filter(v -> v.getStart().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Visit> getVisitsByEmployeeId(int employeeId, List<Visit> visits) {
        return visits.stream()
                .filter(v -> v.getEmployeeId() == employeeId)
                .collect(Collectors.toList());
    }

    public String assignBadge(int visitId) {
        Visit visit = visitRepository.getVisitById(visitId);
        if (visit == null) {
            return "Visita non valida.";
        }
        if (visit.getStatus() != Status.IN_ATTESA) {
            return "Il badge può essere assegnato solo a visite in attesa.";
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = visit.getStart();
        if (now.isBefore(startTime)) {
            return "Non puoi assegnare il badge prima dell'orario di inizio della visita.";
        }
        if (now.isAfter(startTime.plusMinutes(BADGE_MAX_LATE))) {
            return "Il periodo per l'assegnazione del badge è scaduto.";
        }
        String badgeCode = badgeRepository.assignFirstAvailableBadge();
        if (badgeCode == null) {
            return "Nessun badge disponibile.";
        }
        visit.setBadgeCode(badgeCode);
        visit.setStatus(Status.IN_CORSO);
        visitRepository.updateVisit(visit);
        return OPERATION_SUCCESS;
    }

    public String endVisit(int visitId) {
        Visit visit = visitRepository.getVisitById(visitId);
        if (visit == null) {
            return "Visita non valida.";
        }
        if (visit.getStatus() != Status.IN_CORSO) {
            return "Solo le visite in corso possono essere terminate.";
        }
        visit.setStatus(Status.COMPLETATO);
        visit.setAEnd(LocalDateTime.now());
        visitRepository.updateVisit(visit);
        if (visit.getBadgeCode() != null) {
            badgeRepository.releaseBadge(visit.getBadgeCode());
        }
        return OPERATION_SUCCESS;
    }


    public String addVisit(int visitorId, int employeeId, LocalDateTime start, int expectedDuration) {

        if (visitorsRepository.getVisitorById(visitorId) == null) {
            return "Il visitatore non esiste.";
        }
        if (employeeRepository.getEmployeeById(employeeId) == null) {
            return "Il dipendente non esiste.";
        }
        Department department = employee.getDepartment();
        if (Department.PORTINERIA.equals(department)) {
            return "Il dipendente non può essere del dipartimento PORTINERIA.";
        }
        LocalDate startDate = start.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        if (!LocalDate.now().isBefore(startDate)) {
            return "Errore: La visita deve essere programmata almeno per il giorno successivo.";
        }
        if (startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME)) {
            return "L'orario della visita deve essere tra " + MIN_TIME + " e " + MAX_TIME + ".";
        }
        if (expectedDuration < MIN_DURATION || expectedDuration > MAX_DURATION) {
            return "La durata della visita deve essere tra " + MIN_DURATION + " e " + MAX_DURATION + " minuti.";
        }
        int visitId = visit.getId() + 1;
        Visit newVisit = new Visit(visitId, visitorId, employeeId, start, expectedDuration, null, null, Status.IN_ATTESA);
        visitRepository.addVisit(newVisit);
        return OPERATION_SUCCESS;
    }
}

