package bean;

import entity.CafeOrder;
import entity.CafeTable;
import entity.Category;
import entity.MenuItem;
import entity.WaiterRequest;
import enums.OrderStatus;
import facadeLocal.MenuFacadeLocal;
import facadeLocal.OrderFacadeLocal;
import facadeLocal.TableFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminDashboardBean implements Serializable {

    @EJB
    private OrderFacadeLocal orderFacade;
    @EJB
    private MenuFacadeLocal menuFacade;
    @EJB
    private TableFacadeLocal tableFacade;

    // Orders
    private List<CafeOrder> activeOrders;
    
    // Tables
    private List<CafeTable> tables;
    private CafeTable newTable = new CafeTable();

    // Menu
    private List<Category> categories;
    private List<MenuItem> menuItems;
    private Category newCategory = new Category();
    private MenuItem newMenuItem = new MenuItem();
    private Long selectedCategoryId;

    // Waiter Requests
    private List<WaiterRequest> waiterRequests;

    @PostConstruct
    public void init() {
        refreshData();
    }

    public void refreshData() {
        refreshOrders();
        refreshTables();
        refreshMenu();
        refreshWaiterRequests();
    }

    // --- Waiter Logic ---
    public void refreshWaiterRequests() {
        waiterRequests = orderFacade.getActiveWaiterRequests();
    }

    public void resolveWaiterRequest(Long id) {
        orderFacade.resolveWaiterRequest(id);
        refreshWaiterRequests();
    }

    // --- Order Logic ---
    public void refreshOrders() {
        activeOrders = orderFacade.getActiveOrders();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        orderFacade.updateOrderStatus(orderId, status);
        refreshOrders();
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Güncellendi", "Sipariş durumu güncellendi."));
    }

    // --- Table Logic ---
    public void refreshTables() {
        tables = tableFacade.getAllTables();
    }

    public void addTable() {
        tableFacade.createTable(newTable);
        newTable = new CafeTable();
        refreshTables();
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Eklendi", "Masa eklendi."));
    }

    public void deleteTable(Long id) {
        tableFacade.deleteTable(id);
        refreshTables();
    }

    // --- Menu Logic ---
    public void refreshMenu() {
        categories = menuFacade.getAllCategories();
        menuItems = menuFacade.getAllMenuItems();
    }

    public void addCategory() {
        menuFacade.createCategory(newCategory);
        newCategory = new Category();
        refreshMenu();
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Eklendi", "Kategori eklendi."));
    }

    public void deleteCategory(Long id) {
        try {
            menuFacade.deleteCategory(id);
            refreshMenu();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Silindi", "Kategori başarıyla silindi."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kategori silinemedi. İçinde ürünler olabilir."));
        }
    }

    public void addMenuItem() {
        if (selectedCategoryId != null) {
            Category cat = menuFacade.findCategoryById(selectedCategoryId);
            newMenuItem.setCategory(cat);
            menuFacade.createMenuItem(newMenuItem);
            newMenuItem = new MenuItem();
            selectedCategoryId = null; // Reset selection
            refreshMenu();
        }
    }

    public void toggleAvailability(MenuItem item) {
        item.setIsAvailable(!item.getIsAvailable());
        menuFacade.updateMenuItem(item);
        refreshMenu();
    }

    public void updateMenuItemPrice(MenuItem item) {
        menuFacade.updateMenuItem(item);
        refreshMenu();
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Güncellendi", "Ürün fiyatı güncellendi."));
    }

    public void deleteMenuItem(Long id) {
        menuFacade.deleteMenuItem(id);
        refreshMenu();
    }

    // Getters and Setters
    public List<WaiterRequest> getWaiterRequests() { return waiterRequests; }
    public List<CafeOrder> getActiveOrders() { return activeOrders; }
    public List<CafeTable> getTables() { return tables; }
    public CafeTable getNewTable() { return newTable; }
    public void setNewTable(CafeTable newTable) { this.newTable = newTable; }
    public List<Category> getCategories() { return categories; }
    public List<MenuItem> getMenuItems() { return menuItems; }
    public Category getNewCategory() { return newCategory; }
    public void setNewCategory(Category newCategory) { this.newCategory = newCategory; }
    public MenuItem getNewMenuItem() { return newMenuItem; }
    public void setNewMenuItem(MenuItem newMenuItem) { this.newMenuItem = newMenuItem; }
    public Long getSelectedCategoryId() { return selectedCategoryId; }
    public void setSelectedCategoryId(Long selectedCategoryId) { this.selectedCategoryId = selectedCategoryId; }
}
