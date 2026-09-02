package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Categoria;
import michelangelodicello.CapstoneBackEnd.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping
    public List<Categoria> getAllCategorie() {
        return categoriaService.getAll();
    }

    @GetMapping("/{id}")
    public Categoria getCategoriaById(@PathVariable Long id) {
        return categoriaService.getById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Categoria createCategoria(@RequestBody Categoria body) {
        return categoriaService.save(body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Categoria updateCategoria(@PathVariable Long id, @RequestBody Categoria body) {
        return categoriaService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategoria(@PathVariable Long id) {
        categoriaService.delete(id);
    }
}