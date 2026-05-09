package bean;

import entity.User;
import facadeLocal.AuthFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private AuthFacadeLocal authFacade;

    private String username;
    private String password;
    private User currentUser;

    @PostConstruct
    public void init() {
        authFacade.initDefaultAdmin(); // Create default admin if not exists
    }

    public void login() {
        User user = authFacade.login(username, password);
        if (user != null) {
            currentUser = user;
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("admin.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı adı veya şifre yanlış."));
        }
    }

    public void logout() {
        currentUser = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void checkLogin() {
        if (currentUser == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public User getCurrentUser() { return currentUser; }
}
