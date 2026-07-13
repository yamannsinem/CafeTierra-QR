package bean;

import entity.CafeTable;
import facadelocal.TableFacadeLocal;
import util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminTableBean implements Serializable {

    @EJB
    private TableFacadeLocal tableFacade;

    private List<CafeTable> tables;
    private CafeTable newTable = new CafeTable();

    @PostConstruct
    public void init() {
        refreshTables();
    }

    public void refreshTables() {
        tables = tableFacade.getAllTables();
    }

    public void addTable() {
        if (newTable.getTableNumber() == null || newTable.getTableNumber().trim().isEmpty()) {
            FacesUtil.addErrorMessage("Hata", "Masa adı/numarası boş olamaz.");
            return;
        }
        tableFacade.createTable(newTable);
        newTable = new CafeTable();
        refreshTables();
        FacesUtil.addInfoMessage("Eklendi", "Masa eklendi.");
    }

    public void deleteTable(Long id) {
        if (id != null) {
            tableFacade.deleteTable(id);
            refreshTables();
            FacesUtil.addInfoMessage("Silindi", "Masa silindi.");
        }
    }

    public List<CafeTable> getTables() { return tables; }
    public CafeTable getNewTable() { return newTable; }
    public void setNewTable(CafeTable newTable) { this.newTable = newTable; }
}
