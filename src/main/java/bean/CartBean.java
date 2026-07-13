package bean;

import entity.MenuItem;
import util.FacesUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class CartBean implements Serializable {

    private Map<MenuItem, Integer> cart = new HashMap<>();

    public void addToCart(MenuItem item) {
        if (item != null) {
            cart.put(item, cart.getOrDefault(item, 0) + 1);
            FacesUtil.addInfoMessage("Sepete Eklendi", item.getName() + " sepete eklendi.");
        }
    }
    
    public void removeFromCart(MenuItem item) {
        if (item != null && cart.containsKey(item)) {
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

    public int getCartItemCount() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void clear() {
        cart.clear();
    }

    public Map<MenuItem, Integer> getCartMap() {
        return cart;
    }

    public boolean isEmpty() {
        return cart.isEmpty();
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
        if (cart.isEmpty()) {
            FacesUtil.addWarnMessage("Uyarı", "Sepetiniz boş.");
            return;
        }
        FacesUtil.redirect("payment.xhtml");
    }
}
