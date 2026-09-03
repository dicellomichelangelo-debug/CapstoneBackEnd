package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.payloads.RecensioneDTO;
import michelangelodicello.CapstoneBackEnd.payloads.RecensioneResponseDTO;
import michelangelodicello.CapstoneBackEnd.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @GetMapping
    public List<RecensioneResponseDTO> getReviews(
            @PathVariable Long productId
    ) {
        return recensioneService.getByProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(
            "hasAnyRole('USER', 'ADMIN')"
    )
    public RecensioneResponseDTO createReview(
            @PathVariable Long productId,
            @RequestBody RecensioneDTO body,
            Authentication authentication
    ) {
        Utente currentUser =
                (Utente) authentication.getPrincipal();

        return recensioneService.create(
                productId,
                body,
                currentUser
        );
    }
}