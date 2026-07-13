package facade;

import entity.User;
import facadelocal.AuthFacadeLocal;
import security.PasswordUtil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AuthFacade extends AbstractFacade<User> implements AuthFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    public AuthFacade() {
        super(User.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        
        if (users.isEmpty()) {
            return null;
        }
        User user = users.get(0);
        if (PasswordUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
