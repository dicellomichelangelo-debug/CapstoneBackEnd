package michelangelodicello.CapstoneBackEnd.controllers;

import michelangelodicello.CapstoneBackEnd.entities.Ordine;
import michelangelodicello.CapstoneBackEnd.service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @GetMapping("/user/{utenteId}")
    public List<Ordine> getOrdersByUser(@PathVariable Long utenteId) {
        return ordineService.getOrdersByUser(utenteId);
    }

    @GetMapping("/{id}")
    public Ordine getOrderById(@PathVariable Long id) {
        return ordineService.getOrderById(id);
    }

    @PostMapping("/checkout/user/{utenteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Ordine checkout(@PathVariable Long utenteId) {
        return ordineService.createOrderFromCart(utenteId);
    }
}