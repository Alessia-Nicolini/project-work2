package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.BadgeRepository;
import it.itsincom.webdevd.repositories.VisitRepository;
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

    public String addVisit(int visitorId, int employeeId, LocalDateTime start, int expectedDuration) {
        // TODO Controllare che l'id del visitatore esista.
        // TODO Controllare che l'id del dipendente esista.
        //  Attenzione: il dipendente non deve essere del dipartimento PORTINERIA.
        // TODO Salvare 'start' in due variabili:
        //   LocalDate startDate = start.toLocalDate();
        //   LocalTime startTime = start.toLocalTime();
        // TODO Controllare che 'startDate' sia un giorno dopo 'LocalDate.now()'.
        // TODO Controllare che 'startTime' sia compreso tra 'MAX_TIME' e 'MIN_TIME' (controllare costanti in alto).
        // TODO Controllare che 'expectedDuration' sia sia compreso tra 'MAX_DURATION' e 'MIN_DURATION'.

        // TODO Per ogni controllo fallito, ritornare una stringa d'errore, come nei metodi sopra: 'assignBadge()', 'endVisit()'.
        // TODO Se non ci sono errori, creare un oggetto Visit e metterlo al posto di null.
        //   Attenzione: l'id della visita deve essere l'id dell'ultima visita salvata su file + 1.
        visitRepository.addVisit(null);
        return OPERATION_SUCCESS;
    }
}
