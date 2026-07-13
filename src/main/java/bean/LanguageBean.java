package bean;

import entity.Category;
import entity.MenuItem;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LanguageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String language = "TR"; // Default is Turkish

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void toggleLanguage() {
        if ("TR".equals(language)) {
            language = "EN";
        } else {
            language = "TR";
        }
    }


    public String tr(String trText, String enText) {
        if ("EN".equals(language)) {
            return enText != null ? enText : trText;
        }
        return trText;
    }

    /**
     * Translates category name.
     */
    public String getCategoryName(Category category) {
        if (category == null) {
            return "";
        }
        if ("EN".equals(language) && category.getNameEn() != null && !category.getNameEn().isEmpty()) {
            return category.getNameEn();
        }
        return category.getName();
    }

    /**
     * Translates menu item name.
     */
    public String getItemName(MenuItem item) {
        if (item == null) {
            return "";
        }
        if ("EN".equals(language) && item.getNameEn() != null && !item.getNameEn().isEmpty()) {
            return item.getNameEn();
        }
        return item.getName();
    }

    /**
     * Translates menu item description.
     */
    public String getItemDescription(MenuItem item) {
        if (item == null) {
            return "";
        }
        if ("EN".equals(language) && item.getDescriptionEn() != null && !item.getDescriptionEn().isEmpty()) {
            return item.getDescriptionEn();
        }
        return item.getDescription();
    }
}
