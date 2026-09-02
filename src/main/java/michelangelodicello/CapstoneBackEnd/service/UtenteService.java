package michelangelodicello.CapstoneBackEnd.service;

import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.exceptions.NotFoundException;
import michelangelodicello.CapstoneBackEnd.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public List<Utente> getAll() {
        return utenteRepository.findAll();
    }

    public Utente getById(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con ID " + id + " non trovato."));
    }

    public Utente getByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato."));
    }

    public Utente update(Long id, Utente updatedUser) {
        Utente found = this.getById(id);
        found.setNome(updatedUser.getNome());
        found.setCognome(updatedUser.getCognome());
        found.setEmail(updatedUser.getEmail());
        return utenteRepository.save(found);
    }

    public void delete(Long id) {
        Utente found = this.getById(id);
        utenteRepository.delete(found);
    }
}