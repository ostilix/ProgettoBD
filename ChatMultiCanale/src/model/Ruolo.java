package model;

public enum Ruolo {
    AMMINISTRATORE(1),
    CAPOPROGETTO(2),
    DIPENDENTEBASE(3);

    private final int id;

    Ruolo(int id) {
        this.id = id;
    }

    public static Ruolo fromInt(int id){
        for(Ruolo type : values()){
            if(type.getId() == id){
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
