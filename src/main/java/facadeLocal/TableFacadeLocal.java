package facadeLocal;

import entity.CafeTable;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface TableFacadeLocal {
    void createTable(CafeTable table);
    void deleteTable(Long id);
    List<CafeTable> getAllTables();
    CafeTable findTableById(Long id);
}
