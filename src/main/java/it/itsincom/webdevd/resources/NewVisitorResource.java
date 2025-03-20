package it.itsincom.webdevd.resources;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.services.DepartmentService;
import it.itsincom.webdevd.services.NewVisitorService;
import it.itsincom.webdevd.services.SessionService;
import it.itsincom.webdevd.services.VisitService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("newVisitor")
public class NewVisitorResource {
    private static final String FILE_PATH = "data/visitors.csv";

    private final Template newVisitor;
    private final DepartmentService departmentService;
    private final NewVisitorService newVisitorService;

    public NewVisitorResource(Template newVisitor, DepartmentService departmentService, NewVisitorService newVisitorService) {
        this.newVisitor = newVisitor;
        this.departmentService = departmentService;
        this.newVisitorService = newVisitorService;
    }

    @GET
    public Response showRegistrationPage(@CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }
        TemplateInstance page = getNewVisitorTemplate(sessionId, null, null);
        return Response.ok(page).build();
    }

    @POST
    public Response processRegistration(
            @FormParam("first-name") String firstName,
            @FormParam("last-name") String lastName,
            @FormParam("phone") String phone,
            @FormParam("email") String email,
            @CookieParam(SessionService.SESSION_COOKIE_NAME) String sessionId) {
        Response response = departmentService.checkSession(sessionId);
        if (response != null) {
            return response;
        }

        String result = newVisitorService.addNewVisitor(firstName, lastName, phone, email);
        if (result.equals(VisitService.OPERATION_SUCCESS)) {
            return buildResponse(sessionId, "Visitatore aggiunto con successo!", null);
        }
        return buildResponse(sessionId, null, result);
    }

    private Response buildResponse(String sessionId, String success, String error) {
        TemplateInstance page = getNewVisitorTemplate(sessionId, success, error);
        if (error == null) {
            return Response.ok().entity(page).build();
        }
        return Response.status(409).entity(page).build();
    }

    private TemplateInstance getNewVisitorTemplate(String sessionId, String success, String error) {
        return newVisitor.data("success", success).data("error", error);
    }
}
