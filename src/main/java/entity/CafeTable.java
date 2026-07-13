package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cafe_tables")
public class CafeTable extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String tableNumber;

    @Column(nullable = false)
    private Boolean isOccupied = false;

    public CafeTable() {}

    public CafeTable(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    // Getters and Setters
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public Boolean getIsOccupied() { return isOccupied; }
    public void setIsOccupied(Boolean isOccupied) { this.isOccupied = isOccupied; }
}
