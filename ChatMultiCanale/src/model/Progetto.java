package model;

import java.sql.Date;

public class Progetto {
    private int codiceProgetto;
    private String nome;
    private Date dataScadenza;
    private String capoProgetto;

    public Progetto(String nome, Date dataScadenza) {
        this.nome = nome;
        this.dataScadenza = dataScadenza;
    }

    public Progetto() {

    }

    public int getCodiceProgetto() {
        return codiceProgetto;
    }
    public void setCodiceProgetto(int codiceProgetto) {
        this.codiceProgetto = codiceProgetto;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getCapoProgetto() {
        return capoProgetto;
    }
    public void setCapoProgetto(String capoProgetto) {
        this.capoProgetto = capoProgetto;
    }
}


