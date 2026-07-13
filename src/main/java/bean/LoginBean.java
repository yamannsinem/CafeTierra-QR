package bean;

import entity.User;
import facadelocal.AuthFacadeLocal;
import util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private AuthFacadeLocal authFacade;

    private String username;
    private String password;
    private User currentUser;

    public void login() {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            FacesUtil.addErrorMessage("Hata", "Kullanıcı adı ve şifre alanları boş bırakılamaz.");
            return;
        }
        User user = authFacade.login(username.trim(), password);
        if (user != null) {
            // Oturum Sabitleme (Session Fixation) Koruması: Oturum kimliğini yenile
            jakarta.servlet.http.HttpServletRequest request = (jakarta.servlet.http.HttpServletRequest) 
                jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.changeSessionId();

            currentUser = user;
            jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
            FacesUtil.redirect("admin.xhtml");
        } else {
            FacesUtil.addErrorMessage("Hata", "Kullanıcı adı veya şifre yanlış.");
        }
    }

    public void logout() {
        currentUser = null;
        jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        FacesUtil.invalidateSession();
        FacesUtil.redirect("login.xhtml");
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void checkLogin() {
        if (currentUser == null) {
            FacesUtil.redirect("login.xhtml");
        }
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public User getCurrentUser() { return currentUser; }
}
