package bean;

import entity.CafeOrder;
import entity.WaiterRequest;
import enums.OrderStatus;
import facadelocal.OrderFacadeLocal;
import util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminOrderBean implements Serializable {

    @EJB
    private OrderFacadeLocal orderFacade;

    private List<CafeOrder> activeOrders;
    private List<WaiterRequest> waiterRequests;

    @PostConstruct
    public void init() {
        refreshData();
    }

    public void refreshData() {
        refreshOrders();
        refreshWaiterRequests();
    }

    public void refreshWaiterRequests() {
        waiterRequests = orderFacade.getActiveWaiterRequests();
    }

    public void resolveWaiterRequest(Long id) {
        if (id != null) {
            orderFacade.resolveWaiterRequest(id);
            refreshWaiterRequests();
        }
    }

    public void refreshOrders() {
        activeOrders = orderFacade.getActiveOrders();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        if (orderId == null || status == null) {
            FacesUtil.addErrorMessage("Hata", "Sipariş veya durum bilgisi eksik.");
            return;
        }
        orderFacade.updateOrderStatus(orderId, status);
        refreshOrders();
        FacesUtil.addInfoMessage("Güncellendi", "Sipariş durumu güncellendi.");
    }

    // Getters and Setters
    public List<WaiterRequest> getWaiterRequests() { return waiterRequests; }
    public List<CafeOrder> getActiveOrders() { return activeOrders; }
}
