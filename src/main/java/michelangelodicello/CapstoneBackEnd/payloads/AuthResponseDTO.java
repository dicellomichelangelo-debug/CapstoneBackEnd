package michelangelodicello.CapstoneBackEnd.payloads;


public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        String role,
        Long userId
) {
}
