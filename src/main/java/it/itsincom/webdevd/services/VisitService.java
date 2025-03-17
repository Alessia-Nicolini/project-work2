package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.BadgeRepository;
import it.itsincom.webdevd.repositories.VisitRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VisitService {
    public static final String OPERATION_SUCCESS = "Success";
    private static final int BADGE_MAX_LATE = 15;

    private final VisitRepository visitRepository;
    private final BadgeRepository badgeRepository;

    public VisitService(VisitRepository visitRepository, BadgeRepository badgeRepository) {
        this.visitRepository = visitRepository;
        this.badgeRepository = badgeRepository;
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
}
