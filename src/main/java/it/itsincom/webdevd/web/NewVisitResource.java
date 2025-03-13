package it.itsincom.webdevd.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Path("newVisit")
public class NewVisitResource {

    private final Template newVisit;

    public NewVisitResource(Template newVisit) {
        this.newVisit = newVisit;
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
        if (exist(email)) {
            LocalDate today = LocalDate.now();
            if(today.isBefore(date)) {
                writeVisit(email, date, start, duration);
                return Response.seeOther(URI.create("/department.html")).build();
            }
            else {
                return Response.status(400).entity("Prenotare con almeno un giorno di anticipo").build();}
        }

        return Response.status(400).entity("L'utente non è registrato").build();
    }


    public void writeVisit(String mail, LocalDate date, LocalTime start, int duration) {
        String FILE_PATH = "data/visit.csv";
        LocalTime end = start.plusMinutes(duration);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(mail + "," + date + "," + start + "," + end);
            writer.newLine();
            Response.ok("Dati salvati correttamente!").build();
        } catch (IOException e) {
            Response.status(500).entity("Errore nel salvataggio dei dati").build();
        }
    }
    public boolean exist(String email) throws IOException {
        String FILE_PATH = "data/visitors.csv";
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[3].equals(email)) {
                return true;
            }
        }
        return false;
    }
}







