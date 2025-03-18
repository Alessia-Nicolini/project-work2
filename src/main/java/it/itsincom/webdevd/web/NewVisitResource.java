package it.itsincom.webdevd.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.NewVisitRepository;
import it.itsincom.webdevd.repositories.VisitRepository;
import it.itsincom.webdevd.services.NewVisitService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.itsincom.webdevd.services.NewVisitService.OPERATION_SUCCESS;

@Path("newVisit")
public class NewVisitResource {

    private final Template newVisit;
    private final NewVisitService newVisitService;
    private final NewVisitRepository newVisitRepository;

    public NewVisitResource(Template newVisit, NewVisitService newVisitService, NewVisitRepository newVisitRepository) {
        this.newVisit = newVisit;
        this.newVisitService = newVisitService;
        this.newVisitRepository = newVisitRepository;
    }

    @GET
    public TemplateInstance showVisit() {
        return newVisit.instance();
    }


    @POST
    public Response saveVisit(@QueryParam("email") String email,
                              @QueryParam("start") LocalDateTime start,
                              @QueryParam("answer") String answer,
                              Visit newVisit)
            throws IOException {
        if (!OPERATION_SUCCESS.equals(answer)) {
            return Response.status(400).entity("L'utente non è registrato").build();
        }
        LocalDate today = LocalDate.now();
        if (start.toLocalDate().isAfter(today)) {
            newVisitService.registerUser(email);

            List<Visit> visits = new ArrayList<>();
            visits.add(newVisit);
            newVisitRepository.writeVisit( visits );

            return Response.seeOther(URI.create("/department.html")).build();
        } else {
            return Response.status(400).entity("Prenotare con almeno un giorno di anticipo").build();
        }
    }
}








