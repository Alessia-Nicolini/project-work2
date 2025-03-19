package it.itsincom.webdevd.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.Visitor;
import it.itsincom.webdevd.repositories.VisitorRepository;
import it.itsincom.webdevd.services.DepartmentService;
import it.itsincom.webdevd.services.EmployeeService;
import it.itsincom.webdevd.services.SessionService;
import it.itsincom.webdevd.services.VisitService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("newVisit")
public class NewVisitResource {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Template newVisit;
    private final DepartmentService departmentService;
    private final VisitorRepository visitorRepository;
    private final SessionService sessionService;
    private final EmployeeService employeeService;
    private final VisitService visitService;

    public NewVisitResource(Template newVisit,
                            DepartmentService departmentService,
                            VisitorRepository visitorRepository,
                            SessionService sessionService,
                            EmployeeService employeeService,
                            VisitService visitService) {
        this.newVisit = newVisit;
        this.departmentService = departmentService;
        this.visitorRepository = visitorRepository;
        this.sessionService = sessionService;
        this.employeeService = employeeService;
        this.visitService = visitService;
    }

    @GET
    public Response showNewVisitPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        TemplateInstance page = getNewVisitTemplate(sessionId, null,null);
        return Response.ok(page).build();
    }

    @POST
    public Response processRegistration(@FormParam("visitor-id") String visitorId,
                                        @FormParam("employee-id") String employeeId,
                                        @FormParam("start") String start,
                                        @FormParam("expected-duration") String expectedDuration,
                                        @CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
               return response;
        }
        String result = visitService.addVisit(Integer.parseInt(visitorId),Integer.parseInt(employeeId), LocalDateTime.parse(start, DATE_TIME_FORMATTER),Integer.parseInt(expectedDuration));
        if(result.equals(VisitService.OPERATION_SUCCESS)){
            return buildResponse(sessionId, "Visita aggiunta con successo.", null);
        }
        return buildResponse(sessionId, null, result);
    }

    private Response buildResponse(String sessionId, String success, String error) {
        TemplateInstance page = getNewVisitTemplate(sessionId, success, error);
        if(error == null){
            return Response.ok().entity(page).build();
        }

        return Response.status(409).entity(page).build();
    }



    private TemplateInstance getNewVisitTemplate(String sessionId,String success, String error) {
        List<Visitor> visitors = visitorRepository.getAllVisitors();
        Employee loggedEmployee = sessionService.getEmployeeFromSession(sessionId);
        List<Employee> employees = employeeService.getNoReceptionEmployees();
        return newVisit
                .data("visitors", visitors)
                .data("logged-employee", loggedEmployee)
                .data("employees", employees)
                .data("success", success)
                .data("error", error);
    }
}
