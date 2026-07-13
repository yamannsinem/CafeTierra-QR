package facadelocal;

import entity.CafeOrder;
import entity.WaiterRequest;
import enums.OrderStatus;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface OrderFacadeLocal {
    void createOrder(CafeOrder order);
    CafeOrder findOrderById(Long id);
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    List<CafeOrder> getActiveOrders();
    List<CafeOrder> getAllOrders();
    void createWaiterRequest(WaiterRequest request);
    List<WaiterRequest> getActiveWaiterRequests();
    void resolveWaiterRequest(Long id);
    boolean isTableOccupied(Long tableId);
}
