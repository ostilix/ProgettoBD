package dao;

import exception.DAOException;
import model.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CapoProgettoDAO {

    public static List<Impiegato> listaImpiegati() throws DAOException{
        List<Impiegato> impiegati = new ArrayList<>();
        try{
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call visualizza_impiegati()");
            popolaListaImpiegati(impiegati, cs);
        }catch (SQLException e){
            throw new DAOException("Errore nel caricamento della lista degli impiegati: " + e.getMessage());
        }
        return impiegati;
    }

    public static List<Progetto> listaProgetti() throws DAOException{
        List<Progetto> progetti = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            SessionManager sessione = SessionManager.getInstance();

            CallableStatement cs = conn.prepareCall("call visualizza_progetti(?)");
            cs.setString(1, sessione.getUserCf());
            popolaListaProgetti(progetti, cs);
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero dei progetti: " + e.getMessage());
        }
        return progetti;
    }

    public static void creaCanale(Canale canale) throws DAOException{
        try{
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call crea_canale_pubblico(?,?)");

            cs.setString(1, canale.getNome());
            cs.setInt(2, canale.getProgetto());
            cs.executeUpdate();
        }catch (SQLException e){
            throw new DAOException("Errore nella creazione del canale: " + e.getMessage());

        }
    }

    public static void assegnaCanale(String codiceFiscale, int codiceCanale) throws DAOException{
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call aggiungi_al_canale(?,?)");

            cs.setString(1, codiceFiscale);
            cs.setInt(2, codiceCanale);
            cs.executeUpdate();
        }catch (SQLException e) {
            throw new DAOException("Errore: non è stato possibile aggiungere l'impiegato al canale: " + e.getMessage());
        }
    }

    public static void assegnaProgetto(String codiceFiscale, int codiceProgetto) throws DAOException{
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call aggiungi_al_progetto(?,?)");

            cs.setString(1, codiceFiscale);
            cs.setInt(2, codiceProgetto);
            cs.executeUpdate();
        }catch (SQLException e) {
            throw new DAOException("Errore: non è stato possibile aggiungere l'impiegato al canale: " + e.getMessage());
        }
    }

    public static List<Impiegato> listaMembriProgetto(int codiceProgetto) throws DAOException{
        List<Impiegato> impiegati = new ArrayList<>();
        try{
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call visualizza_membri_progetto(?)");
            cs.setInt(1, codiceProgetto);
            popolaListaImpiegati(impiegati, cs);
        }catch (SQLException e){
            throw new DAOException("Errore nel caricamento della lista dei canali: " + e.getMessage());
        }
        return impiegati;
    }

    public static List<Impiegato> listaAssentiCanale(int codiceCanale) throws DAOException{
        List<Impiegato> impiegati = new ArrayList<>();
        try{
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call visualizza_assenti_canale(?)");
            cs.setInt(1, codiceCanale);
            popolaListaImpiegati(impiegati, cs);
        }catch (SQLException e){
            throw new DAOException("Errore nel caricamento della lista degli impiegati non ancora aggiunti al canale: " + e.getMessage());
        }
        return impiegati;
    }

    public static List<Impiegato> listaAssentiProgetto(int codiceProgetto) throws DAOException{
        List<Impiegato> impiegati = new ArrayList<>();
        try{
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call visualizza_assenti_progetto(?)");
            cs.setInt(1, codiceProgetto);
            popolaListaImpiegati(impiegati, cs);
        }catch (SQLException e){
            throw new DAOException("Errore nel caricamento della lista degli impiegati non ancora aggiunti al progetto: " + e.getMessage());
        }
        return impiegati;
    }

    private static void popolaListaProgetti(List<Progetto> listaProgetti, CallableStatement cs) throws SQLException {
        ResultSet resultSet = cs.executeQuery();

        while (resultSet.next()){
            Progetto progetto = new Progetto();
            progetto.setCodiceProgetto(resultSet.getInt("CodiceProgetto"));
            progetto.setNome(resultSet.getString("NomeProgetto"));
            progetto.setDataScadenza(resultSet.getDate("DataScadenza"));
            progetto.setCapoProgetto(resultSet.getString("CapoProgetto"));

            listaProgetti.add(progetto);
        }
    }

    private static void popolaListaImpiegati(List<Impiegato> listaImpiegati, CallableStatement cs) throws SQLException {
        ResultSet resultSet = cs.executeQuery();

        while (resultSet.next()) {
            Impiegato impiegato = new Impiegato();
            impiegato.setCodiceFiscale(resultSet.getString("CodiceFiscale"));
            impiegato.setNome(resultSet.getString("Nome"));
            impiegato.setCognome(resultSet.getString("Cognome"));
            String ruoloString = resultSet.getString("Ruolo").replace(" ","").toUpperCase();
            Ruolo ruolo = Ruolo.valueOf(ruoloString);
            impiegato.setRuolo(ruolo.getId());

            listaImpiegati.add(impiegato);
        }
    }
}
