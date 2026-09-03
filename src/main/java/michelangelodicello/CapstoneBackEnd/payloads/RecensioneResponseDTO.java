package michelangelodicello.CapstoneBackEnd.payloads;

import java.time.LocalDateTime;

public record RecensioneResponseDTO(
        Long id,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Long userId,
        String userName
) {
}