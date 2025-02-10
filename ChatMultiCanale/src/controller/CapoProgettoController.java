package controller;

import dao.CapoProgettoDAO;
import dao.ConnectionFactory;
import dao.ImpiegatoDAO;
import exception.DAOException;
import model.Canale;
import model.Impiegato;
import model.Ruolo;
import view.CapoProgettoView;
import java.io.IOException;
import java.sql.SQLException;

public class CapoProgettoController implements Controller{
    @Override
    public void start() {
        try {
            ConnectionFactory.changeRole(Ruolo.CAPOPROGETTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        CapoProgettoView.showTitle();
        while (true) {
            int choice;
            try {
                choice = CapoProgettoView.showMenu();

                try {
                    switch (choice) {
                        case 1 -> creaCanale();
                        case 2 -> visualizzaCanali();
                        case 3 -> inserisciInCanale();
                        case 4 -> visualizzaMembriCanale();
                        case 5 -> inserisciInProgetto();
                        case 6 -> visualizzaMembriProgetto();
                        case 7 -> inviaMSG();
                        case 8 -> visualizzaConversazioni();
                        case 9 -> visualizzaConversazioniPrivate();
                        case 10 -> visualizzaProgetti();
                        case 11 -> System.exit(0);
                        default -> throw new RuntimeException("Opzione non valida");
                    }
                } catch (DAOException e) {
                    System.out.println("Errore durante l'operazione: " + e.getMessage());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void creaCanale() throws DAOException {
        Canale canale;
        canale = CapoProgettoView.nuovoCanale();

        try{
            CapoProgettoDAO.creaCanale(canale);
            System.out.println("Canale creato con successo!\n");
        }catch(DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public void visualizzaCanali(){
        try{
            CapoProgettoView.listaCanali();
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public void inserisciInCanale(){
        try {
            Object[] result = CapoProgettoView.assegnaCanale();
            int codiceCanale = (int) result[0];
            Impiegato impiegato = (Impiegato) result[1];

            CapoProgettoDAO.assegnaCanale(impiegato.getCodiceFiscale(), codiceCanale);
            System.out.println("L'impiegato " + impiegato.getNome() + " " + impiegato.getCognome() + " è stato inserito con successo nel canale con codice " + codiceCanale);
        }catch(DAOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void visualizzaMembriCanale(){
        try{
            int codiceCanale = CapoProgettoView.listaMembriCanale();
            ImpiegatoDAO.listaMembriCanale(codiceCanale);
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public void inserisciInProgetto(){
        try {
            Object[] result = CapoProgettoView.assegnaProgetto();
            int codiceProgetto = (int) result[0];
            Impiegato impiegato = (Impiegato) result[1];

            CapoProgettoDAO.assegnaProgetto(impiegato.getCodiceFiscale(), codiceProgetto);
            System.out.println("L'impiegato " + impiegato.getNome() + " " + impiegato.getCognome() + " è stato inserito con successo nel progetto con codice " + codiceProgetto);
        }catch(DAOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void visualizzaMembriProgetto(){
        try{
            int codiceProgetto = CapoProgettoView.listaMembriProgetto();
            CapoProgettoDAO.listaMembriProgetto(codiceProgetto);
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public void inviaMSG() throws DAOException {
        try {
            Object[] result = CapoProgettoView.inviaMessaggio();
            int codiceCanale = (int) result[0];
            String testo = (String) result[1];

            ImpiegatoDAO.inviaMessaggio(codiceCanale, testo);
            System.out.println("Messaggio inviato con successo");
        }catch(DAOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void visualizzaConversazioni(){
        try{
            int codiceCanale = CapoProgettoView.leggiMessaggiCanale();
            ImpiegatoDAO.leggiMessaggi(codiceCanale);
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void visualizzaConversazioniPrivate(){
        try{
            CapoProgettoView.leggiMessaggiPrivati();
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void visualizzaProgetti(){
        try{
            CapoProgettoView.listaProgetti();
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }
}
