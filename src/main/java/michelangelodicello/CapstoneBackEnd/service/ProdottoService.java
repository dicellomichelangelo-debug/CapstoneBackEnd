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

        return prodottoRepository.filterProducts(
                catFilter,
                maxPrice,
                searchFilter,
                Boolean.TRUE.equals(onlyDiscounted),
                Boolean.TRUE.equals(inStockOnly)
        );
    }

    public Prodotto getById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Prodotto con ID " + id + " non trovato."
                        )
                );
    }

    public Prodotto save(ProdottoDTO body) {
        validateProduct(body);

        Categoria categoria = categoriaRepository
                .findById(body.categoriaId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Categoria con ID "
                                        + body.categoriaId()
                                        + " non trovata."
                        )
                );

        Prodotto prodotto = new Prodotto();

        prodotto.setCategoria(categoria);
        prodotto.setTitle(body.title().trim());
        prodotto.setSubtitle(body.subtitle());
        prodotto.setBadge(body.badge());
        prodotto.setImage(body.image());
        prodotto.setOffers(
                body.offers() != null ? body.offers() : 0
        );
        prodotto.setPrice(body.price());
        prodotto.setStockQuantity(
                body.stockQuantity() != null
                        ? body.stockQuantity()
                        : 0
        );
        prodotto.setSpecs(body.specs());

        // I valori iniziali non vengono decisi dall'admin.
        prodotto.setRating(0.0);
        prodotto.setReviews("0");

        return prodottoRepository.save(prodotto);
    }

    public Prodotto update(Long id, ProdottoDTO body) {
        validateProduct(body);

        Prodotto prodotto = getById(id);

        Categoria categoria = categoriaRepository
                .findById(body.categoriaId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Categoria con ID "
                                        + body.categoriaId()
                                        + " non trovata."
                        )
                );

        prodotto.setCategoria(categoria);
        prodotto.setTitle(body.title().trim());
        prodotto.setSubtitle(body.subtitle());
        prodotto.setBadge(body.badge());
        prodotto.setImage(body.image());
        prodotto.setOffers(
                body.offers() != null ? body.offers() : 0
        );
        prodotto.setPrice(body.price());
        prodotto.setStockQuantity(
                body.stockQuantity() != null
                        ? body.stockQuantity()
                        : 0
        );
        prodotto.setSpecs(body.specs());

        // Rating e recensioni non vengono modificati dall'admin.

        return prodottoRepository.save(prodotto);
    }

    public void delete(Long id) {
        Prodotto prodotto = getById(id);
        prodottoRepository.delete(prodotto);
    }

    private void validateProduct(ProdottoDTO body) {
        if (body.categoriaId() == null) {
            throw new IllegalArgumentException(
                    "La categoria è obbligatoria."
            );
        }

        if (body.title() == null || body.title().isBlank()) {
            throw new IllegalArgumentException(
                    "Il titolo è obbligatorio."
            );
        }

        if (body.price() == null || body.price() < 0) {
            throw new IllegalArgumentException(
                    "Il prezzo deve essere maggiore o uguale a zero."
            );
        }

        if (
                body.stockQuantity() != null
                        && body.stockQuantity() < 0
        ) {
            throw new IllegalArgumentException(
                    "La disponibilità non può essere negativa."
            );
        }
    }
}