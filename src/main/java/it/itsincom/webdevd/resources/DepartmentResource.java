package it.itsincom.webdevd.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.VisitRepository;
import it.itsincom.webdevd.services.DepartmentService;
import it.itsincom.webdevd.services.SessionService;
import it.itsincom.webdevd.services.VisitService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Path("/department")
public class DepartmentResource {
    private final Template department;
    private final DepartmentService departmentService;
    private final VisitService visitService;
    private final VisitRepository visitRepository;
    private final SessionService sessionService;

    public DepartmentResource(Template department,
                              DepartmentService departmentService,
                              VisitService visitService,
                              VisitRepository visitRepository,
                              SessionService sessionService) {
        this.department = department;
        this.departmentService = departmentService;
        this.visitService = visitService;
        this.visitRepository = visitRepository;
        this.sessionService = sessionService;
    }

    @GET
    public Response getDepartmentPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId,
                                      @QueryParam("date") String dateStr) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        LocalDate date = departmentService.getDate(dateStr);
        TemplateInstance page = getDepartmentTemplate(date, sessionId);
        return Response.ok(page).build();
    }

    private TemplateInstance getDepartmentTemplate(LocalDate date, String sessionId) {
        List<Visit> visitsByDate = visitService.getVisitsByDate(date, visitRepository.getAllVisits());
        int employeeId = sessionService.getEmployeeFromSession(sessionId).getId();
        List<Visit> visits = visitService.getVisitsByEmployeeId(employeeId, visitService.enrichVisitsWithNames(visitsByDate));
        return department
                .data("selected-date", date)
                .data("visits", visits);
    }

    @POST
    @Path("/delete")
    public Response deleteVisit(@FormParam("visitId") int visitId,
                                @CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }

        boolean deleted = visitService.deleteVisitById(visitId);
        TemplateInstance page = getDepartmentTemplate(LocalDate.now(), sessionId);
        if (deleted) {
            return Response.seeOther(URI.create("/department")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}