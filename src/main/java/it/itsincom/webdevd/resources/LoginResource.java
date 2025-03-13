package it.itsincom.webdevd.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.enums.Department;
import it.itsincom.webdevd.services.EmployeeService;
import it.itsincom.webdevd.services.SessionService;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/login")
public class LoginResource {

    private final Template login;
    private final EmployeeService employeeService;
    private final SessionService sessionService;

    public LoginResource(Template login, SessionService sessionService, EmployeeService employeeService) {
        this.login = login;
        this.sessionService = sessionService;
        this.employeeService = employeeService;
    }

    @GET
    public TemplateInstance showLoginPage() {
        return login.data("error-msg", null);
    }

    @POST
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password) {
        String[] authResult = employeeService.authenticate(email, password);
        if (authResult[0].equals("200")) {
            String uri = "/department";
            Employee employee = employeeService.getEmployeeFromStringArray(authResult[1].split(","));
            if (employee.getDepartment() == Department.PORTINERIA) {
                uri = "/reception";
            }
            NewCookie sessionCookie = sessionService.createUserSession(employee);
            return Response
                    .seeOther(URI.create(uri))
                    .cookie(sessionCookie)
                    .build();
        }
        return Response
                .status(Integer.parseInt(authResult[0]))
                .entity(login.data("error-msg", authResult[1]))
                .build();
    }
}
