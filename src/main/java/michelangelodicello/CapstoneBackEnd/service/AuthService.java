package michelangelodicello.CapstoneBackEnd.service;

import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.enums.Role;
import michelangelodicello.CapstoneBackEnd.exceptions.UnauthorizedException;
import michelangelodicello.CapstoneBackEnd.payloads.AuthResponseDTO;
import michelangelodicello.CapstoneBackEnd.payloads.LoginDTO;
import michelangelodicello.CapstoneBackEnd.repositories.UtenteRepository;
import michelangelodicello.CapstoneBackEnd.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTools jwtTools;

    public Utente registerUser(Utente body) {
        if (utenteRepository.existsByEmail(body.getEmail())) {
            throw new IllegalArgumentException("Email già in uso nel sistema!");
        }

        Utente newUser = new Utente();
        newUser.setNome(body.getNome());
        newUser.setCognome(body.getCognome());
        newUser.setEmail(body.getEmail());

        newUser.setPassword(passwordEncoder.encode(body.getPassword()));
        newUser.setRole(Role.ROLE_USER);

        return utenteRepository.save(newUser);
    }

    public AuthResponseDTO authenticateUser(LoginDTO body) {
        Utente utente = utenteRepository.findByEmail(body.email())
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide!"));

        if (!passwordEncoder.matches(body.password(), utente.getPassword())) {
            throw new UnauthorizedException("Credenziali non valide!");
        }

        String token = jwtTools.createToken(utente);

        return new AuthResponseDTO(
                token,
                "Bearer",
                utente.getRole().name(),
                utente.getId()
        );
    }
}