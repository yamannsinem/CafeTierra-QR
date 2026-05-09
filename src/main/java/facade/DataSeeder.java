package facade;

import entity.Category;
import entity.MenuItem;
import entity.User;
import entity.CafeTable;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Singleton
@Startup
public class DataSeeder {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        // Eğer kullanıcı tablosu boşsa VEYA kategori tablosu boşsa veri ekle
        boolean noUsers = em.createQuery("SELECT u FROM User u", User.class).getResultList().isEmpty();
        boolean noCategories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList().isEmpty();
        
        if (noUsers || noCategories) {
            seedData();
        }
    }

    private void seedData() {
        // Admin User
        User admin = new User("yonetici_tierra", "Tierra44!");
        em.persist(admin);

        // Categories
        Category hotDrinks = new Category("Sıcak İçecekler");
        Category coldDrinks = new Category("Soğuk İçecekler");
        Category desserts = new Category("Tatlılar");
        Category snacks = new Category("Atıştırmalıklar");
        em.persist(hotDrinks);
        em.persist(coldDrinks);
        em.persist(desserts);
        em.persist(snacks);

        // Menu Items - Sıcak İçecekler
        em.persist(new MenuItem("Espresso", "Yoğun ve sert İtalyan kahvesi", new BigDecimal("45.00"), hotDrinks));
        em.persist(new MenuItem("Latte", "Yumuşak içimli sütlü kahve", new BigDecimal("65.00"), hotDrinks));
        em.persist(new MenuItem("Türk Kahvesi", "Geleneksel Türk kahvesi", new BigDecimal("55.00"), hotDrinks));
        em.persist(new MenuItem("Cappuccino", "Bol köpüklü klasik lezzet", new BigDecimal("70.00"), hotDrinks));
        em.persist(new MenuItem("Mocha", "Çikolata ve kahvenin uyumu", new BigDecimal("80.00"), hotDrinks));
        em.persist(new MenuItem("Flat White", "Yoğun kahve tadı ve süt kreması", new BigDecimal("75.00"), hotDrinks));
        
        // Soğuk İçecekler
        em.persist(new MenuItem("Iced Americano", "Buzlu ve ferahlatıcı kahve", new BigDecimal("60.00"), coldDrinks));
        em.persist(new MenuItem("Limonata", "Ev yapımı taze limonata", new BigDecimal("50.00"), coldDrinks));
        em.persist(new MenuItem("Iced Latte", "Soğuk sütlü kahve keyfi", new BigDecimal("70.00"), coldDrinks));
        em.persist(new MenuItem("Çilekli Frappe", "Buzlu meyveli karışım", new BigDecimal("85.00"), coldDrinks));
        
        // Tatlılar
        em.persist(new MenuItem("San Sebastian", "Akışkan kıvamlı peynir keki", new BigDecimal("110.00"), desserts));
        em.persist(new MenuItem("Tiramisu", "Klasik İtalyan tatlısı", new BigDecimal("95.00"), desserts));
        em.persist(new MenuItem("Magnolia", "Taze meyveli ve bisküvili puding", new BigDecimal("85.00"), desserts));
        em.persist(new MenuItem("Frambuazlı Cheesecake", "Hafif ve lezzetli peynir keki", new BigDecimal("105.00"), desserts));

        // Atıştırmalıklar
        em.persist(new MenuItem("Kruvasan", "Tereyağlı çıtır Fransız lezzeti", new BigDecimal("65.00"), snacks));
        em.persist(new MenuItem("Kurabiye Tabağı", "Günlük taze kurabiye çeşitleri", new BigDecimal("75.00"), snacks));

        // Tables - Reduced number as requested
        for (int i = 1; i <= 5; i++) {
            em.persist(new CafeTable("Masa " + i));
        }
        em.persist(new CafeTable("Bahçe 1"));
        em.persist(new CafeTable("Bahçe 2"));
    }
}
