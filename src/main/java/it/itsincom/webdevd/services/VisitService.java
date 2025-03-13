package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.VisitRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public List<Visit> getVisitsByDate(LocalDate date) {
        return visitRepository.getAllVisits()
                .stream()
                .filter(v -> v.getStart().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public void assignBadge(int visitId) {
        Visit visit = visitRepository
                .getVisitById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit with id " + visitId + " not found."));
        if (visit.getStatus() != Status.IN_ATTESA) {
            throw new IllegalStateException("Badge can only be assigned to pending visits.");
        }
        //visit.setBadgeCode(badgeCode);
        visit.setStatus(Status.IN_CORSO);
        visitRepository.updateVisit(visit);
    }

    public void endVisit(int visitId) {
        Visit visit = visitRepository.getVisitById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit with id " + visitId + " not found."));
        if (visit.getStatus() != Status.IN_CORSO) {
            throw new IllegalStateException("Only visits in progress can be ended.");
        }
        visit.setStatus(Status.COMPLETATO);
        visit.setAEnd(LocalDateTime.now());
        visitRepository.updateVisit(visit);
    }
}
