package michelangelodicello.CapstoneBackEnd.repositories;

import michelangelodicello.CapstoneBackEnd.entities.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecensioneRepository
        extends JpaRepository<Recensione, Long> {

    List<Recensione>
    findByProdottoIdOrderByCreatedAtDesc(Long prodottoId);

    boolean existsByProdottoIdAndUtenteId(
            Long prodottoId,
            Long utenteId
    );
}