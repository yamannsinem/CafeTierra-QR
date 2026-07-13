package bean;

import entity.Category;
import entity.MenuItem;
import facadelocal.MenuFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class MenuCacheBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MenuCacheBean.class.getName());

    @EJB
    private MenuFacadeLocal menuFacade;

    private List<Category> categories;
    private List<MenuItem> availableItems;

    @PostConstruct
    public void init() {
        refresh();
    }

    public synchronized void refresh() {
        LOGGER.info("Refreshing menu cache from database...");
        try {
            categories = menuFacade.getAllCategories();
            availableItems = menuFacade.getAllMenuItems().stream()
                    .filter(MenuItem::getIsAvailable)
                    .toList();
        } catch (Exception e) {
            LOGGER.severe("Failed to refresh menu cache: " + e.getMessage());
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<MenuItem> getAvailableItems() {
        return availableItems;
    }

    public List<MenuItem> getPopularItems() {
        if (availableItems == null) {
            return java.util.Collections.emptyList();
        }
        return availableItems.stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsPopular()))
                .toList();
    }
}
