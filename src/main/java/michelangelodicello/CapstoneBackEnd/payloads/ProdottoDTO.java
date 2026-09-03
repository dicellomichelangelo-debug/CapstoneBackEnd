package michelangelodicello.CapstoneBackEnd.payloads;

import java.util.Map;

public record ProdottoDTO(
        Long categoriaId,
        String title,
        String subtitle,
        String badge,
        String image,
        Integer offers,
        Double price,
        Integer stockQuantity,
        Map<String, Object> specs
) {
}