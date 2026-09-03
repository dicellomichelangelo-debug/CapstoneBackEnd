package michelangelodicello.CapstoneBackEnd.payloads;

public record RegisterDTO(
        String nome,
        String cognome,
        String email,
        String password
) {
}