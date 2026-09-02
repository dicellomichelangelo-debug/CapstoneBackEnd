package michelangelodicello.CapstoneBackEnd.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categorie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "prodotti",
        "hibernateLazyInitializer",
        "handler"
})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            unique = true,
            nullable = false,
            length = 50
    )
    private String name;

    @Column(
            name = "display_name",
            nullable = false,
            length = 100
    )
    private String displayName;

    @OneToMany(
            mappedBy = "categoria",
            cascade = CascadeType.ALL
    )
    private List<Prodotto> prodotti;
}