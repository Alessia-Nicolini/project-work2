package it.itsincom.webdevd.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.service.DepartmentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Path("department")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class DepartmentResource {

    private final Template department;

    @Inject
    DepartmentService departmentService;

    public DepartmentResource(Template department) {
        this.department = department;
    }

    @GET
    public TemplateInstance showDepartmentPage(@QueryParam("data") String data) {
        LocalDate date = (data != null && !data.isEmpty()) ? LocalDate.parse(data) : LocalDate.now();
        List<Visita> visite = departmentService.getVisiteByDate(date);
        return department.data("visite", visite, "dataSelezionata", date);
    }
}