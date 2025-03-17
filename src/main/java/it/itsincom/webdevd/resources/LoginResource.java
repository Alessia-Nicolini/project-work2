package it.itsincom.webdevd.resources;

import io.quarkus.qute.Template;
import it.itsincom.webdevd.models.Employee;
import it.itsincom.webdevd.models.enums.Department;
import it.itsincom.webdevd.services.EmployeeService;
import it.itsincom.webdevd.services.SessionService;
import jakarta.ws.rs.*;
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
    public Response showLoginPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Employee employee = sessionService.getEmployeeFromSession(sessionId);
        if (employee != null) {
            String uri = getURI(employee);
            return Response.seeOther(URI.create(uri)).build();
        }
        return Response.ok(login.data("error-msg", null)).build();
    }

    @POST
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password) {
        String[] authResult = employeeService.authenticate(email, password);
        if (authResult[0].equals("200")) {
            Employee employee = employeeService.getEmployeeFromStringArray(authResult[1].split(","));
            String uri = getURI(employee);
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

    private String getURI(Employee employee) {
        return (employee.getDepartment() == Department.PORTINERIA) ? "/reception" : "/department";
    }
}
