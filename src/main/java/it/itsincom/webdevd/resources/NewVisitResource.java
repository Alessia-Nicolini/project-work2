package it.itsincom.webdevd.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.Visitor;
import it.itsincom.webdevd.repositories.VisitorsRepository;
import it.itsincom.webdevd.services.DepartmentService;
import it.itsincom.webdevd.services.EmployeeService;
import it.itsincom.webdevd.services.SessionService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("newVisit")
public class NewVisitResource {
    private final Template newVisit;
    private final DepartmentService departmentService;
    private final VisitorsRepository visitorsRepository;
    private final SessionService sessionService;
    private final EmployeeService employeeService;

    public NewVisitResource(Template newVisit,
                            DepartmentService departmentService,
                            VisitorsRepository visitorsRepository,
                            SessionService sessionService,
                            EmployeeService employeeService) {
        this.newVisit = newVisit;
        this.departmentService = departmentService;
        this.visitorsRepository = visitorsRepository;
        this.sessionService = sessionService;
        this.employeeService = employeeService;
    }

    @GET
    public Response showNewVisitPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        TemplateInstance page = getNewVisitTemplate(sessionId);
        return Response.ok(page).build();
    }

    @POST
    public Response processRegistration(@FormParam("visitor-id") String visitorId,
                                        @FormParam("employee-id") String employeeId,
                                        @FormParam("start") String start,
                                        @FormParam("expected-duration") String expectedDuration) {

        /*String FILE_PATH = "data/visits.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(mail + "," + date + "," + start + "," + end);
            writer.newLine();
            return Response.seeOther(URI.create("department")).entity(newVisit.data("visitConfermation", "Visita Salvata")).build();
        } catch (IOException e) {
            return Response.status(500).entity("Errore nel salvataggio dei dati").build();
        }*/
        return null;
    }

    private TemplateInstance getNewVisitTemplate(String sessionId) {
        List<Visitor> visitors = visitorsRepository.getAllVisitors();
        Employee loggedEmployee = sessionService.getEmployeeFromSession(sessionId);
        List<Employee> employees = employeeService.getNoReceptionEmployees();
        return newVisit
                .data("visitors", visitors)
                .data("logged-employee", loggedEmployee)
                .data("employees", employees);
    }
}





