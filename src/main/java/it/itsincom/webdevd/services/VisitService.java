package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.enums.Department;
import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.BadgeRepository;
import it.itsincom.webdevd.repositories.EmployeeRepository;
import it.itsincom.webdevd.repositories.VisitRepository;
import it.itsincom.webdevd.repositories.VisitorRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VisitService {
    public static final String OPERATION_SUCCESS = "Success";
    public static final LocalTime MAX_TIME = LocalTime.of(16, 0);
    public static final LocalTime MIN_TIME = LocalTime.of(9, 0);
    public static final int MAX_DURATION = 120;
    public static final int MIN_DURATION = 15;
    private static final int BADGE_MAX_LATE = 20;

    private final VisitRepository visitRepository;
    private final BadgeRepository badgeRepository;
    private final VisitorRepository visitorRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public VisitService(VisitorRepository visitorRepository,
                        BadgeRepository badgeRepository,
                        VisitRepository visitRepository,
                        EmployeeService employeeService,
                        EmployeeRepository employeeRepository) {
        this.visitRepository = visitRepository;
        this.badgeRepository = badgeRepository;
        this.visitorRepository = visitorRepository;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
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

    public List<Visit> enrichVisitsWithNames(List<Visit> visits) {
        return visits.stream().peek(visit -> {
            String visitorName = visitorRepository.getNameById(visit.getVisitorId());
            String employeeName = employeeService.getNameById(visit.getEmployeeId());
            visit.setVisitorName(visitorName);
            visit.setEmployeeName(employeeName);
        }).collect(Collectors.toList());
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
        if (visitorRepository.getVisitorById(visitorId) == null) {
            return "Il visitatore non esiste.";
        }
        Employee employee = employeeRepository.getEmployeeById(employeeId);
        if (employee == null) {
            return "Il dipendente non esiste.";
        }
        Department department = employee.getDepartment();
        if (Department.PORTINERIA.equals(department)) {
            return "Il dipendente non può essere del dipartimento PORTINERIA.";
        }
        LocalDate startDate = start.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        if (!LocalDate.now().isBefore(startDate)) {
            return "La visita deve essere programmata almeno per il giorno successivo.";
        }
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return "Il giorno della visita deve essere tra Lunedì e Venerdì";
        }
        if (startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME)) {
            return "L'orario della visita deve essere tra " + MIN_TIME + " e " + MAX_TIME + ".";
        }
        if (expectedDuration < MIN_DURATION || expectedDuration > MAX_DURATION) {
            return "La durata della visita deve essere tra " + MIN_DURATION + " e " + MAX_DURATION + " minuti.";
        }
        int visitId = visitRepository.getLastVisitId() + 1;
        Visit newVisit = new Visit(visitId, visitorId, employeeId, start, expectedDuration, null, null, Status.IN_ATTESA);
        visitRepository.addVisit(newVisit);
        return OPERATION_SUCCESS;
    }
    public boolean deleteVisitById(int visitId) {
        List<Visit> visits = visitRepository.getAllVisits();
        boolean removed = visits.removeIf(v -> v.getId() == visitId);

        if (removed) {
            visitRepository.writeAllVisits(visits);
        }

        return removed;
    }
}
