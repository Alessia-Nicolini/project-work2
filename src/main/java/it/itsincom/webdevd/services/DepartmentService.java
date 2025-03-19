package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.Employee;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@ApplicationScoped
public class DepartmentService {
    private final SessionService sessionService;

    public DepartmentService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public Response checkSession(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Employee employee = sessionService.getEmployeeFromSession(sessionId);
        if (employee == null) {
            return Response.seeOther(URI.create("/login")).build();
        }
        return null;
    }

    public LocalDate getDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return LocalDate.now();
        }
    }
}
