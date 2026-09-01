package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.payloads.AuthResponseDTO;
import michelangelodicello.CapstoneBackEnd.payloads.LoginDTO;
import michelangelodicello.CapstoneBackEnd.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente register(@RequestBody Utente body) {
        return authService.registerUser(body);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginDTO body) {
        return authService.authenticateUser(body);
    }
}