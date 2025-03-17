package it.itsincom.webdevd.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.models.Visit;
import it.itsincom.webdevd.repositories.NewVisitRepository;
import it.itsincom.webdevd.repositories.VisitRepository;
import it.itsincom.webdevd.services.NewVisitService;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Path("newVisit")
public class NewVisitResource {

    private final Template newVisit;
    private final NewVisitService newVisitService;

    public NewVisitResource(Template newVisit, NewVisitService newVisitService) {
        this.newVisit = newVisit;
        this.newVisitService = newVisitService;
    }

    @GET
    public TemplateInstance showVisit() {
        return newVisit.instance();
    }

    @POST
    public Response saveVisit(@FormParam("email") String email,
                              @FormParam("date") LocalDate date,
                              @FormParam("start") LocalTime start,
                              @FormParam("duration") int duration) throws IOException {

//        newVisitService.registerUser(email);
//        if (registerUser(email)) {
//            LocalDate today = LocalDate.now();
//            if (today.isBefore(date)) {
//                NewVisitRepository.writeVisit(List<Visit> visits);
//                return Response.seeOther(URI.create("/department.html")).build();
//            } else {
//                return Response.status(400).entity("Prenotare con almeno un giorno di anticipo").build();
//            }
//        }
//
//        return Response.status(400).entity("L'utente non è registrato").build();
        return null;
    }

}








