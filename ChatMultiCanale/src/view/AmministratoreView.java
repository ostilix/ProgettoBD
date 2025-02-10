package view;

import dao.CapoProgettoDAO;
import exception.DAOException;
import model.*;
import java.io.IOException;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AmministratoreView {
    public static void showTitle(){
        System.out.println("********************");
        System.out.println("*     Benvenuto    *");
        System.out.println("********************");
    }

    public static int showMenu() throws IOException{
        System.out.println("Digita il numero dell'operazione che vuoi effettuare: ");
        System.out.println("1) Inserisci un nuovo impiegato nel sistema");
        System.out.println("2) Assegna un capo progetto ad un progetto");
        System.out.println("3) Inserisci un nuovo progetto");
        System.out.println("4) Visualizza la lista di tutti gli impiegati");
        System.out.println("5) Esci");

        Scanner input = new Scanner(System.in);
        int choice;

        while(true){
            try{
                System.out.println("Digita il numero dell'operazione:");
                choice = input.nextInt();
                if(choice >= 1 && choice <= 5){
                    break;
                }else{
                    throw new InputMismatchException();
                }
            }catch (InputMismatchException e){
                System.out.println("Numero non valido. Inserire un numero compreso tra 1 e 5.\n Riprova.");
            }
        }
        return choice;
    }


    public static Impiegato nuovoImpiegato(){
        System.out.println("\n******* INSERISCI UN NUOVO IMPIEGATO *******");
        Scanner scanner = new Scanner(System.in);
        String codiceFiscale;
        String nome;
        String cognome;
        Ruolo ruolo = null;
        String username;
        String password;

        System.out.println("Inserisci il codice fiscale dell'impiegato: ");
        codiceFiscale = scanner.nextLine();
        System.out.println("Inserisci il nome dell'impiegato: ");
        nome = scanner.nextLine();
        System.out.println("Inserisci il cognome dell'impiegato: ");
        cognome = scanner.nextLine();
        while (ruolo == null) {
            System.out.println("\nSeleziona il ruolo dell'impiegato:");
            for (Ruolo role : Ruolo.values()) {
                System.out.println(role.getId() + " - " + role.name());
            }

            System.out.println("Inserisci il numero del ruolo (1-3): ");
            while (!scanner.hasNextInt()) { //se inserisco delle lettere
                System.out.println("Errore: Inserisci un numero valido.");
                scanner.next();
                System.out.println("Inserisci il numero del ruolo:\n1) Amministratore\n2) Capo proogetto\n3) Dipendente base\n");
            }
            int ruoloId = scanner.nextInt();
            scanner.nextLine();

            if (ruoloId < 1 || ruoloId > 3) {
                System.out.println("Errore: Il numero deve essere compreso tra 1 e 3.\nRiprova.");
                continue;
            }

            ruolo = Ruolo.fromInt(ruoloId);
        }
        System.out.println("Inserisci l'username dell'impiegato: ");
        username = scanner.nextLine();
        System.out.println("Inserisci la password per l'accesso al sistema: ");
        password = scanner.nextLine();

        return new Impiegato(codiceFiscale, nome, cognome, ruolo.getId(), username, password);
    }

    public static Object[] assegnaCapo() throws DAOException {
        String codiceFiscale;
        int codiceProgetto;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n******* ASSEGNA UN CAPO PROGETTO AD UN PROGETTO *******");

        System.out.println("\nLista dei progetti disponibili:");
        List<Progetto> progetti = CapoProgettoDAO.listaProgetti(); //torna la lista dei progetti dal db
        for(Progetto progetto : progetti){ //itero sulla lista
            System.out.println("Codice Progetto: " + progetto.getCodiceProgetto());
            System.out.println("Nome: " + progetto.getNome());
            System.out.println("Data scadenza: " + progetto.getDataScadenza());
            System.out.println("Capo Progetto: " + progetto.getCapoProgetto());
            System.out.println("*******************************\n\n");
        }

        System.out.print("\nInserisci il codice del progetto a cui vuoi assegnare il capo progetto: ");
        codiceProgetto = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nLista degli impiegati disponibili:");
        List<Impiegato> impiegati = CapoProgettoDAO.listaImpiegati(); //torna la lista degli impiegati dal db

        for(Impiegato impiegato : impiegati){
            System.out.println("Codice Fiscale: " + impiegato.getCodiceFiscale());
            System.out.println("Nome: " + impiegato.getNome());
            System.out.println("Cognome: " + impiegato.getCognome());
            System.out.println("Ruolo: " + impiegato.getRuolo());
            System.out.println("*******************************\n\n");
        }
        System.out.print("\nInserisci il codice fiscale del capo progetto che vuoi assegnare: ");
        codiceFiscale = scanner.nextLine();

        Impiegato capoProgettoScelto = null;
        for(Impiegato impiegato : impiegati){
            if(impiegato.getCodiceFiscale().equalsIgnoreCase(codiceFiscale)){ //controllo e seleziono l'impiegato scelto
                capoProgettoScelto = impiegato;
                break;
            }
        }

        if (capoProgettoScelto == null) {
            System.out.println("Errore: Codice fiscale non valido. Operazione annullata.");
            return new Object[0];
        }

        return new Object[]{codiceProgetto, capoProgettoScelto}; //array di oggetti tipo diverso, int e Impiegato

    }

    public static Progetto creaProgetto(){
        System.out.println("\n******* INSERISCI UN NUOVO PROGETTO *******");
        Scanner scanner = new Scanner(System.in);
        String nome;
        Date dataScadenza = null;

        System.out.println("Inserisci il nome del nuovo progetto: ");
        nome = scanner.nextLine();

        while (dataScadenza == null) {
            try {
                System.out.println("Inserisci la data di scadenza nel formato AAAA-MM-GG: ");
                String dataInput = scanner.nextLine();
                dataScadenza = Date.valueOf(dataInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Formato data non valido. Usare il formato AAAA-MM-GG.");
            }
        }
        return new Progetto(nome, dataScadenza);
    }

    public static void listaImpiegati() throws DAOException {
        System.out.println("\n******* LISTA IMPIEGATI *******");
        List<Impiegato> impiegati = CapoProgettoDAO.listaImpiegati();

        for(Impiegato impiegato : impiegati){
            System.out.println("Codice Fiscale: " + impiegato.getCodiceFiscale());
            System.out.println("Nome: " + impiegato.getNome());
            System.out.println("Cognome: " + impiegato.getCognome());
            System.out.println("Ruolo: " + impiegato.getRuolo());
            System.out.println("*******************************\n\n");
        }
    }
}
