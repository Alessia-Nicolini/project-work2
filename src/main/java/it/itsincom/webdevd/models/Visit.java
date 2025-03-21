package it.itsincom.webdevd.models;

import it.itsincom.webdevd.models.enums.Status;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Visit {
    private final int id;
    private final int visitorId;
    private final int employeeId;
    private final LocalDateTime start;
    private final int expectedDuration;
    private String end;
    private String badgeCode;
    private Status status;
    private String visitorName;
    private String employeeName;
    private final LocalTime expectedEndTime;

    public Visit(int id,
                 int visitorId,
                 int employeeId,
                 LocalDateTime start,
                 int expectedDuration,
                 String end,
                 String badgeCode,
                 Status status) {
        this.id = id;
        this.visitorId = visitorId;
        this.employeeId = employeeId;
        this.start = start;
        this.expectedDuration = expectedDuration;
        this.end = end;
        this.badgeCode = badgeCode;
        this.status = status;
        this.expectedEndTime = start.plusMinutes(expectedDuration).toLocalTime();
    }

    public int getId() {
        return id;
    }

    public int getVisitorId() {
        return visitorId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public int getExpectedDuration() {
        return expectedDuration;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalTime getExpectedEndTime() {
        return expectedEndTime;
    }
}
