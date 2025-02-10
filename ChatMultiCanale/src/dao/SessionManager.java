package dao;

import model.Credenziali;

public class SessionManager {

    private static SessionManager sessionManager = null;

    private Credenziali credenzialiUtente;
    private boolean authenticated;

    private SessionManager() {
        this.authenticated = false;
    }

    public static synchronized SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public void authenticateUser(Credenziali credentials) {
        if (credentials != null) {
            this.credenzialiUtente = credentials;
            this.authenticated = true;
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUserCf() {
        return isAuthenticated() ? credenzialiUtente.getCodiceFiscale() : "Utente non autenticato";
    }
}
