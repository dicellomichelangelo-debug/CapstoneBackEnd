package michelangelodicello.CapstoneBackEnd.service;

import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.enums.Role;
import michelangelodicello.CapstoneBackEnd.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente registerUser(Utente body) {
        if (utenteRepository.existsByEmail(body.getEmail())) {
            throw new IllegalArgumentException("Email già in uso nel sistema!");
        }

        Utente newUser = new Utente();
        newUser.setFirstName(body.getFirstName());
        newUser.setLastName(body.getLastName());
        newUser.setEmail(body.getEmail());
        // Quando farò Spring Security applicare l'encoder alla password
        newUser.setPassword(body.getPassword());
        newUser.setRole(Role.ROLE_USER);

        return utenteRepository.save(newUser);
    }
}