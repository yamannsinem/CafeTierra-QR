package util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacesUtil {

    private static final Logger LOGGER = Logger.getLogger(FacesUtil.class.getName());

    /**
     * Redirects the current user session to the specified URL/page.
     *
     * @param url target page (e.g., "welcome.xhtml")
     */
    public static void redirect(String url) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.getExternalContext() != null) {
            try {
                facesContext.getExternalContext().redirect(url);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to redirect to: " + url, e);
            }
        } else {
            LOGGER.log(Level.WARNING, "FacesContext or ExternalContext is null. Cannot redirect to: {0}", url);
        }
    }

    /**
     * Invalidates the current user session.
     */
    public static void invalidateSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.getExternalContext() != null) {
            facesContext.getExternalContext().invalidateSession();
        }
    }

    /**
     * Retrieves a request parameter by name.
     *
     * @param paramName the parameter key
     * @return the parameter value, or null if not found
     */
    public static String getRequestParameter(String paramName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.getExternalContext() != null) {
            return facesContext.getExternalContext().getRequestParameterMap().get(paramName);
        }
        return null;
    }

    /**
     * Adds an INFO message to the JSF context.
     *
     * @param summary short description
     * @param detail  full message detail
     */
    public static void addInfoMessage(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    /**
     * Adds a WARNING message to the JSF context.
     *
     * @param summary short description
     * @param detail  full message detail
     */
    public static void addWarnMessage(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
    }

    /**
     * Adds an ERROR message to the JSF context.
     *
     * @param summary short description
     * @param detail  full message detail
     */
    public static void addErrorMessage(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    private static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            facesContext.addMessage(null, new FacesMessage(severity, summary, detail));
        }
    }
}
