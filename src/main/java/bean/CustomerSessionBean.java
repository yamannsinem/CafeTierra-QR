package bean;

import entity.CafeOrder;
import entity.CafeTable;
import entity.Category;
import entity.MenuItem;
import entity.OrderItem;
import entity.WaiterRequest;
import facadeLocal.MenuFacadeLocal;
import facadeLocal.OrderFacadeLocal;
import facadeLocal.TableFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class CustomerSessionBean implements Serializable {

    @EJB
    private MenuFacadeLocal menuFacade;
    @EJB
    private TableFacadeLocal tableFacade;
    @EJB
    private OrderFacadeLocal orderFacade;

    private Long tableId;
    private CafeTable currentTable;
    
    private List<Category> categories;
    private List<MenuItem> availableItems;

    // Cart: MenuItem -> Quantity
    private Map<MenuItem, Integer> cart = new HashMap<>();

    // Aktif sipariş takibi
    private Long activeOrderId;
    private CafeOrder activeOrder;

    @PostConstruct
    public void init() {
        categories = menuFacade.getAllCategories();
        availableItems = menuFacade.getAllMenuItems().stream().filter(MenuItem::getIsAvailable).toList();
    }

    public void checkTable() {
        String tableParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("table");
        if (tableParam != null && !tableParam.isEmpty()) {
            try {
                this.tableId = Long.parseLong(tableParam);
                this.currentTable = tableFacade.findTableById(this.tableId);
                
                // Eğer masa doluysa VE kullanıcının bu masada aktif bir siparişi yoksa engelle
                if (this.currentTable != null && orderFacade.isTableOccupied(this.tableId)) {
                    // Kullanıcının mevcut seansındaki activeOrderId bu masaya mı ait?
                    if (activeOrderId == null || activeOrder == null || !activeOrder.getCafeTable().getId().equals(this.tableId)) {
                        FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Masa Dolu", "Seçtiğiniz masa şu an dolu. Lütfen başka bir masa seçin."));
                        FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.xhtml");
                        return;
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        
        // If no table is set (not in URL and not previously set in session), redirect to welcome
        if (this.currentTable == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.xhtml");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void callWaiter() {
        if (currentTable != null) {
            WaiterRequest req = new WaiterRequest(currentTable);
            orderFacade.createWaiterRequest(req);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Garson Çağrıldı", "İsteğiniz iletildi, garsonumuz en kısa sürede masanıza gelecektir."));
        }
    }

    public void goToPayment() {
        if (cart.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyarı", "Sepetiniz boş."));
            return;
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("payment.xhtml");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(MenuItem item) {
        cart.put(item, cart.getOrDefault(item, 0) + 1);
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Sepete Eklendi", item.getName() + " sepete eklendi."));
    }
    
    public void removeFromCart(MenuItem item) {
        if (cart.containsKey(item)) {
            int qty = cart.get(item);
            if (qty > 1) {
                cart.put(item, qty - 1);
            } else {
                cart.remove(item);
            }
        }
    }

    public BigDecimal getCartTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            BigDecimal itemTotal = entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue()));
            total = total.add(itemTotal);
        }
        return total;
    }
    
    public static class CartItem implements Serializable {
        private final MenuItem menuItem;
        private final int quantity;
        public CartItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }
        public MenuItem getMenuItem() { return menuItem; }
        public int getQuantity() { return quantity; }
        public BigDecimal getTotalPrice() {
            return menuItem.getPrice().multiply(new BigDecimal(quantity));
        }
    }

    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            items.add(new CartItem(entry.getKey(), entry.getValue()));
        }
        return items;
    }

    public void checkout() {
        goToPayment();
    }

    public void placeOrder() {
        if (currentTable == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Lütfen QR kodunuzu tekrar okutun (Masa bilgisi eksik)."));
            return;
        }
        if (cart.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyarı", "Sepetiniz boş."));
            return;
        }

        CafeOrder order = new CafeOrder();
        order.setCafeTable(currentTable);
        order.setTotalAmount(getCartTotal());
        
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            OrderItem oi = new OrderItem(order, entry.getKey(), entry.getValue(), entry.getKey().getPrice());
            orderItems.add(oi);
        }
        order.setItems(orderItems);

        try {
            orderFacade.createOrder(order);
            this.activeOrderId = order.getId();
            this.activeOrder = order;
            cart.clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("order-success.xhtml");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Sipariş oluşturulamadı: " + e.getMessage()));
        }
    }

    public void refreshActiveOrder() {
        if (activeOrderId != null) {
            this.activeOrder = orderFacade.findOrderById(activeOrderId);
        }
    }

    public CafeOrder getActiveOrder() {
        if (activeOrderId != null && activeOrder != null) {
            // Her çağrıda güncel durumu çek
            this.activeOrder = orderFacade.findOrderById(activeOrderId);
        }
        return activeOrder;
    }

    public boolean isHasActiveOrder() {
        return activeOrderId != null && activeOrder != null;
    }

    // Getters and Setters
    public CafeTable getCurrentTable() { return currentTable; }
    public List<Category> getCategories() { 
        return menuFacade.getAllCategories(); 
    }
    public List<MenuItem> getAvailableItems() { 
        return menuFacade.getAllMenuItems().stream().filter(MenuItem::getIsAvailable).toList(); 
    }
    public List<CafeTable> getAllTables() { return tableFacade.getAllTables(); }
    public int getCartItemCount() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    // For welcome screen selection
    private Long selectedTableId;
    public Long getSelectedTableId() { return selectedTableId; }
    public void setSelectedTableId(Long selectedTableId) { this.selectedTableId = selectedTableId; }
    
    public void setTableAndEnter() {
        if (selectedTableId != null) {
            // Masa doluluk kontrolü
            if (orderFacade.isTableOccupied(selectedTableId)) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Masa Dolu", "Bu masa şu an başka bir müşterimiz tarafından kullanılıyor."));
                return;
            }
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?table=" + selectedTableId);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void resetTable() {
        this.currentTable = null;
        this.tableId = null;
        this.cart.clear();
        this.activeOrderId = null;
        this.activeOrder = null;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.xhtml");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
