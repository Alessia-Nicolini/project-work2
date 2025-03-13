package it.itsincom.webdevd.models;

import it.itsincom.webdevd.models.states.Status;

import java.time.LocalDate;
import java.time.LocalTime;


public class Visit {
    private final Long id;
    private final String visitorName;
    private final String employeeName;
    private final LocalDate date;
    private final LocalTime startTime;
    private final int expectedDuration;
    private String badgeCode;
    private LocalTime actualEndTime;
    private Status status;

    public Visit(Long id, String visitorName, String employeeName, LocalDate date, LocalTime startTime, int expectedDuration, String badgeCode, LocalTime actualEndTime, Status status) {
        this.id = id;
        this.visitorName = visitorName;
        this.employeeName = employeeName;
        this.date = date;
        this.startTime = startTime;
        this.expectedDuration = expectedDuration;
        this.badgeCode = badgeCode;
        this.actualEndTime = actualEndTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public int getExpectedDuration() {
        return expectedDuration;
    }

    public String getBadgeCode() {
        return badgeCode;
    }
    public void setBadgeCode(String badgeCode) {
        this.badgeCode = badgeCode;
    }

    public LocalTime getActualEndTime() {
        return actualEndTime;
    }
    public void setActualEndTime(LocalTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
