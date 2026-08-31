package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Prodotto;
import michelangelodicello.CapstoneBackEnd.payloads.ProdottoDTO;
import michelangelodicello.CapstoneBackEnd.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    @GetMapping
    public List<Prodotto> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "false") Boolean onlyDiscounted,
            @RequestParam(defaultValue = "false") Boolean inStockOnly
    ) {
        return prodottoService.getAll(category, maxPrice, search, onlyDiscounted, inStockOnly);
    }

    @GetMapping("/{id}")
    public Prodotto getProductById(@PathVariable Long id) {
        return prodottoService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prodotto createProduct(@RequestBody ProdottoDTO body) {
        return prodottoService.save(body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        prodottoService.delete(id);
    }
}