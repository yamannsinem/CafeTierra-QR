package facade;

import entity.Category;
import entity.MenuItem;
import entity.User;
import entity.CafeTable;
import entity.CafeComment;
import security.PasswordUtil;
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
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        if (adminPassword == null || adminPassword.trim().isEmpty()) {
            adminPassword = "Tierra44!";
        }
        User admin = new User("yonetici_tierra", PasswordUtil.hashPassword(adminPassword));
        em.persist(admin);

        // Categories with Translations
        Category hotDrinks = new Category("Sıcak İçecekler", "Hot Drinks");
        Category coldDrinks = new Category("Soğuk İçecekler", "Cold Drinks");
        Category desserts = new Category("Tatlılar", "Desserts");
        Category snacks = new Category("Atıştırmalıklar", "Snacks");
        em.persist(hotDrinks);
        em.persist(coldDrinks);
        em.persist(desserts);
        em.persist(snacks);

        // Menu Items - Sıcak İçecekler (Enriched, Localized & Price Adjusted to 180 - 300 TL band)
        em.persist(new MenuItem("Espresso", "Espresso", "Yoğun ve sert İtalyan kahvesi", "Intense and bold Italian coffee", new BigDecimal("180.00"), hotDrinks, true));
        em.persist(new MenuItem("Latte", "Latte", "Yumuşak içimli sütlü kahve", "Smooth milk coffee with light foam", new BigDecimal("220.00"), hotDrinks, false));
        em.persist(new MenuItem("Türk Kahvesi", "Turkish Coffee", "Geleneksel Türk kahvesi", "Traditional Turkish coffee served with Turkish delight", new BigDecimal("190.00"), hotDrinks, true));
        em.persist(new MenuItem("Cappuccino", "Cappuccino", "Bol köpüklü klasik lezzet", "Classic Italian coffee with thick foam", new BigDecimal("240.00"), hotDrinks, false));
        em.persist(new MenuItem("Mocha", "Mocha", "Çikolata ve kahvenin uyumu", "Espresso blended with chocolate and steamed milk", new BigDecimal("260.00"), hotDrinks, false));
        em.persist(new MenuItem("Flat White", "Flat White", "Yoğun kahve tadı ve süt kreması", "Double shot espresso with silky micro-foam", new BigDecimal("250.00"), hotDrinks, true));
        em.persist(new MenuItem("Filtre Kahve", "Filter Coffee", "Günün taze demlenmiş filtre kahvesi", "Freshly brewed filter coffee of the day", new BigDecimal("190.00"), hotDrinks, false));
        em.persist(new MenuItem("Türk Çayı", "Turkish Tea", "Tavşan kanı taze demlenmiş bardak çay", "Freshly brewed traditional Turkish black tea", new BigDecimal("90.00"), hotDrinks, false));
        em.persist(new MenuItem("Bitki Çayı", "Herbal Tea", "Adaçayı, ıhlamur veya yeşil çay seçeneği", "Your choice of sage, linden, or green tea", new BigDecimal("180.00"), hotDrinks, false));
        
        // Soğuk İçecekler (Enriched, Localized & Price Adjusted)
        em.persist(new MenuItem("Iced Americano", "Iced Americano", "Buzlu ve ferahlatıcı kahve", "Espresso poured over ice and cold water", new BigDecimal("210.00"), coldDrinks, false));
        em.persist(new MenuItem("Limonata", "Lemonade", "Ev yapımı taze limonata", "Homemade refreshing lemonade with fresh mint", new BigDecimal("180.00"), coldDrinks, false));
        em.persist(new MenuItem("Iced Latte", "Iced Latte", "Soğuk sütlü kahve keyfi", "Chilled espresso combined with cold milk and ice", new BigDecimal("240.00"), coldDrinks, true));
        em.persist(new MenuItem("Çilekli Frappe", "Strawberry Frappe", "Buzlu meyveli karışım", "Blended ice drink with strawberry puree and whipped cream", new BigDecimal("280.00"), coldDrinks, false));
        em.persist(new MenuItem("Cold Brew", "Cold Brew", "16 saat soğuk demlenmiş premium kahve", "Premium coffee cold-brewed for 16 hours for low acidity", new BigDecimal("250.00"), coldDrinks, false));
        em.persist(new MenuItem("Iced Mocha", "Iced Mocha", "Buzlu çikolatalı kahve keyfi", "Chilled espresso with rich chocolate syrup and cold milk", new BigDecimal("290.00"), coldDrinks, false));
        em.persist(new MenuItem("Şeftalili Milkshake", "Peach Milkshake", "Taze şeftali püresi ve dondurma", "Blended milkshake with fresh peach puree and vanilla ice cream", new BigDecimal("300.00"), coldDrinks, false));
        
        // Tatlılar (Enriched, Localized & Price Adjusted)
        em.persist(new MenuItem("San Sebastian", "San Sebastian Cheesecake", "Akışkan kıvamlı peynir keki", "Famous Basque burnt cheesecake with a creamy center", new BigDecimal("340.00"), desserts, true));
        em.persist(new MenuItem("Tiramisu", "Tiramisu", "Klasik İtalyan tatlısı", "Classic Italian dessert with coffee-soaked ladyfingers", new BigDecimal("290.00"), desserts, false));
        em.persist(new MenuItem("Magnolia", "Magnolia Pudding", "Taze meyveli ve bisküvili puding", "Creamy pudding layers with fresh strawberries and baby biscuits", new BigDecimal("260.00"), desserts, false));
        em.persist(new MenuItem("Frambuazlı Cheesecake", "Raspberry Cheesecake", "Hafif ve lezzetli peynir keki", "Light cheesecake topped with sour raspberry glaze", new BigDecimal("320.00"), desserts, false));
        em.persist(new MenuItem("Çikolatalı Sufle", "Chocolate Souffle", "Sıcak akışkan çikolatalı kek", "Hot chocolate cake with a molten chocolate center", new BigDecimal("280.00"), desserts, false));
        em.persist(new MenuItem("Belçika Waffle", "Belgian Waffle", "Taze meyveler ve Belçika çikolatası ile", "Warm waffle loaded with seasonal fresh fruits and rich chocolate sauce", new BigDecimal("360.00"), desserts, false));

        // Atıştırmalıklar (Enriched, Localized & Price Adjusted)
        em.persist(new MenuItem("Kruvasan", "Butter Croissant", "Tereyağlı çıtır Fransız lezzeti", "Crispy and flaky traditional French butter croissant", new BigDecimal("210.00"), snacks, true));
        em.persist(new MenuItem("Kurabiye Tabağı", "Cookie Platter", "Günlük taze kurabiye çeşitleri", "Assorted daily fresh cookies plate", new BigDecimal("230.00"), snacks, false));
        em.persist(new MenuItem("Kulüp Sandviç", "Club Sandwich", "Tavuk, salam, kaşar peyniri ve patates kızartması ile", "Toasted sandwich with chicken, ham, cheese, served with fries", new BigDecimal("350.00"), snacks, false));
        em.persist(new MenuItem("Patates Kızartması", "French Fries", "Çıtır baharatlı patates kızartması dilimleri", "Crispy seasoned golden potato wedges served with dip sauces", new BigDecimal("220.00"), snacks, false));
        em.persist(new MenuItem("Gurme Peynir Tabağı", "Gourmet Cheese Board", "Yerli ve ithal gurme peynir çeşitleri", "Selection of local and imported premium gourmet cheeses", new BigDecimal("420.00"), snacks, false));

        // Initial Cafe Comments/Reviews
        em.persist(new CafeComment("Ayşe Demir", "Kahveler gerçekten harika, San Sebastian tatlısına bayıldım! Servis de çok hızlıydı.", 5));
        em.persist(new CafeComment("John Doe", "Excellent coffee and warm atmosphere. I recommend the Iced Latte. Staff is very friendly!", 5));
        em.persist(new CafeComment("Murat Kaya", "Masa üzerinden QR kod ile sipariş vermek çok pratik. Çıtır kruvasanları denemelisiniz.", 4));
        em.persist(new CafeComment("Elif Şahin", "Malatya'da böylesine nezih ve premium bir mekan bulmak harika. Flat White efsane.", 5));

        // Tables
        for (int i = 1; i <= 5; i++) {
            em.persist(new CafeTable("Masa " + i));
        }
        em.persist(new CafeTable("Bahçe 1"));
        em.persist(new CafeTable("Bahçe 2"));
    }
}
