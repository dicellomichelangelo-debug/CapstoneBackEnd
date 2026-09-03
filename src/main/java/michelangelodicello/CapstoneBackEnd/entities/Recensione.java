package michelangelodicello.CapstoneBackEnd.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "recensioni",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_recensione_prodotto_utente",
                        columnNames = {
                                "prodotto_id",
                                "utente_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "prodotto_id",
            nullable = false
    )
    private Prodotto prodotto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "utente_id",
            nullable = false
    )
    private Utente utente;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 2000)
    private String comment;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}