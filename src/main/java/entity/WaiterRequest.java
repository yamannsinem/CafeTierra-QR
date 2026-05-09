package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiter_requests")
public class WaiterRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private CafeTable cafeTable;

    @Column(nullable = false)
    private LocalDateTime requestTime;

    private Boolean resolved = false;

    public WaiterRequest() {
        this.requestTime = LocalDateTime.now();
    }

    public WaiterRequest(CafeTable cafeTable) {
        this();
        this.cafeTable = cafeTable;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CafeTable getCafeTable() { return cafeTable; }
    public void setCafeTable(CafeTable cafeTable) { this.cafeTable = cafeTable; }
    public LocalDateTime getRequestTime() { return requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime; }
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
}
