package it.itsincom.webdevd.resources;

import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.services.BadgeService;
import it.itsincom.webdevd.services.SessionService;
import it.itsincom.webdevd.services.VisitService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Path("/reception")
public class ReceptionResource {

    private final Template reception;
    private final SessionService sessionService;
    private final VisitService visitService;
    private final BadgeService badgeService;

    public ReceptionResource(Template reception, SessionService sessionService, VisitService visitService, BadgeService badgeService) {
        this.reception = reception;
        this.sessionService = sessionService;
        this.visitService = visitService;
        this.badgeService = badgeService;
    }

    @GET
    public Response getReceptionPage(@QueryParam("date") String dateStr, @CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Employee employee = sessionService.getEmployeeFromSession(sessionId);
        if (employee == null) {
            return Response.seeOther(URI.create("/login")).build();
        }
        LocalDate date = getDate(dateStr);
        List<Visit> visits = visitService.getVisitsByDate(date);
        Map<String, Integer> badgeStats = badgeService.getBadgeStats();
        TemplateInstance page = reception
                .data("employee", employee.getFirstName() + " " + employee.getLastName())
                .data("visits", visits)
                .data("badge-stats", badgeStats)
                .data("selected-date", date)
                .data("pending", Status.IN_ATTESA)
                .data("in-progress", Status.IN_CORSO);
        return Response.ok(page).build();
    }


    @POST
    @Path("/assign-badge")
    public Response assignBadge(@FormParam("visit-id") int visitId) {
        try {
            visitService.assignBadge(visitId);
            return Response.seeOther(URI.create("/reception")).build();
        }
        catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error assigning badge: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/end-visit")
    public Response endVisit(@FormParam("visit-id") int visitId) {
        try {
            visitService.endVisit(visitId);
            return Response.seeOther(URI.create("/reception")).build();
        }
        catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error ending visit: " + e.getMessage())
                    .build();
        }
    }

    private LocalDate getDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(dateStr);
        }
        catch (DateTimeParseException e) {
            return LocalDate.now();
        }
    }
}
