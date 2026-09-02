package michelangelodicello.CapstoneBackEnd.repositories;

import michelangelodicello.CapstoneBackEnd.entities.DettaglioOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Long> {
    List<DettaglioOrdine> findByOrderId(Long ordineId);
}