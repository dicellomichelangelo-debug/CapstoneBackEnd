package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Carrello;
import michelangelodicello.CapstoneBackEnd.payloads.ElementiCarrelloDTO;
import michelangelodicello.CapstoneBackEnd.service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @GetMapping("/user/{utenteId}")
    public Carrello getCartByUserId(@PathVariable Long utenteId) {
        return carrelloService.getCartByUserId(utenteId);
    }

    @PostMapping("/user/{utenteId}/add")
    public Carrello addToCart(@PathVariable Long utenteId, @RequestBody ElementiCarrelloDTO elementiDTO) {
        return carrelloService.addToCart(utenteId, elementiDTO);
    }

    @DeleteMapping("/user/{utenteId}/remove/{prodottoId}")
    public Carrello removeFromCart(@PathVariable Long utenteId, @PathVariable Long prodottoId) {
        return carrelloService.removeFromCart(utenteId, prodottoId);
    }

    @DeleteMapping("/user/{utenteId}/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable Long utenteId) {
        carrelloService.clearCart(utenteId);
    }
}