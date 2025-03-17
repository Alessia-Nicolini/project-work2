package it.itsincom.webdevd.resources;

import it.itsincom.webdevd.services.SessionService;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/logout")
public class LogoutResource {

    private final SessionService sessionService;

    public LogoutResource(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GET
    public Response logout(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        sessionService.invalidateSession(sessionId);
        NewCookie deleteCookie = new NewCookie.Builder(SessionService.SESSION_COOKIE_NAME)
                .value("")
                .maxAge(0)
                .path("/")
                .build();
        return Response.seeOther(URI.create("/login"))
                .cookie(deleteCookie)
                .build();
    }
}
