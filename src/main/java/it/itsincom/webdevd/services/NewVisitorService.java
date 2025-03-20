package it.itsincom.webdevd.services;
import it.itsincom.webdevd.models.Visitor;
import it.itsincom.webdevd.repositories.NewVisitorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NewVisitorService {
    public static final String OPERATION_SUCCESS = "success";
    private final NewVisitorRepository newVisitorRepository;

    public NewVisitorService(NewVisitorRepository newVisitorRepository) {
        this.newVisitorRepository = newVisitorRepository;
    }
    public String addNewVisitor(String name, String surname, String phone, String email) {
        if (name == null || name.isEmpty() || surname == null || surname.isEmpty() ||
                phone == null || phone.isEmpty() || email == null || email.isEmpty()) {
            return "Tutti i campi sono obbligatori.";
        }

        List<Visitor> visitors = newVisitorRepository.getAllVisitors();

        for (Visitor visitor : visitors) {
            if (visitor.getFirstName().equalsIgnoreCase(name) &&
                    visitor.getLastName().equalsIgnoreCase(surname) &&
                    visitor.getPhone().equals(phone) &&
                    visitor.getEmail().equals(email)) {
                return "Il visitatore è già registrato!";
            }
        }

        Visitor visitor = new Visitor(0, name, surname, phone, email);

        newVisitorRepository.addNewVisitor(visitor);
        return OPERATION_SUCCESS;
    }
}



