package model;

import java.sql.Date;
import java.sql.Time;

public class Messaggio {
    private int codiceMSG;
    private String parole;
    private Date dataInvio;
    private Time oraInvio;
    private String mittente;
    private int destinatario;

    public Messaggio() {}

    public int getCodiceMSG() {
        return codiceMSG;
    }
    public void setCodiceMSG(int codiceMSG) {
        this.codiceMSG = codiceMSG;
    }

    public String getParole() {
        return parole;
    }
    public void setParole(String parole) {
        this.parole = parole;
    }

    public Date getDataInvio() {
        return dataInvio;
    }
    public void setDataInvio(Date dataInvio) {
        this.dataInvio = dataInvio;
    }

    public Time getOraInvio() {
        return oraInvio;
    }
    public void setOraInvio(Time oraInvio) {
        this.oraInvio = oraInvio;
    }

    public String getMittente() {
        return mittente;
    }
    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public int getDestinatario() {
        return destinatario;
    }
    public void setDestinatario(int destinatario) {}
}
