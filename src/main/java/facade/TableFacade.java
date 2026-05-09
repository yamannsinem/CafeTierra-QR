package facade;

import entity.CafeTable;
import facadeLocal.TableFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TableFacade implements TableFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    @Override
    public void createTable(CafeTable table) {
        em.persist(table);
    }

    @Override
    public void deleteTable(Long id) {
        CafeTable table = em.find(CafeTable.class, id);
        if (table != null) {
            em.remove(table);
        }
    }

    @Override
    public List<CafeTable> getAllTables() {
        return em.createQuery("SELECT t FROM CafeTable t ORDER BY t.tableNumber", CafeTable.class).getResultList();
    }

    @Override
    public CafeTable findTableById(Long id) {
        return em.find(CafeTable.class, id);
    }
}
