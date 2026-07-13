package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, String nameEn) {
        this.name = name;
        this.nameEn = nameEn;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }
}
