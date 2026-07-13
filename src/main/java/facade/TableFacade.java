package facade;

import entity.CafeTable;
import facadelocal.TableFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TableFacade extends AbstractFacade<CafeTable> implements TableFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    public TableFacade() {
        super(CafeTable.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void createTable(CafeTable table) {
        create(table);
    }

    @Override
    public void deleteTable(Long id) {
        removeById(id);
    }

    @Override
    public List<CafeTable> getAllTables() {
        return em.createQuery("SELECT t FROM CafeTable t ORDER BY t.tableNumber", CafeTable.class).getResultList();
    }

    @Override
    public CafeTable findTableById(Long id) {
        return find(id);
    }
}
