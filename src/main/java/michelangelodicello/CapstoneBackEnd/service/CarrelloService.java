package michelangelodicello.CapstoneBackEnd.service;

import michelangelodicello.CapstoneBackEnd.entities.Carrello;
import michelangelodicello.CapstoneBackEnd.entities.ElementiCarrello;
import michelangelodicello.CapstoneBackEnd.entities.Prodotto;
import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.exceptions.NotFoundException;
import michelangelodicello.CapstoneBackEnd.payloads.ElementiCarrelloDTO;
import michelangelodicello.CapstoneBackEnd.repositories.CarrelloRepository;
import michelangelodicello.CapstoneBackEnd.repositories.ElementiCarrelloRepository;
import michelangelodicello.CapstoneBackEnd.repositories.ProdottoRepository;
import michelangelodicello.CapstoneBackEnd.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private ElementiCarrelloRepository elementoCarrelloRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    public Carrello getCartByUserId(Long utenteId) {
        return carrelloRepository.findByUtenteId(utenteId)
                .orElseGet(() -> createCartForUser(utenteId));
    }

    private Carrello createCartForUser(Long utenteId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + utenteId));
        Carrello cart = new Carrello();
        cart.setUtente(utente);
        return carrelloRepository.save(cart);
    }

    @Transactional
    public Carrello addToCart(Long utenteId, ElementiCarrelloDTO elementoDTO) {
        Carrello cart = getCartByUserId(utenteId);
        Prodotto prodotto = prodottoRepository.findById(elementoDTO.prodottoId())
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato con ID: " + elementoDTO.prodottoId()));

        Optional<ElementiCarrello> existingItem = elementoCarrelloRepository.findByCarrelloIdAndProdottoId(cart.getId(), prodotto.getId());

        if (existingItem.isPresent()) {
            ElementiCarrello item = existingItem.get();
            item.setQuantity(item.getQuantity() + (elementoDTO.quantity() != null ? elementoDTO.quantity() : 1));
            elementoCarrelloRepository.save(item);
        } else {
            ElementiCarrello newItem = new ElementiCarrello();
            newItem.setCarrello(cart);
            newItem.setProdotto(prodotto);
            newItem.setQuantity(elementoDTO.quantity() != null ? elementoDTO.quantity() : 1);
            elementoCarrelloRepository.save(newItem);
        }

        return carrelloRepository.findById(cart.getId()).get();
    }

    @Transactional
    public Carrello removeFromCart(Long utenteId, Long prodottoId) {
        Carrello cart = getCartByUserId(utenteId);
        Optional<ElementiCarrello> item = elementoCarrelloRepository.findByCarrelloIdAndProdottoId(cart.getId(), prodottoId);

        item.ifPresent(elementoCarrelloRepository::delete);

        return carrelloRepository.findById(cart.getId()).get();
    }

    @Transactional
    public void clearCart(Long utenteId) {
        Carrello cart = getCartByUserId(utenteId);
        elementoCarrelloRepository.deleteByCarrelloId(cart.getId());
    }
}