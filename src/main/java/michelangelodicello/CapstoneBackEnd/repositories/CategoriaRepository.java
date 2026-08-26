package michelangelodicello.CapstoneBackEnd.repositories;

import michelangelodicello.CapstoneBackEnd.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNameIgnoreCase(String name);
}