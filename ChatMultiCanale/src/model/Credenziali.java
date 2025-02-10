package model;

public class Credenziali {
    private final String username;
    private final String password;
    private final Ruolo ruolo;
    private final String codiceFiscale;

    public Credenziali(String username, String password, Ruolo ruolo, String codiceFiscale) {
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
        this.codiceFiscale = codiceFiscale;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
}
