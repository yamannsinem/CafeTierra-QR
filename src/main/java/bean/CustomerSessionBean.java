package bean;

import entity.CafeOrder;
import entity.CafeTable;
import entity.WaiterRequest;
import facadelocal.OrderFacadeLocal;
import facadelocal.TableFacadeLocal;
import util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class CustomerSessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CustomerSessionBean.class.getName());

    @EJB
    private TableFacadeLocal tableFacade;
    @EJB
    private OrderFacadeLocal orderFacade;

    @Inject
    private CartBean cartBean;

    private Long tableId;
    private CafeTable currentTable;
    
    // Aktif sipariş takibi
    private Long activeOrderId;
    private CafeOrder activeOrder;

    // For welcome screen selection
    private Long selectedTableId;

    // Oturum başlangıcında açılış ekranını (splash) bir kere zorunlu kılmak için
    private boolean splashShown = false;
    private Long targetTableId;

    @PostConstruct
    public void init() {
        // Categories and menu items are loaded from MenuCacheBean
    }

    public void checkTable() {
        String tableParam = FacesUtil.getRequestParameter("table");
        if (tableParam != null && !tableParam.isEmpty()) {
            try {
                this.tableId = Long.parseLong(tableParam);
                this.currentTable = tableFacade.findTableById(this.tableId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid table parameter: " + tableParam, e);
            }
        }
        
        if (!splashShown) {
            if (this.tableId != null) {
                this.targetTableId = this.tableId;
            }
            splashShown = true;
            FacesUtil.redirect("splash.xhtml");
            return;
        }
        
        if (this.currentTable == null) {
            FacesUtil.redirect("splash.xhtml");
        }
    }

    public void redirectToRandomFreeTable() {
        if (targetTableId != null) {
            Long tempId = targetTableId;
            targetTableId = null; // Clear it
            try {
                this.currentTable = tableFacade.findTableById(tempId);
                this.tableId = tempId;
                FacesUtil.redirect("index.xhtml?table=" + tempId);
                return;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to restore target table: " + tempId, e);
            }
        }

        List<CafeTable> allTables = tableFacade.getAllTables();
        if (allTables == null || allTables.isEmpty()) {
            LOGGER.log(Level.WARNING, "No tables found in the database to redirect.");
            return;
        }
        
        // Filter free tables
        java.util.List<CafeTable> freeTables = new java.util.ArrayList<>();
        for (CafeTable t : allTables) {
            if (!orderFacade.isTableOccupied(t.getId())) {
                freeTables.add(t);
            }
        }
        
        CafeTable targetTable;
        if (!freeTables.isEmpty()) {
            int randomIndex = new java.util.Random().nextInt(freeTables.size());
            targetTable = freeTables.get(randomIndex);
        } else {
            int randomIndex = new java.util.Random().nextInt(allTables.size());
            targetTable = allTables.get(randomIndex);
        }
        
        LOGGER.log(Level.INFO, "Auto-redirecting to random table: " + targetTable.getTableNumber() + " (ID: " + targetTable.getId() + ")");
        FacesUtil.redirect("index.xhtml?table=" + targetTable.getId());
    }

    public void callWaiter() {
        if (currentTable != null) {
            WaiterRequest req = new WaiterRequest(currentTable);
            orderFacade.createWaiterRequest(req);
            FacesUtil.addInfoMessage("Garson Çağrıldı", "İsteğiniz iletildi, garsonumuz en kısa sürede masanıza gelecektir.");
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

    public List<CafeTable> getAllTables() { 
        return tableFacade.getAllTables(); 
    }
    
    public void setTableAndEnter() {
        if (selectedTableId != null) {
            FacesUtil.redirect("index.xhtml?table=" + selectedTableId);
        }
    }

    public boolean isTableOccupied(Long id) {
        if (id == null) {
            return false;
        }
        return orderFacade.isTableOccupied(id);
    }
    
    public void resetTable() {
        this.currentTable = null;
        this.tableId = null;
        if (cartBean != null) {
            cartBean.clear();
        }
        this.activeOrderId = null;
        this.activeOrder = null;
        FacesUtil.redirect("welcome.xhtml");
    }

    public void clearSessionForLanding() {
        this.currentTable = null;
        this.tableId = null;
        this.activeOrderId = null;
        this.activeOrder = null;
        if (cartBean != null) {
            cartBean.clear();
        }
    }

    // Getters and Setters
    public CafeTable getCurrentTable() { return currentTable; }
    public Long getTableId() { return tableId; }
    public void setTableId(Long tableId) { this.tableId = tableId; }
    
    public Long getActiveOrderId() { return activeOrderId; }
    public void setActiveOrderId(Long activeOrderId) { this.activeOrderId = activeOrderId; }
    public void setActiveOrder(CafeOrder activeOrder) { this.activeOrder = activeOrder; }
    
    public Long getSelectedTableId() { return selectedTableId; }
    public void setSelectedTableId(Long selectedTableId) { this.selectedTableId = selectedTableId; }
}
