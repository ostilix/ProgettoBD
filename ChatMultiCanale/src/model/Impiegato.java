package model;

public class Impiegato {
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private int ruolo;
    private String username;
    private String password;

    public Impiegato(String codiceFiscale, String nome, String cognome, int ruolo, String username, String password) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
        this.username = username;
        this.password = password;
    }

    public Impiegato(){}

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Ruolo getRuolo() {
        return Ruolo.fromInt(ruolo);
    }
    public void setRuolo(Ruolo ruolo) {
        if (ruolo == null) {
            throw new IllegalArgumentException("Errore: Ruolo è impostato a null");
        }else{
            this.ruolo = ruolo.getId();
        }
    }

    public int getRuoloId() {
        return Ruolo.fromInt(ruolo).getId();
    }
    public void setRuolo(int ruoloId) {
        Ruolo ruolo = Ruolo.fromInt(ruoloId);
        if (ruolo != null) {
            this.ruolo = ruoloId;
        } else {
            throw new IllegalArgumentException("ID del ruolo non valido: " + ruoloId);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
