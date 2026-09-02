package michelangelodicello.CapstoneBackEnd.service;

import michelangelodicello.CapstoneBackEnd.entities.Categoria;
import michelangelodicello.CapstoneBackEnd.entities.Prodotto;
import michelangelodicello.CapstoneBackEnd.exceptions.NotFoundException;
import michelangelodicello.CapstoneBackEnd.payloads.ProdottoDTO;
import michelangelodicello.CapstoneBackEnd.repositories.CategoriaRepository;
import michelangelodicello.CapstoneBackEnd.repositories.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Prodotto> getAll(
            String category,
            Double maxPrice,
            String search,
            Boolean onlyDiscounted,
            Boolean inStockOnly
    ) {
        // Evita parametri String null nella query PostgreSQL
        String catFilter =
                category != null
                        && !category.trim().isEmpty()
                        && !category.equalsIgnoreCase("all")
                        ? category.trim()
                        : "all";

        String searchFilter =
                search != null && !search.trim().isEmpty()
                        ? search.trim()
                        : "";

        boolean discountedFilter =
                Boolean.TRUE.equals(onlyDiscounted);

        boolean stockFilter =
                Boolean.TRUE.equals(inStockOnly);

        return prodottoRepository.filterProducts(
                catFilter,
                maxPrice,
                searchFilter,
                discountedFilter,
                stockFilter
        );
    }

    public Prodotto getById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prodotto con ID " + id + " non trovato."));
    }

    public Prodotto save(ProdottoDTO body) {
        Categoria cat = categoriaRepository.findById(body.categoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + body.categoriaId() + " non trovata."));

        Prodotto p = new Prodotto();

        if (body.id() != null) {
            p.setId(body.id());
        }

        p.setCategoria(cat);
        p.setTitle(body.title());
        p.setSubtitle(body.subtitle());
        p.setBadge(body.badge());
        p.setImage(body.image());
        p.setRating(body.rating());
        p.setReviews(body.reviews());
        p.setOffers(body.offers());
        p.setPrice(body.price());
        p.setStockQuantity(body.stockQuantity() != null ? body.stockQuantity() : 0);
        p.setSpecs(body.specs());

        return prodottoRepository.save(p);
    }

    public Prodotto update(Long id, ProdottoDTO body) {
        Prodotto p = this.getById(id);

        Categoria cat = categoriaRepository.findById(body.categoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + body.categoriaId() + " non trovata."));

        p.setCategoria(cat);
        p.setTitle(body.title());
        p.setSubtitle(body.subtitle());
        p.setBadge(body.badge());
        p.setImage(body.image());
        p.setRating(body.rating());
        p.setReviews(body.reviews());
        p.setOffers(body.offers());
        p.setPrice(body.price());
        p.setStockQuantity(body.stockQuantity() != null ? body.stockQuantity() : 0);
        p.setSpecs(body.specs());

        return prodottoRepository.save(p);
    }

    public void delete(Long id) {
        Prodotto p = this.getById(id);
        prodottoRepository.delete(p);
    }
}