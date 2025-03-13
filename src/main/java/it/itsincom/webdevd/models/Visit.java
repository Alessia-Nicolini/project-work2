package it.itsincom.webdevd.models;

import it.itsincom.webdevd.models.enums.Status;
import java.time.LocalDateTime;

public class Visit {
    private final int id;
    private final String visitorName;
    private final String employeeName;
    private final LocalDateTime start;
    private final int expectedDuration;
    private LocalDateTime end;
    private String badgeCode;
    private Status status;

    public Visit(int id, String visitorName, String employeeName, LocalDateTime start, int expectedDuration, LocalDateTime end, String badgeCode, Status status) {
        this.id = id;
        this.visitorName = visitorName;
        this.employeeName = employeeName;
        this.start = start;
        this.expectedDuration = expectedDuration;
        this.end = end;
        this.badgeCode = badgeCode;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public int getExpectedDuration() {
        return expectedDuration;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setAEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getBadgeCode() {
        return badgeCode;
    }

    public void setBadgeCode(String badgeCode) {
        this.badgeCode = badgeCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
