package facade;

import entity.CafeComment;
import facadelocal.CommentFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CommentFacade extends AbstractFacade<CafeComment> implements CommentFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    public CommentFacade() {
        super(CafeComment.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void createComment(CafeComment comment) {
        create(comment);
    }

    @Override
    public List<CafeComment> getAllComments() {
        return em.createQuery("SELECT c FROM CafeComment c ORDER BY c.createdAt DESC", CafeComment.class)
                 .getResultList();
    }
}
