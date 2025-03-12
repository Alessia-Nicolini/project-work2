package it.itsincom.webdevd.web;

import io.quarkus.qute.Template;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;



@Path("new-visit")
public class VisitResource {
    private static final String FILE_PATH = "data/visit.csv";

    private final Template visit;

    public VisitResource(Template visit) {
        this.visit = visit;
    }

    @GET
    public Response showVisit() {
        return Response.ok(visit.render()).build();
    }

    @POST
    public Response writeVisit(@FormParam("mail") String mail,
                               @FormParam("date") LocalDate date,
                               @FormParam("start") LocalTime start,
                               @FormParam("duration") int duration
    ) {


        String FILE_PATH = "data/visit.csv";
        LocalTime end = start.plusMinutes(duration);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(STR."\{mail},\{date},\{start},\{end}");
            writer.newLine();
            return Response.ok("Dati salvati correttamente!").build();
        } catch (IOException e) {
            return Response.status(500).entity("Errore nel salvataggio dei dati").build();
        }
    }

}




