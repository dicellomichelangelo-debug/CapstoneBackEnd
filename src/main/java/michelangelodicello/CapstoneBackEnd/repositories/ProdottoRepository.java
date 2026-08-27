package michelangelodicello.CapstoneBackEnd.repositories;

import michelangelodicello.CapstoneBackEnd.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    @Query("SELECT p FROM Prodotto p WHERE " +
            "(:category IS NULL OR :category = 'all' OR LOWER(p.categoria.name) = LOWER(:category)) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.subtitle) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:onlyDiscounted IS FALSE OR p.badge IS NOT NULL OR p.offers > 0) AND " +
            "(:inStockOnly IS FALSE OR p.stockQuantity > 0)")
    List<Prodotto> filterProducts(
            @Param("category") String category,
            @Param("maxPrice") Double maxPrice,
            @Param("search") String search,
            @Param("onlyDiscounted") Boolean onlyDiscounted,
            @Param("inStockOnly") Boolean inStockOnly
    );
}