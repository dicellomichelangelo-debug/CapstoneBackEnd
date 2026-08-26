package michelangelodicello.CapstoneBackEnd.payloads;

import java.util.Map;

public record ProdottoDTO(
        Long id,
        Long categoriaId,
        String title,
        String subtitle,
        String badge,
        String image,
        Double rating,
        String reviews,
        Integer offers,
        Double price,
        Integer stockQuantity,
        Map<String, Object> specs
) {
}