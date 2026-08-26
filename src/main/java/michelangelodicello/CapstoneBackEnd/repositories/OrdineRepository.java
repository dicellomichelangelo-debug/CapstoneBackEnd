package michelangelodicello.CapstoneBackEnd.repositories;

import michelangelodicello.CapstoneBackEnd.entities.Ordine;
import michelangelodicello.CapstoneBackEnd.enums.StatoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByUtenteIdOrderByCreatedAtDesc(Long utenteId);

    List<Ordine> findByStatus(StatoOrdine stato);
}