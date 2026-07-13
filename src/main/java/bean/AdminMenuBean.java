package bean;

import entity.Category;
import entity.MenuItem;
import facadelocal.MenuFacadeLocal;
import util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named
@ViewScoped
public class AdminMenuBean implements Serializable {

    @EJB
    private MenuFacadeLocal menuFacade;

    @Inject
    private MenuCacheBean menuCacheBean;

    private List<Category> categories;
    private List<MenuItem> menuItems;
    private Category newCategory = new Category();
    private MenuItem newMenuItem = new MenuItem();
    private Long selectedCategoryId;

    @PostConstruct
    public void init() {
        refreshMenu();
    }

    public void refreshMenu() {
        categories = menuFacade.getAllCategories();
        menuItems = menuFacade.getAllMenuItems();
    }

    public void addCategory() {
        if (newCategory.getName() == null || newCategory.getName().trim().isEmpty()) {
            FacesUtil.addErrorMessage("Hata", "Kategori adı boş olamaz.");
            return;
        }
        menuFacade.createCategory(newCategory);
        newCategory = new Category();
        refreshMenu();
        if (menuCacheBean != null) {
            menuCacheBean.refresh();
        }
        FacesUtil.addInfoMessage("Eklendi", "Kategori eklendi.");
    }

    public void deleteCategory(Long id) {
        if (id == null) {
            return;
        }
        try {
            menuFacade.deleteCategory(id);
            refreshMenu();
            if (menuCacheBean != null) {
                menuCacheBean.refresh();
            }
            FacesUtil.addInfoMessage("Silindi", "Kategori başarıyla silindi.");
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Hata", "Kategori silinemedi. İçinde ürünler olabilir.");
        }
    }

    public void addMenuItem() {
        if (newMenuItem.getName() == null || newMenuItem.getName().trim().isEmpty()) {
            FacesUtil.addErrorMessage("Hata", "Ürün ismi boş olamaz.");
            return;
        }
        if (newMenuItem.getPrice() == null || newMenuItem.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            FacesUtil.addErrorMessage("Hata", "Geçerli bir ürün fiyatı girin.");
            return;
        }
        if (selectedCategoryId == null) {
            FacesUtil.addErrorMessage("Hata", "Lütfen bir kategori seçin.");
            return;
        }
        Category cat = menuFacade.findCategoryById(selectedCategoryId);
        if (cat == null) {
            FacesUtil.addErrorMessage("Hata", "Seçilen kategori bulunamadı.");
            return;
        }
        newMenuItem.setCategory(cat);
        menuFacade.createMenuItem(newMenuItem);
        newMenuItem = new MenuItem();
        selectedCategoryId = null; // Reset selection
        refreshMenu();
        if (menuCacheBean != null) {
            menuCacheBean.refresh();
        }
        FacesUtil.addInfoMessage("Eklendi", "Ürün başarıyla eklendi.");
    }

    public void toggleAvailability(MenuItem item) {
        if (item != null) {
            item.setIsAvailable(!item.getIsAvailable());
            menuFacade.updateMenuItem(item);
            refreshMenu();
            if (menuCacheBean != null) {
                menuCacheBean.refresh();
            }
        }
    }

    public void updateMenuItemPrice(MenuItem item) {
        if (item == null || item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            FacesUtil.addErrorMessage("Hata", "Geçersiz ürün fiyatı.");
            return;
        }
        menuFacade.updateMenuItem(item);
        refreshMenu();
        if (menuCacheBean != null) {
            menuCacheBean.refresh();
        }
        FacesUtil.addInfoMessage("Güncellendi", "Ürün fiyatı güncellendi.");
    }

    public void deleteMenuItem(Long id) {
        if (id != null) {
            menuFacade.deleteMenuItem(id);
            refreshMenu();
            if (menuCacheBean != null) {
                menuCacheBean.refresh();
            }
            FacesUtil.addInfoMessage("Silindi", "Ürün başarıyla silindi.");
        }
    }

    // Getters and Setters
    public List<Category> getCategories() { return categories; }
    public List<MenuItem> getMenuItems() { return menuItems; }
    public Category getNewCategory() { return newCategory; }
    public void setNewCategory(Category newCategory) { this.newCategory = newCategory; }
    public MenuItem getNewMenuItem() { return newMenuItem; }
    public void setNewMenuItem(MenuItem newMenuItem) { this.newMenuItem = newMenuItem; }
    public Long getSelectedCategoryId() { return selectedCategoryId; }
    public void setSelectedCategoryId(Long selectedCategoryId) { this.selectedCategoryId = selectedCategoryId; }
}
