package michelangelodicello.CapstoneBackEnd.service;

import michelangelodicello.CapstoneBackEnd.entities.*;
import michelangelodicello.CapstoneBackEnd.enums.StatoOrdine;
import michelangelodicello.CapstoneBackEnd.exceptions.NotFoundException;
import michelangelodicello.CapstoneBackEnd.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private DettaglioOrdineRepository dettaglioOrdineRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private ElementiCarrelloRepository elementoCarrelloRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    public List<Ordine> getOrdersByUser(Long utenteId) {
        return ordineRepository.findByUtenteIdOrderByCreatedAtDesc(utenteId);
    }

    public Ordine getOrderById(Long id) {
        return ordineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ordine con ID " + id + " non trovato."));
    }

    @Transactional
    public Ordine createOrderFromCart(Long utenteId) {
        Carrello cart = carrelloRepository.findByUtenteId(utenteId)
                .orElseThrow(() -> new NotFoundException("Carrello vuoto o non trovato per l'utente " + utenteId));

        List<ElementiCarrello> cartItems = elementoCarrelloRepository.findByCarrelloId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Impossibile effettuare l'ordine: il carrello è vuoto.");
        }

        Ordine ordine = new Ordine();
        ordine.setUtente(cart.getUtente());
        ordine.setStatoOrdine(StatoOrdine.PAID);

        double totale = 0.0;
        List<DettaglioOrdine> dettagli = new ArrayList<>();

        for (ElementiCarrello item : cartItems) {
            Prodotto prodotto = item.getProdotto();


            if (prodotto.getStockQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Quantità insufficiente in magazzino per il prodotto: " + prodotto.getTitle());
            }
            prodotto.setStockQuantity(prodotto.getStockQuantity() - item.getQuantity());
            prodottoRepository.save(prodotto);


            DettaglioOrdine dettaglio = new DettaglioOrdine();
            dettaglio.setOrder(ordine);
            dettaglio.setProdotto(prodotto);
            dettaglio.setQuantity(item.getQuantity());
            dettaglio.setPriceAtPurchase(prodotto.getPrice());

            totale += prodotto.getPrice() * item.getQuantity();
            dettagli.add(dettaglio);
        }

        ordine.setTotalAmount(totale);
        Ordine savedOrder = ordineRepository.save(ordine);

        for (DettaglioOrdine d : dettagli) {
            d.setOrder(savedOrder);
            dettaglioOrdineRepository.save(d);
        }

        elementoCarrelloRepository.deleteByCarrelloId(cart.getId());

        return savedOrder;
    }
}