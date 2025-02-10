package controller;

import dao.AmministratoreDAO;
import dao.ConnectionFactory;
import exception.DAOException;
import model.Impiegato;
import model.Progetto;
import model.Ruolo;
import view.AmministratoreView;
import java.io.IOException;
import java.sql.SQLException;

public class AmministratoreController implements Controller{
    @Override
    public void start(){
        try{
            ConnectionFactory.changeRole(Ruolo.AMMINISTRATORE);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        AmministratoreView.showTitle();

        while(true){
            int choice;
            try{
                choice = AmministratoreView.showMenu();
            }catch(IOException e){
                throw new RuntimeException(e);
            }

            switch(choice){
                case 1 -> aggiungiImpiegato();
                case 2 -> assegnaCapoProgetto();
                case 3 -> nuovoProgetto();
                case 4 -> listaDipendenti();
                case 5 -> System.exit(0);
                default -> throw new RuntimeException("Opzione non valida");
            }
        }
    }

    public void aggiungiImpiegato(){
        Impiegato impiegato;
        impiegato = AmministratoreView.nuovoImpiegato();

        try{
            AmministratoreDAO.nuovoImpiegato(impiegato);
            System.out.println("Impiegato aggiunto con successo!");
        }catch(DAOException e){
            System.out.println(e.getMessage());

        }
    }

    public void assegnaCapoProgetto(){

        try {
            Object[] result = AmministratoreView.assegnaCapo();

            int codiceProgetto = (int) result[0];
            Impiegato capoProgetto = (Impiegato) result[1];

            AmministratoreDAO.assegnaCapo(capoProgetto.getCodiceFiscale(), codiceProgetto);
            System.out.println("Capo progetto assegnato con successo al progetto");

        }catch(DAOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void nuovoProgetto(){
        Progetto progetto;
        progetto = AmministratoreView.creaProgetto();

        try{
            AmministratoreDAO.creaProgetto(progetto);
            System.out.println("Progetto aggiunto con successo!");
        }catch(DAOException e){
            System.out.println(e.getMessage());
        }
    }

    public void listaDipendenti(){
        try{
            AmministratoreView.listaImpiegati();
        }catch(DAOException e){
            System.out.println(e.getMessage());
        }

    }
}
