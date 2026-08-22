package michelangelodicello.CapstoneBackEnd.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dettaglio_ordine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DettaglioOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Ordine order;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "product_id")
    private Prodotto prodotto;

    @Column(name = "price_at_purchase", nullable = false)
    private Double priceAtPurchase;

    @Column(nullable = false)
    private Integer quantity;
}