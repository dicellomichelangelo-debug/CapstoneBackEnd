package michelangelodicello.CapstoneBackEnd.entities;


import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "prodotto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    private String badge;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String image;

    private Double rating;

    private String reviews;

    @Column(name = "offers_count")
    private Integer offers;

    @Column(nullable = false)
    private Double price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> specs;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}