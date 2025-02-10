package controller;

import dao.ConnectionFactory;
import dao.ImpiegatoDAO;
import exception.DAOException;
import model.Ruolo;
import view.DipendenteBaseView;
import java.io.IOException;
import java.sql.SQLException;

public class DipendenteBaseController implements Controller{
    @Override
    public void start() {
        try{
            ConnectionFactory.changeRole(Ruolo.DIPENDENTEBASE);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        DipendenteBaseView.showTitle();
        while (true) {
            int choice;
            try {
                choice = DipendenteBaseView.showMenu();

                try {
                    switch (choice) {
                        case 1 -> visualizzaCanali();
                        case 2 -> visualizzaMembriCanale();
                        case 3 -> inviaMSG();
                        case 4 -> visualizzaConversazioni();
                        case 5 -> System.exit(0);
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

    public void visualizzaCanali(){
        try{
            DipendenteBaseView.listaCanali();
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }


    public void visualizzaMembriCanale(){
        try{
            Object[] result = DipendenteBaseView.listaMembriCanale();
            int codiceCanale = (int) result[0];
            ImpiegatoDAO.listaMembriCanale(codiceCanale);
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public void inviaMSG() throws DAOException {
        try {
            Object[] result = DipendenteBaseView.inviaMessaggio();
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
            Object[] result = DipendenteBaseView.leggiMessaggiCanale();
            int codiceCanale = (int) result[0];
            ImpiegatoDAO.leggiMessaggi(codiceCanale);
        }catch (DAOException e){
            System.out.println(e.getMessage());
        }
    }
}
