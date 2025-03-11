package it.itsincom.webdevd.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@ApplicationScoped
public class UserSession {

    private static final String SESSION_COOKIE_NAME = "session";

    private final Map<String, String> userSessions = new ConcurrentHashMap<>();


    public NewCookie createUserSession(String email) {
        String sessionId = UUID.randomUUID().toString();
        userSessions.put(sessionId, email);
        return new NewCookie.Builder(SESSION_COOKIE_NAME).value(sessionId).build();
    }


    public String getUserFromSession(String sessionId) {
        return userSessions.get(sessionId);
    }


    public void removeUserFromSession(String sessionId) {
        userSessions.remove(sessionId);
    }
}
