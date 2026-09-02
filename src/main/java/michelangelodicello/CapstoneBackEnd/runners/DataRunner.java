package michelangelodicello.CapstoneBackEnd.runners;

import michelangelodicello.CapstoneBackEnd.entities.Categoria;
import michelangelodicello.CapstoneBackEnd.entities.Prodotto;
import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.enums.Role;
import michelangelodicello.CapstoneBackEnd.repositories.CategoriaRepository;
import michelangelodicello.CapstoneBackEnd.repositories.ProdottoRepository;
import michelangelodicello.CapstoneBackEnd.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataRunner implements CommandLineRunner {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {

        // 1. Inserimento Utenti Admin e User
        if (utenteRepository.count() == 0) {
            Utente admin = new Utente();
            admin.setNome("Michelangelo");
            admin.setCognome("Di Cello");
            admin.setEmail("admin@techstore.com");
            admin.setPassword(passwordEncoder.encode("AdminPassword123!"));
            admin.setRole(Role.ROLE_ADMIN);

            Utente user = new Utente();
            user.setNome("Mario");
            user.setCognome("Rossi");
            user.setEmail("user@techstore.com");
            user.setPassword(passwordEncoder.encode("UserPassword123!"));
            user.setRole(Role.ROLE_USER);

            utenteRepository.saveAll(List.of(admin, user));
            System.out.println("Utenti Admin e User creati!");
        }

        // 2. Inserimento Categorie e Prodotti da JSON
        if (categoriaRepository.count() == 0) {
            InputStream inputStream = new ClassPathResource("products.json").getInputStream();
            JsonDataWrapper data = objectMapper.readValue(inputStream, JsonDataWrapper.class);

            Map<String, Categoria> categoriaMap = new HashMap<>();

// Mappatura e Salvataggio Categorie
            for (JsonCategory jsonCat : data.getCategories()) {
                Categoria cat = new Categoria();
                cat.setName(jsonCat.getId());           // es. "macbook", "windows", "cpu"
                cat.setDisplayName(jsonCat.getLabel()); // es. "MacBook", "Notebook Windows"

                Categoria savedCat = categoriaRepository.save(cat);
                categoriaMap.put(jsonCat.getId(), savedCat);
            }
            // Mappatura e Salvataggio Prodotti nel DataRunner
            for (JsonProduct jsonProd : data.getProducts()) {
                Categoria catAssociata = categoriaMap.get(jsonProd.getCategoryId());

                if (catAssociata != null) {
                    Prodotto p = new Prodotto();
                    p.setCategoria(catAssociata);
                    p.setTitle(jsonProd.getTitle());
                    p.setSubtitle(jsonProd.getSubtitle());
                    p.setBadge(jsonProd.getBadge());
                    p.setImage(jsonProd.getImage());
                    p.setRating(jsonProd.getRating());
                    p.setReviews(jsonProd.getReviews());
                    p.setOffers(jsonProd.getOffers());
                    p.setPrice(jsonProd.getPrice());
                    p.setStockQuantity(20);
                    p.setSpecs(jsonProd.getSpecs());

                    prodottoRepository.save(p);
                }
            }

            System.out.println("✅ Importati con successo " + categoriaMap.size() + " Categorie e " + data.getProducts().size() + " Prodotti!");
        }
    }

    // --- CLASSI DTO DI SUPPORTO PER IL PARSING JSON ---

    public static class JsonDataWrapper {
        private List<JsonCategory> categories;
        private List<JsonProduct> products;

        public List<JsonCategory> getCategories() {
            return categories;
        }

        public void setCategories(List<JsonCategory> categories) {
            this.categories = categories;
        }

        public List<JsonProduct> getProducts() {
            return products;
        }

        public void setProducts(List<JsonProduct> products) {
            this.products = products;
        }
    }

    public static class JsonCategory {
        private String id;
        private String label;
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class JsonProduct {
        private String categoryId;
        private String title;
        private String subtitle;
        private String badge;
        private String image;
        private Double rating;
        private String reviews;
        private Integer offers;
        private Double price;
        private Map<String, Object> specs; // Mappato come Map per il campo JSONB

        // Getters e Setters
        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public String getReviews() {
            return reviews;
        }

        public void setReviews(String reviews) {
            this.reviews = reviews;
        }

        public Integer getOffers() {
            return offers;
        }

        public void setOffers(Integer offers) {
            this.offers = offers;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Map<String, Object> getSpecs() {
            return specs;
        }

        public void setSpecs(Map<String, Object> specs) {
            this.specs = specs;
        }
    }

}