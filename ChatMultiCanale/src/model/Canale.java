package model;

public class Canale {
    private int codiceCanale;
    private String nome;
    private Canale.Tipo tipo;
    private int progetto;

    public Canale(String nome, int codiceProgetto) {
        this.nome = nome;
        this.progetto = codiceProgetto;
    }

    public Canale() {}

    public enum Tipo{
        Pubblico(1),
        Privato(2);

        private final int id;

        Tipo(int id) {
            this.id = id;
        }

        public static Canale.Tipo fromId(int id){
            for(Canale.Tipo tipo : Canale.Tipo.values()){
                if(tipo.getId() == id){
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Errore: Id non valido, è impostato a " + id);
        }

        public int getId() {
            return id;
        }
    }

    public int getCodiceCanale() {
        return codiceCanale;
    }
    public void setCodiceCanale(int codiceCanale) {
        this.codiceCanale = codiceCanale;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Canale.Tipo getTipo() {
        return tipo;
    }
    public void setTipo(Canale.Tipo tipo) {
        this.tipo = tipo;
    }

    public int getProgetto() {
        return progetto;
    }
    public void setProgetto(int progetto) {
        this.progetto = progetto;
    }
}


