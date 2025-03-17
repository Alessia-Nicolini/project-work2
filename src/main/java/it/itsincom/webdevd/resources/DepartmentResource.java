package it.itsincom.webdevd.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.services.AllDepartmentsService;
import it.itsincom.webdevd.services.SessionService;
import it.itsincom.webdevd.services.VisitService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;

@Path("/department")
public class DepartmentResource {

    private final Template department;
    private final VisitService visitService;
    private final AllDepartmentsService allDepartmentsService;
    private final SessionService sessionService;

    public DepartmentResource(Template department, VisitService visitService, AllDepartmentsService allDepartmentsService, SessionService sessionService) {
        this.department = department;
        this.visitService = visitService;
        this.allDepartmentsService = allDepartmentsService;
        this.sessionService = sessionService;
    }

    @GET
    public Response getDepartmentPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId, @QueryParam("date") String dateStr) {
        Response response = allDepartmentsService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        LocalDate date = allDepartmentsService.getDate(dateStr);
        TemplateInstance page = getDepartmentTemplate(date, sessionId);
        return Response.ok(page).build();
    }

    private TemplateInstance getDepartmentTemplate(LocalDate date, String sessionId) {
        List<Visit> visitsByDate = visitService.getVisitsByDate(date);
        int employeeId = sessionService.getEmployeeFromSession(sessionId).getId();
        List<Visit> visits = visitService.getVisitsByEmployeeId(visitsByDate, employeeId);
        return department
                .data("visits", visits)
                .data("selected-date", date);
    }
}