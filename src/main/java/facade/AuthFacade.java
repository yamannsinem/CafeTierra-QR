package facade;

import entity.User;
import facadeLocal.AuthFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AuthFacade implements AuthFacadeLocal {

    @PersistenceContext(unitName = "CafePU")
    private EntityManager em;

    @Override
    public User login(String username, String password) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();
        
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public void initDefaultAdmin() {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = 'admin'", User.class).getResultList();
        if (users.isEmpty()) {
            User admin = new User("admin", "admin123");
            em.persist(admin);
        }
    }
}
