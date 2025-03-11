package it.itsincom.webdevd.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.service.UserSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;


@Path("login")
public class LoginResource {

    private static final String FILE_PATH = "data/employees.csv";

    private final Template login;
    private final UserSession userSessionService;


    public LoginResource(Template login, UserSession userSessionService) {
        this.login = login;
        this.userSessionService = userSessionService;
    }


    @GET
    public TemplateInstance showLoginPage() {
        return login.data("error-msg", null);
    }


    @POST
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password) {
        String[] authResult = authenticate(email, password);

        if (authResult[0].equals("200")) {
            String uri = "/reparto";
            if (authResult[1].equals("Portineria")) {
                uri = "/portineria";
            }
            NewCookie sessionCookie = userSessionService.createUserSession(email);
            return Response
                    .seeOther(URI.create(uri))
                    .cookie(sessionCookie)
                    .build();
        }

        return Response
                .status(Integer.parseInt(authResult[0]))
                .entity(login.data("error-msg", authResult[1]))
                .build();
    }


    private String[] authenticate(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return new String[]{"400", "Per favore, inserisci sia l'email che la password."};
        }

        try (Reader reader = new FileReader(FILE_PATH)) {
            CSVFormat format = CSVFormat.Builder.create()
                    .setHeader()
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .get();
            CSVParser parser = CSVParser.parse(reader, format);

            for (CSVRecord record : parser) {
                String storedEmail = record.get("email");
                String storedPassword = record.get("password");
                String department = record.get("department");

                if (storedEmail.equals(email) && storedPassword.equals(password)) {
                    return new String[]{"200", department};
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new String[]{"500", "Errore interno del server."};
        }

        return new String[]{"401", "Email o password errati. Riprova."};
    }
}
