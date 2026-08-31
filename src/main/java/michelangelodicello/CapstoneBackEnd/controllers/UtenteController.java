package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping
    public List<Utente> getAllUsers() {
        return utenteService.getAll();
    }

    @GetMapping("/{id}")
    public Utente getUserById(@PathVariable Long id) {
        return utenteService.getById(id);
    }

    @PutMapping("/{id}")
    public Utente updateUser(@PathVariable Long id, @RequestBody Utente body) {
        return utenteService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        utenteService.delete(id);
    }
}