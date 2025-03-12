package it.itsincom.webdevd.web;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/new-visitor")
public class NewVisitorResource {

    private static final String FILE_PATH = "data/visitors.csv";

    private final Template newVisitor;

    public NewVisitorResource(Template newVisitor) {
        this.newVisitor = newVisitor;
    }

    @GET
    public TemplateInstance showRegistrationPage() {
        return newVisitor.data("message", null);
    }

    @POST
    public Response processRegistration(
            @FormParam("nome") String nome,
            @FormParam("cognome") String cognome,
            @FormParam("telefono") String telefono,
            @FormParam("email") String email) {

        String messaggioErrore = null;

        if (nome == null || nome.isEmpty() || cognome == null || cognome.isEmpty() ||
                telefono == null || telefono.isEmpty() || email == null || email.isEmpty()) {
            messaggioErrore = "Tutti i campi sono obbligatori.";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(newVisitor.data("message", messaggioErrore))
                    .build();
        }

        if (isVisitorAlreadyRegistered(nome, cognome, telefono, email)) {
            messaggioErrore = "Il visitatore è già registrato!";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(newVisitor.data("message", messaggioErrore))
                    .build();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(nome + "," + cognome + "," + telefono + "," + email);
            writer.newLine();
        } catch (IOException e) {
            return Response.serverError().entity("Errore nel salvataggio.").build();
        }

        return Response.seeOther(URI.create("/department.html")).build();

    }

    private boolean isVisitorAlreadyRegistered(String nome, String cognome, String telefono, String email) {
        if (!Files.exists(Paths.get(FILE_PATH))) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dati = line.split(",");
                if (dati.length == 4) {
                    String storedNome = dati[0].trim();
                    String storedCognome = dati[1].trim();
                    String storedTelefono = dati[2].trim();
                    String storedEmail = dati[3].trim();

                    if (storedNome.equalsIgnoreCase(nome) &&
                            storedCognome.equalsIgnoreCase(cognome) &&
                            storedTelefono.equals(telefono) &&
                            storedEmail.equals(email)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
