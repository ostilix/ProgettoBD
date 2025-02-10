package dao;

import exception.DAOException;
import model.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImpiegatoDAO {

    public static List<Canale> listaCanali() throws DAOException{
        List<Canale> canali = new ArrayList<>();
        try{
            Connection conn = ConnectionFactory.getConnection();
            SessionManager sessione = SessionManager.getInstance();
            CallableStatement cs = conn.prepareCall("call visualizza_canali(?)");
            cs.setString(1, sessione.getUserCf());
            popolaListaCanali(canali, cs);
        }catch (SQLException e){
            throw new DAOException("Errore nel caricamento della lista dei canali: " + e.getMessage());
        }
        return canali;
    }

    public static List<Impiegato> listaMembriCanale(int codiceCanale) throws DAOException{
        List<Impiegato> impiegati = new ArrayList<>();
        try{
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("call visualizza_membri_canale(?)");
            cs.setInt(1, codiceCanale);
            popolaListaImpiegati(impiegati, cs);
        }catch (SQLException e){
            throw new DAOException("Errore nel caricamento della lista dei canali: " + e.getMessage());
        }
        return impiegati;
    }

    public static void inviaMessaggio(int codiceCanale, String testo ) throws DAOException{
        try {
            Connection conn = ConnectionFactory.getConnection();
            SessionManager sessione = SessionManager.getInstance();

            CallableStatement cs = conn.prepareCall("call invia_msg(?,?,?)");

            cs.setString(1, sessione.getUserCf());
            cs.setInt(2, codiceCanale);
            cs.setString(3, testo);

            cs.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore nell'invio del messaggio: " + e.getMessage());
        }
    }

    public void rispondiAMessaggio(int codiceMessaggioRiferito, String testo, String tipo) throws DAOException{
        try {
            Connection conn = ConnectionFactory.getConnection();
            SessionManager sessione = SessionManager.getInstance();
            CallableStatement cs = conn.prepareCall("call rispondi_a_msg(?,?,?,?)");

            cs.setInt(1, codiceMessaggioRiferito);
            cs.setString(2, sessione.getUserCf());
            cs.setString(3, testo);
            cs.setString(4, tipo);

            cs.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore nell'invio della risposta: " + e.getMessage());
        }
    }

    public static List<Messaggio> leggiMessaggi(int codiceCanale) throws DAOException {
        List<Messaggio> messaggi = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            SessionManager sessione = SessionManager.getInstance();
            CallableStatement cs = conn.prepareCall("call leggi_msg(?,?)");

            cs.setInt(1, codiceCanale);
            cs.setString(2, sessione.getUserCf());
            popolaListaMessaggi(messaggi, cs);
        } catch (SQLException e) {
            throw new DAOException("Errore nel caricamento dei messaggi: " + e.getMessage());
        }
        return messaggi;
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

    private static void popolaListaCanali(List<Canale> listaCanali, CallableStatement cs) throws SQLException {
        ResultSet resultSet = cs.executeQuery();

        while (resultSet.next()) {
            Canale canale = new Canale();
            canale.setCodiceCanale(resultSet.getInt("CodiceCanale"));
            canale.setNome(resultSet.getString("NomeCanale"));
            String tipoString = resultSet.getString("Tipo");
            Canale.Tipo tipo = Canale.Tipo.valueOf(tipoString); // Conversione da stringa a ENUM
            canale.setTipo(tipo);
            canale.setProgetto(resultSet.getInt("CodiceProgetto"));

            listaCanali.add(canale);
        }
    }

    private static void popolaListaMessaggi(List<Messaggio> listaMessaggi, CallableStatement cs) throws SQLException {
        boolean result = cs.execute();

        if (result) {
            try (ResultSet rs = cs.getResultSet()) {
                while (rs.next()) {
                    Messaggio messaggio = new Messaggio();
                    messaggio.setCodiceMSG(rs.getInt("CodiceMessaggio"));
                    messaggio.setDataInvio(rs.getDate("DataInvio"));
                    messaggio.setOraInvio(rs.getTime("OraInvio"));
                    messaggio.setMittente(rs.getString("Mittente"));
                    messaggio.setParole(rs.getString("Testo"));

                    if (rs.getString("RispostaA") != null) {
                        messaggio.setParole(messaggio.getParole() + "\n[Risposta a: " + rs.getString("RispostaA") + "]");
                    }

                    listaMessaggi.add(messaggio);
                }
            }
        }
    }
}
