package bean;

import entity.CafeOrder;
import entity.OrderItem;
import facadelocal.OrderFacadeLocal;
import util.FacesUtil;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class PaymentBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PaymentBean.class.getName());

    @EJB
    private OrderFacadeLocal orderFacade;

    @Inject
    private CartBean cartBean;

    @Inject
    private CustomerSessionBean customerSessionBean;

    private String cardName;
    private String cardNumber;
    private String cardDate;
    private String cardCvv;

    public void placeOrder() {
        if (customerSessionBean.getCurrentTable() == null) {
            FacesUtil.addErrorMessage("Hata", "Lütfen QR kodunuzu tekrar okutun (Masa bilgisi eksik).");
            return;
        }
        if (cartBean.isEmpty()) {
            FacesUtil.addWarnMessage("Uyarı", "Sepetiniz boş.");
            return;
        }

        // 2. Ödeme Formu Backend Doğrulaması
        if (cardName == null || cardName.trim().isEmpty() || 
            cardNumber == null || cardNumber.trim().isEmpty() ||
            cardDate == null || cardDate.trim().isEmpty() ||
            cardCvv == null || cardCvv.trim().isEmpty()) {
            FacesUtil.addErrorMessage("Hata", "Ödeme bilgileri eksik veya geçersiz.");
            return;
        }

        String cleanCardNumber = cardNumber.replaceAll("\\s+", "");
        if (cleanCardNumber.length() < 16 || !cleanCardNumber.matches("\\d+")) {
            FacesUtil.addErrorMessage("Hata", "Geçersiz kart numarası. En az 16 haneli olmalıdır.");
            return;
        }

        if (!cardDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            FacesUtil.addErrorMessage("Hata", "Geçersiz son kullanma tarihi. Format AA/YY olmalıdır.");
            return;
        }

        if (cardCvv.length() < 3 || !cardCvv.matches("\\d+")) {
            FacesUtil.addErrorMessage("Hata", "Geçersiz CVV.");
            return;
        }

        CafeOrder order = new CafeOrder();
        order.setCafeTable(customerSessionBean.getCurrentTable());
        order.setTotalAmount(cartBean.getCartTotal());
        
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<entity.MenuItem, Integer> entry : cartBean.getCartMap().entrySet()) {
            OrderItem oi = new OrderItem(order, entry.getKey(), entry.getValue(), entry.getKey().getPrice());
            orderItems.add(oi);
        }
        order.setItems(orderItems);

        try {
            orderFacade.createOrder(order);
            customerSessionBean.setActiveOrderId(order.getId());
            customerSessionBean.setActiveOrder(order);
            
            // Temizleme işlemleri
            cartBean.clear();
            this.cardName = null;
            this.cardNumber = null;
            this.cardDate = null;
            this.cardCvv = null;
            
            FacesUtil.redirect("order-success.xhtml");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to place order", e);
            FacesUtil.addErrorMessage("Hata", "Sipariş oluşturulurken bir hata oluştu. Lütfen tekrar deneyiniz.");
        }
    }

    // Getters and Setters
    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardDate() { return cardDate; }
    public void setCardDate(String cardDate) { this.cardDate = cardDate; }
    public String getCardCvv() { return cardCvv; }
    public void setCardCvv(String cardCvv) { this.cardCvv = cardCvv; }
}
