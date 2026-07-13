package facade;

import entity.Category;
import entity.MenuItem;
import facadelocal.MenuFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MenuFacade extends AbstractFacade<MenuItem> implements MenuFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    public MenuFacade() {
        super(MenuItem.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

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
        create(menuItem);
    }

    @Override
    public void updateMenuItem(MenuItem menuItem) {
        edit(menuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        removeById(id);
    }

    @Override
    public MenuItem findMenuItemById(Long id) {
        return find(id);
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
