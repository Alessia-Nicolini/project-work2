package it.itsincom.webdevd.web;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
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

    public NewVisitorResource(Template newVisitor) {
        this.newVisitor = newVisitor;
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

        String messaggioErrore = null;

        if (name == null || name.isEmpty() || surname == null || surname.isEmpty() ||
                phone == null || phone.isEmpty() || email == null || email.isEmpty()) {
            messaggioErrore = "Tutti i campi sono obbligatori.";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(newVisitor.data("message", messaggioErrore))
                    .build();
        }

        if (isVisitorAlreadyRegistered(name, surname, phone, email)) {
            messaggioErrore = "Il visitatore è già registrato!";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(newVisitor.data("message", messaggioErrore))
                    .build();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(name + "," + surname + "," + phone + "," + email);
            writer.newLine();
        } catch (IOException e) {
            return Response.serverError().entity("Errore nel salvataggio.").build();
        }

        return Response.seeOther(URI.create("/department")).build();

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
