package it.itsincom.webdevd.service;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("visita")
public class VisitResource {
    private static final String FILE_PATH = "data/visit.csv";

    private final Template visit;

    public VisitResource(Template visit) {
        this.visit = visit;
    }

    @GET
    public Response showVisit() {return Response.ok(visit.render()).build();}
}
