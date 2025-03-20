package it.itsincom.webdevd.resources;

import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.models.enums.Status;
import it.itsincom.webdevd.repositories.VisitRepository;
import it.itsincom.webdevd.services.DepartmentService;
import it.itsincom.webdevd.services.BadgeService;
import it.itsincom.webdevd.services.SessionService;
import it.itsincom.webdevd.services.VisitService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Path("/reception")
public class ReceptionResource {
    private final Template reception;
    private final DepartmentService departmentService;
    private final VisitService visitService;
    private final VisitRepository visitRepository;
    private final BadgeService badgeService;

    public ReceptionResource(Template reception,
                             DepartmentService departmentService,
                             VisitService visitService,
                             VisitRepository visitRepository,
                             BadgeService badgeService) {
        this.reception = reception;
        this.departmentService = departmentService;
        this.visitService = visitService;
        this.visitRepository = visitRepository;
        this.badgeService = badgeService;
    }

    @GET
    public Response getReceptionPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId,
                                     @QueryParam("date") String dateStr) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        LocalDate date = departmentService.getDate(dateStr);
        TemplateInstance page = getReceptionTemplate(date, null, null);
        return Response.ok(page).build();
    }

    @POST
    @Path("/assign-badge")
    public Response assignBadge(@FormParam("visit-id") int visitId,
                                @CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId,
                                @QueryParam("date") String dateStr) {
        String assignmentResult = visitService.assignBadge(visitId);
        if (assignmentResult.equals(VisitService.OPERATION_SUCCESS)) {
            return Response.seeOther(URI.create("/reception")).build();
        }
        return buildErrorResponse(sessionId, dateStr, assignmentResult, null);
    }

    @POST
    @Path("/end-visit")
    public Response endVisit(@FormParam("visit-id") int visitId,
                             @CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId,
                             @QueryParam("date") String dateStr) {
        String endingResult = visitService.endVisit(visitId);
        if (endingResult.equals(VisitService.OPERATION_SUCCESS)) {
            return Response.seeOther(URI.create("/reception")).build();
        }
        return buildErrorResponse(sessionId, dateStr, null, endingResult);
    }

    private TemplateInstance getReceptionTemplate(LocalDate date, String assignmentError, String endingResult) {
        Map<String, Integer> badgeStats = badgeService.getBadgeStats();
        List<Visit> allVisits = visitRepository.getAllVisits();
        List<Visit> visits = visitService.getVisitsByDate(date, visitService.enrichVisitsWithNames(allVisits));
        visits.sort(Comparator.comparing(Visit::getStart));
        return reception
                .data("badge-stats", badgeStats)
                .data("selected-date", date)
                .data("assignment-error", assignmentError)
                .data("ending-error", endingResult)
                .data("visits", visits)
                .data("pending", Status.IN_ATTESA)
                .data("in-progress", Status.IN_CORSO);
    }

    private Response buildErrorResponse(String sessionId, String dateStr, String assignmentError, String endingError) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        LocalDate date = departmentService.getDate(dateStr);
        TemplateInstance page = getReceptionTemplate(date, assignmentError, endingError);
        return Response.status(500).entity(page).build();
    }
}
