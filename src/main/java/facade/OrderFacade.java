package facade;

import entity.CafeOrder;
import entity.WaiterRequest;
import enums.OrderStatus;
import facadelocal.OrderFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrderFacade extends AbstractFacade<CafeOrder> implements OrderFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    public OrderFacade() {
        super(CafeOrder.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void createOrder(CafeOrder order) {
        if (order == null) {
            return;
        }
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setCafeOrder(order));
        }
        create(order);
    }

    @Override
    public CafeOrder findOrderById(Long id) {
        if (id == null) {
            return null;
        }
        return find(id);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        if (orderId == null || newStatus == null) {
            return;
        }
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
        if (request == null) {
            return;
        }
        em.persist(request);
    }

    @Override
    public List<WaiterRequest> getActiveWaiterRequests() {
        return em.createQuery("SELECT w FROM WaiterRequest w WHERE w.resolved = false ORDER BY w.requestTime ASC", WaiterRequest.class).getResultList();
    }

    @Override
    public void resolveWaiterRequest(Long id) {
        if (id == null) {
            return;
        }
        WaiterRequest req = em.find(WaiterRequest.class, id);
        if (req != null) {
            req.setResolved(true);
            em.merge(req);
        }
    }

    @Override
    public boolean isTableOccupied(Long tableId) {
        if (tableId == null) {
            return false;
        }
        Long count = em.createQuery("SELECT COUNT(o) FROM CafeOrder o WHERE o.cafeTable.id = :tId AND o.status != :deliveredStatus", Long.class)
                .setParameter("tId", tableId)
                .setParameter("deliveredStatus", OrderStatus.DELIVERED)
                .getSingleResult();
        return count > 0;
    }
}
