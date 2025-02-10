package dao;
import exception.DAOException;
import model.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;



public class AmministratoreDAO {

    public static void nuovoImpiegato(Impiegato impiegato) throws DAOException {
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call nuovo_impiegato(?,?,?,?,?,?)");
            cs.setString(1, impiegato.getCodiceFiscale());
            cs.setString(2, impiegato.getNome());
            cs.setString(3, impiegato.getCognome());
            cs.setInt(4, impiegato.getRuoloId());
            cs.setString(5, impiegato.getUsername());
            cs.setString(6, impiegato.getPassword());
            cs.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore nell'aggiunta di un nuovo impiegato: " + e.getMessage());
        }
    }

    public static void assegnaCapo(String codiceFiscale, int codiceProgetto) throws DAOException {
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call assegna_capo(?,?)");

            cs.setString(1, codiceFiscale);
            cs.setInt(2, codiceProgetto);
            cs.executeUpdate();
        }catch (SQLException e) {
            throw new DAOException("Errore: capo progetto non assegnato al progetto: " + e.getMessage());
        }
    }

    public static void creaProgetto(Progetto progetto) throws DAOException{
        try{
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call crea_progetto(?,?)");

            cs.setString(1, progetto.getNome());
            cs.setDate(2, progetto.getDataScadenza());
            cs.executeUpdate();
        }catch (SQLException e){
            throw new DAOException("Errore nella creazione del progetto: " + e.getMessage());

        }
    }
}

