package it.itsincom.webdevd.services;
import it.itsincom.webdevd.models.Visitor;
import it.itsincom.webdevd.repositories.VisitorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NewVisitorService {
    public static final String OPERATION_SUCCESS = "Success";
    private static final String ITALIAN_MOBILE_REGEX = "^(\\+39\\s?|0039\\s?)?3\\d{2}[-\\s]?\\d{3}[-\\s]?\\d{4}$";
    private static final String ITALIAN_FIXED_REGEX = "^(?:(?:\\+39|0039)\\s?)?(?:0\\d{1,3}|\\(0\\d{1,3}\\))[-\\s]?\\d{5,8}$";


    private final VisitorRepository visitorRepository;

    public NewVisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public static boolean isValidEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        return email.split("@").length == 2;
    }

    public static boolean isValidItalianMobile(String phone) {
        return phone != null && phone.matches(ITALIAN_MOBILE_REGEX);
    }

    public static boolean isValidItalianFixed(String phone) {
        return phone != null && phone.matches(ITALIAN_FIXED_REGEX);
    }

    public String addNewVisitor(String first_name, String last_name, String email, String phone) {
        if (first_name == null || first_name.isEmpty() || last_name == null || last_name.isEmpty() ||
                phone == null || phone.isEmpty() || email == null || email.isEmpty()) {
            return "Tutti i campi sono obbligatori.";
        }

        if (!isValidEmail(email)) {
            return "Email non valida.";
        }
        if (!isValidItalianMobile(phone) && !isValidItalianFixed(phone)) {
            return "Numero di telefono non valido.";
        }

        List<Visitor> visitors = visitorRepository.getAllVisitors();

        for (Visitor visitor : visitors) {
            if (visitor.getFirstName().equalsIgnoreCase(first_name) &&
                    visitor.getLastName().equalsIgnoreCase(last_name) &&
                    visitor.getPhone().equals(phone) &&
                    visitor.getEmail().equals(email)) {
                return "Il visitatore è già registrato!";
            }
        }

        int visitorId = visitorRepository.getLastVisitorId() + 1;

        Visitor visitor = new Visitor(visitorId, first_name, last_name, email, phone);

        visitorRepository.addVisitor(visitor);
        return OPERATION_SUCCESS;
    }
}



