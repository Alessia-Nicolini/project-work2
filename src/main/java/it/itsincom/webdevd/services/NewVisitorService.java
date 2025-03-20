package it.itsincom.webdevd.services;
import it.itsincom.webdevd.models.Visitor;
import it.itsincom.webdevd.repositories.NewVisitorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NewVisitorService {
    public static final String OPERATION_SUCCESS = "Success";

    private final NewVisitorRepository newVisitorRepository;

    public NewVisitorService(NewVisitorRepository newVisitorRepository) {
        this.newVisitorRepository = newVisitorRepository;
    }

    public String addNewVisitor(String first_name, String last_name, String email, String phone) {
        if (first_name == null || first_name.isEmpty() || last_name == null || last_name.isEmpty() ||
                phone == null || phone.isEmpty() || email == null || email.isEmpty()) {
            return "Tutti i campi sono obbligatori.";
        }

        List<Visitor> visitors = newVisitorRepository.getAllVisitors();

        for (Visitor visitor : visitors) {
            if (visitor.getFirstName().equalsIgnoreCase(first_name) &&
                    visitor.getLastName().equalsIgnoreCase(last_name) &&
                    visitor.getPhone().equals(phone) &&
                    visitor.getEmail().equals(email)) {
                return "Il visitatore è già registrato!";
            }
        }

        Visitor visitor = new Visitor(0, first_name, last_name, email, phone);

        newVisitorRepository.addNewVisitor(visitor);
        return OPERATION_SUCCESS;
    }
}



