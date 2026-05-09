package facadeLocal;

import entity.User;
import jakarta.ejb.Local;

@Local
public interface AuthFacadeLocal {
    User login(String username, String password);
    void initDefaultAdmin();
}
