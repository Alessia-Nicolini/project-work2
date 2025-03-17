package it.itsincom.webdevd.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
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

    public DepartmentResource(Template department, VisitService visitService, AllDepartmentsService allDepartmentsService) {
        this.department = department;
        this.visitService = visitService;
        this.allDepartmentsService = allDepartmentsService;
    }

    @GET
    public Response getDepartmentPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId, @QueryParam("date") String dateStr) {
        Response response = allDepartmentsService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        LocalDate date = allDepartmentsService.getDate(dateStr);
        TemplateInstance page = getDepartmentTemplate(date);
        return Response.ok(page).build();
    }

    private TemplateInstance getDepartmentTemplate(LocalDate date) {
        List<Visit> visits = visitService.getVisitsByDate(date);
        return department
                .data("visits", visits)
                .data("selected-date", date);
    }
}