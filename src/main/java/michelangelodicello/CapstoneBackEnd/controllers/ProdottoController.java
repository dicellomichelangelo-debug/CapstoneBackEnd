package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Prodotto;
import michelangelodicello.CapstoneBackEnd.payloads.ProdottoDTO;
import michelangelodicello.CapstoneBackEnd.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;


    @GetMapping
    public List<Prodotto> getAllProdotti(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean onlyDiscounted,
            @RequestParam(required = false) Boolean inStockOnly

    ) {
        return prodottoService.getAll(category, maxPrice, search, onlyDiscounted, inStockOnly);
    }

    @GetMapping("/{id}")
    public Prodotto getProdottoById(@PathVariable Long id) {
        return prodottoService.getById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Prodotto createProdotto(@RequestBody ProdottoDTO body) {
        return prodottoService.save(body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Prodotto updateProdotto(@PathVariable Long id, @RequestBody ProdottoDTO body) {
        return prodottoService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProdotto(@PathVariable Long id) {
        prodottoService.delete(id);
    }
}