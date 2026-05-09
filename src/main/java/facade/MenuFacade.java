package facade;

import entity.Category;
import entity.MenuItem;
import facadeLocal.MenuFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MenuFacade implements MenuFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    @Override
    public void createCategory(Category category) {
        em.persist(category);
    }

    @Override
    public void updateCategory(Category category) {
        em.merge(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = em.find(Category.class, id);
        if (category != null) {
            em.remove(category);
        }
    }

    @Override
    public Category findCategoryById(Long id) {
        return em.find(Category.class, id);
    }

    @Override
    public List<Category> getAllCategories() {
        return em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class).getResultList();
    }

    @Override
    public void createMenuItem(MenuItem menuItem) {
        em.persist(menuItem);
    }

    @Override
    public void updateMenuItem(MenuItem menuItem) {
        em.merge(menuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = em.find(MenuItem.class, id);
        if (menuItem != null) {
            em.remove(menuItem);
        }
    }

    @Override
    public MenuItem findMenuItemById(Long id) {
        return em.find(MenuItem.class, id);
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return em.createQuery("SELECT m FROM MenuItem m ORDER BY m.category.name, m.name", MenuItem.class).getResultList();
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(Long categoryId) {
        return em.createQuery("SELECT m FROM MenuItem m WHERE m.category.id = :categoryId ORDER BY m.name", MenuItem.class)
                 .setParameter("categoryId", categoryId)
                 .getResultList();
    }

    @Override
    public List<MenuItem> getAvailableMenuItemsByCategory(Long categoryId) {
        return em.createQuery("SELECT m FROM MenuItem m WHERE m.category.id = :categoryId AND m.isAvailable = true ORDER BY m.name", MenuItem.class)
                 .setParameter("categoryId", categoryId)
                 .getResultList();
    }
}
