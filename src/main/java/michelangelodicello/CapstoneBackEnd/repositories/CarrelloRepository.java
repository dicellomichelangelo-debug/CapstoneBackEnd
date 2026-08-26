package michelangelodicello.CapstoneBackEnd.repositories;

import michelangelodicello.CapstoneBackEnd.entities.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    Optional<Carrello> findByUtenteId(Long utenteId);
}