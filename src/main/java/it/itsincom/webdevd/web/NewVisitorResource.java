package it.itsincom.webdevd.web;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.services.NewVisitorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("newVisitor")
public class NewVisitorResource {

    private static final String FILE_PATH = "data/visitors.csv";

    private final Template newVisitor;
    private final NewVisitorService newVisitorService;

    public NewVisitorResource(Template newVisitor, NewVisitorService newVisitorService) {
        this.newVisitor = newVisitor;
        this.newVisitorService = newVisitorService;
    }

    @GET
    public TemplateInstance showRegistrationPage() {
        return newVisitor.data("message", null);
    }

    @POST
    public Response processRegistration(
            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("phone") String phone,
            @FormParam("email") String email) {

        String result = newVisitorService.addNewVisitor(name, surname, phone, email);

        return Response.seeOther(URI.create("/department")).build();
    }
}


