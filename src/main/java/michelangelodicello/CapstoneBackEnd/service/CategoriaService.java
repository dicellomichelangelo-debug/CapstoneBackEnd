package michelangelodicello.CapstoneBackEnd.service;

import michelangelodicello.CapstoneBackEnd.entities.Categoria;
import michelangelodicello.CapstoneBackEnd.exceptions.NotFoundException;
import michelangelodicello.CapstoneBackEnd.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAll() {
        return categoriaRepository.findAll();
    }

    public Categoria getById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + id + " non trovata."));
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria update(Long id, Categoria body) {
        Categoria found = this.getById(id);

        found.setName(body.getName());
        found.setDisplayName(body.getDisplayName());

        return categoriaRepository.save(found);
    }

    public void delete(Long id) {
        Categoria found = this.getById(id);
        categoriaRepository.delete(found);
    }
}