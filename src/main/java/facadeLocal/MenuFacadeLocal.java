package facadelocal;

import entity.Category;
import entity.MenuItem;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface MenuFacadeLocal {
    void createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Long id);
    Category findCategoryById(Long id);
    List<Category> getAllCategories();
    void createMenuItem(MenuItem menuItem);
    void updateMenuItem(MenuItem menuItem);
    void deleteMenuItem(Long id);
    MenuItem findMenuItemById(Long id);
    List<MenuItem> getAllMenuItems();
    List<MenuItem> getMenuItemsByCategory(Long categoryId);
    List<MenuItem> getAvailableMenuItemsByCategory(Long categoryId);
}
