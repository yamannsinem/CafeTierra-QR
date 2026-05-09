package facade;

import entity.CafeOrder;
import entity.WaiterRequest;
import enums.OrderStatus;
import facadeLocal.OrderFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrderFacade implements OrderFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    @Override
    public void createOrder(CafeOrder order) {
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setCafeOrder(order));
        }
        em.persist(order);
    }

    @Override
    public CafeOrder findOrderById(Long id) {
        return em.find(CafeOrder.class, id);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        CafeOrder order = em.find(CafeOrder.class, orderId);
        if (order != null) {
            order.setStatus(newStatus);
            em.merge(order);
        }
    }

    @Override
    public List<CafeOrder> getActiveOrders() {
        return em.createQuery("SELECT o FROM CafeOrder o WHERE o.status != :deliveredStatus ORDER BY o.orderTime DESC", CafeOrder.class)
                 .setParameter("deliveredStatus", OrderStatus.DELIVERED)
                 .getResultList();
    }

    @Override
    public List<CafeOrder> getAllOrders() {
        return em.createQuery("SELECT o FROM CafeOrder o ORDER BY o.orderTime DESC", CafeOrder.class).getResultList();
    }

    @Override
    public void createWaiterRequest(WaiterRequest request) {
        em.persist(request);
    }

    @Override
    public List<WaiterRequest> getActiveWaiterRequests() {
        return em.createQuery("SELECT w FROM WaiterRequest w WHERE w.resolved = false ORDER BY w.requestTime ASC", WaiterRequest.class).getResultList();
    }

    @Override
    public void resolveWaiterRequest(Long id) {
        WaiterRequest req = em.find(WaiterRequest.class, id);
        if (req != null) {
            req.setResolved(true);
            em.merge(req);
        }
    }

    @Override
    public boolean isTableOccupied(Long tableId) {
        Long count = em.createQuery("SELECT COUNT(o) FROM CafeOrder o WHERE o.cafeTable.id = :tId AND o.status != :deliveredStatus", Long.class)
                .setParameter("tId", tableId)
                .setParameter("deliveredStatus", OrderStatus.DELIVERED)
                .getSingleResult();
        return count > 0;
    }
}
