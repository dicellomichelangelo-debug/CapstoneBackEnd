package michelangelodicello.CapstoneBackEnd.repositories;

import michelangelodicello.CapstoneBackEnd.entities.ElementiCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElementiCarrelloRepository extends JpaRepository<ElementiCarrello, Long> {
    List<ElementiCarrello> findByCarrelloId(Long carrelloId);

    Optional<ElementiCarrello> findByCarrelloIdAndProdottoId(Long carrelloId, Long prodottoId);

    void deleteByCarrelloId(Long carrelloId);
}