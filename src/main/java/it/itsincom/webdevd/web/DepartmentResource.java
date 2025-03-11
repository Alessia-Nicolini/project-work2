package it.itsincom.webdevd.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("department")
public class DepartmentResource {

    private final Template department;

    public DepartmentResource(Template department) {
        this.department = department;
    }

    @GET
    public TemplateInstance showDepartmentPage() {
        return department.instance();
    }
}
