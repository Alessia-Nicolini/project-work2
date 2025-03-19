package it.itsincom.webdevd.services;

import it.itsincom.webdevd.models.Employee;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@ApplicationScoped
public class SessionService {
    public static final String SESSION_COOKIE_NAME = "session";
    private static final int SESSION_MAX_AGE = 3600;

    private final Map<String, Employee> sessionsEmployees = new ConcurrentHashMap<>();

    public NewCookie createUserSession(Employee employee) {
        String sessionId = UUID.randomUUID().toString();
        sessionsEmployees.put(sessionId, employee);
        return new NewCookie.Builder(SESSION_COOKIE_NAME)
                .value(sessionId)
                .maxAge(SESSION_MAX_AGE)
                .httpOnly(true)
                .secure(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();
    }

    public Employee getEmployeeFromSession(String sessionId) {
        if (sessionId == null || !sessionsEmployees.containsKey(sessionId)) {
            return null;
        }
        return sessionsEmployees.get(sessionId);
    }

    public void invalidateSession(String sessionId) {
        sessionsEmployees.remove(sessionId);
    }
}
