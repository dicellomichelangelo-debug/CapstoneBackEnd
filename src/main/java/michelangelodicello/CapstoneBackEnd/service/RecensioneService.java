package michelangelodicello.CapstoneBackEnd.service;

import jakarta.transaction.Transactional;
import michelangelodicello.CapstoneBackEnd.entities.Prodotto;
import michelangelodicello.CapstoneBackEnd.entities.Recensione;
import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.payloads.RecensioneDTO;
import michelangelodicello.CapstoneBackEnd.payloads.RecensioneResponseDTO;
import michelangelodicello.CapstoneBackEnd.repositories.ProdottoRepository;
import michelangelodicello.CapstoneBackEnd.repositories.RecensioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private ProdottoService prodottoService;

    public List<RecensioneResponseDTO> getByProduct(
            Long prodottoId
    ) {
        prodottoService.getById(prodottoId);

        return recensioneRepository
                .findByProdottoIdOrderByCreatedAtDesc(
                        prodottoId
                )
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public RecensioneResponseDTO create(
            Long prodottoId,
            RecensioneDTO body,
            Utente currentUser
    ) {
        validateReview(body);

        Prodotto prodotto =
                prodottoService.getById(prodottoId);

        boolean alreadyReviewed =
                recensioneRepository
                        .existsByProdottoIdAndUtenteId(
                                prodottoId,
                                currentUser.getId()
                        );

        if (alreadyReviewed) {
            throw new IllegalArgumentException(
                    "Hai già recensito questo prodotto."
            );
        }

        Recensione recensione = new Recensione();
        recensione.setProdotto(prodotto);
        recensione.setUtente(currentUser);
        recensione.setRating(body.rating());
        recensione.setComment(body.comment().trim());

        Recensione saved =
                recensioneRepository.save(recensione);

        updateProductRating(prodotto);

        return toResponseDTO(saved);
    }

    private void updateProductRating(Prodotto prodotto) {
        List<Recensione> reviews =
                recensioneRepository
                        .findByProdottoIdOrderByCreatedAtDesc(
                                prodotto.getId()
                        );

        double average = reviews
                .stream()
                .mapToInt(Recensione::getRating)
                .average()
                .orElse(0.0);

        // Arrotondamento a una cifra decimale
        double roundedAverage =
                Math.round(average * 10.0) / 10.0;

        prodotto.setRating(roundedAverage);
        prodotto.setReviews(
                String.valueOf(reviews.size())
        );

        prodottoRepository.save(prodotto);
    }

    private RecensioneResponseDTO toResponseDTO(
            Recensione recensione
    ) {
        Utente user = recensione.getUtente();

        String userName =
                ((user.getNome() != null
                        ? user.getNome()
                        : "")
                        + " "
                        + (user.getCognome() != null
                        ? user.getCognome()
                        : ""))
                        .trim();

        return new RecensioneResponseDTO(
                recensione.getId(),
                recensione.getRating(),
                recensione.getComment(),
                recensione.getCreatedAt(),
                user.getId(),
                userName.isBlank()
                        ? "Utente"
                        : userName
        );
    }

    private void validateReview(RecensioneDTO body) {
        if (
                body.rating() == null
                        || body.rating() < 1
                        || body.rating() > 5
        ) {
            throw new IllegalArgumentException(
                    "La valutazione deve essere compresa tra 1 e 5."
            );
        }

        if (
                body.comment() == null
                        || body.comment().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Il testo della recensione è obbligatorio."
            );
        }

        if (body.comment().trim().length() > 2000) {
            throw new IllegalArgumentException(
                    "La recensione non può superare 2000 caratteri."
            );
        }
    }
}