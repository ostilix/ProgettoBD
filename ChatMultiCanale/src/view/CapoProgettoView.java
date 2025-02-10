package view;

import dao.CapoProgettoDAO;
import dao.ImpiegatoDAO;
import exception.DAOException;
import model.*;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CapoProgettoView {
    public static void showTitle(){
        System.out.println("********************");
        System.out.println("*     BENVENUTO    *");
        System.out.println("********************\n");
    }

    public static int showMenu() throws IOException{
        System.out.println("1) Crea un nuovo canale pubblico");
        System.out.println("2) Visualizza i canali dei progetti di cui si è responsabili");
        System.out.println("3) Inserisci un impiegato nel canale");
        System.out.println("4) Visualizza membri di un canale");
        System.out.println("5) Assegna un impiegato ad un progetto");
        System.out.println("6) Visualizza membri di un progetto");
        System.out.println("7) Invia un messaggio in un canale");
        System.out.println("8) Visualizza le conversazioni dei canali a cui appartieni");
        System.out.println("9) Visualizza le conversazioni dei canali privati");
        System.out.println("10) Visualizza la lista dei progetti di cui si è responsabili");
        System.out.println("11) Esci\n");

        Scanner input = new Scanner(System.in);
        int choice;

        while(true){
            try{
                System.out.println("Digita il numero dell'operazione che vuoi effettuare:");
                choice = input.nextInt();

                if(choice >= 1 && choice <= 11){
                    break;
                }else{
                    throw new InputMismatchException();
                }
            }catch (InputMismatchException e){
                System.out.println("Numero non valido. Inserire un numero compreso tra 1 e 11. Riprova.\n");
            }
        }
        return choice;
    }

    public static Canale nuovoCanale() throws DAOException {
        System.out.println("\n******* INSERISCI UN NUOVO CANALE *******");
        Scanner scanner = new Scanner(System.in);
        String nome;
        int codiceProgetto;

        System.out.println("\n******* Lista dei progetti disponibili *******");
        List<Progetto> progetti = CapoProgettoDAO.listaProgetti();
        stampaListaProgetti(progetti);

        System.out.println("\nInserisci il codice del progetto di riferimento del canale: ");
        codiceProgetto = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nInserisci il nome del canale: ");
        nome = scanner.nextLine();

        return new Canale(nome, codiceProgetto);
    }

    public static void listaCanali() throws DAOException {
        System.out.println("\n******* LISTA CANALI *******");
        List<Canale> canali = ImpiegatoDAO.listaCanali();

        if (canali.isEmpty()) {
            System.out.println("\nNon sei iscritto a nessun canale.\n");
            return;
        }
        stampaListaCanali(canali);
    }

    public static Object[] assegnaCanale() throws DAOException {
        System.out.println("\n******* AGGIUNGI IMPIEGATO AD UN CANALE *******");
        int codiceCanale;
        String codiceFiscale;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n******* Lista dei canali disponibili *******\n");
        List<Canale> canali = ImpiegatoDAO.listaCanali();
        stampaListaCanali(canali);
        System.out.print("\nInserisci il codice del canale da selezionare: ");
        codiceCanale = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n******* Lista degli impiegati non ancora aggiunti al canale selezionato *******");
        List<Impiegato> impiegati = CapoProgettoDAO.listaAssentiCanale(codiceCanale);
        stampaListaImpiegati(impiegati);
        System.out.print("Inserisci il codice fiscale dell'impiegato da aggiungere al canale: ");
        codiceFiscale = scanner.nextLine();

        Impiegato impiegatoScelto = null;
        for(Impiegato impiegato : impiegati){
            if(impiegato.getCodiceFiscale().equalsIgnoreCase(codiceFiscale)){
                impiegatoScelto = impiegato;
                break;
            }
        }

        if (impiegatoScelto == null) {
            System.out.println("Errore: Codice fiscale non valido. Operazione annullata.");
            return new Object[0];
        }

        return new Object[]{codiceCanale, impiegatoScelto};
    }

    public static int listaMembriCanale() throws DAOException {
        int codiceCanale = -1;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n******* Lista dei canali di cui sei responsabile *******");
        List<Canale> canali = ImpiegatoDAO.listaCanali();

        if (canali.isEmpty()) {
            System.out.println("Non sei responsabile di nessun canale.\n");
            return codiceCanale;
        }
        stampaListaCanali(canali);
        System.out.print("Inserisci il codice del canale da selezionare: ");
        codiceCanale = scanner.nextInt();
        scanner.nextLine();

        System.out.println("******* Lista dei membri del canale selezionato *******");
        List<Impiegato> impiegati = ImpiegatoDAO.listaMembriCanale(codiceCanale);
        stampaListaImpiegati(impiegati);

        return codiceCanale;
    }

    public static Object[] assegnaProgetto() throws DAOException {
        String codiceFiscale;
        int codiceProgetto;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n******* ASSEGNA UN IMPIEGATO AD UN PROGETTO *******");

        System.out.println("\n******* Lista dei progetti di cui sei responsabile *******");
        List<Progetto> progetti = CapoProgettoDAO.listaProgetti();
        stampaListaProgetti(progetti);

        System.out.print("\nInserisci il codice del progetto da selezionare: ");
        codiceProgetto = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n******* Lista degli impiegati non ancora appartenenti al progetto selezionato *******");
        List<Impiegato> impiegati = CapoProgettoDAO.listaAssentiProgetto(codiceProgetto);
        stampaListaImpiegati(impiegati);

        System.out.print("\nInserisci il codice fiscale dell'impigeato da aggiungere al progetto: ");
        codiceFiscale = scanner.nextLine();

        Impiegato impiegatoScelto = null;
        for(Impiegato impiegato : impiegati){
            if(impiegato.getCodiceFiscale().equalsIgnoreCase(codiceFiscale)){
                impiegatoScelto = impiegato;
                break;
            }
        }

        if (impiegatoScelto == null) {
            System.out.println("Errore: Codice fiscale non valido. Operazione annullata.");
            return new Object[0];
        }
        return new Object[]{codiceProgetto, impiegatoScelto};
    }

    public static int listaMembriProgetto() throws DAOException {
        int codiceProgetto = -1;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n******* Lista dei progetti di cui sei responsabile *******");
        List<Progetto> progetti = CapoProgettoDAO.listaProgetti();

        if (progetti.isEmpty()) {
            System.out.println("Non sei responsabile di nessun progetto.\n");
            return codiceProgetto;
        }
        stampaListaProgetti(progetti);

        System.out.print("\nInserisci il codice del progetto da selezionare: ");
        codiceProgetto = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n******* Lista dei membri del progetto selezionato *******");
        List<Impiegato> impiegati = CapoProgettoDAO.listaMembriProgetto(codiceProgetto);
        stampaListaImpiegati(impiegati);

        return codiceProgetto;
    }

    public static Object[] inviaMessaggio() throws DAOException {
        System.out.println("\n******* INVIA UN MESSAGGIO *******");
        int codiceCanale;
        String testo;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n******* Lista dei canali di cui fai parte *******");
        List<Canale> canali = ImpiegatoDAO.listaCanali();
        if(canali.isEmpty()){
            System.out.println("Non fai parte di nessun canale.\n");
            return new Object[0];
        }
        stampaListaCanali(canali);

        System.out.print("\nSeleziona il canale dove vuoi inviare il messaggio: ");
        codiceCanale = scanner.nextInt();
        scanner.nextLine();

        System.out.print("\nScrivi il testo da inviare:");
        testo = scanner.nextLine();
        return new Object[]{codiceCanale, testo};
    }

    public static int leggiMessaggiCanale() throws DAOException {
        int codiceCanale = -1;
        try {
            System.out.println("\n******* LEGGI MESSAGGI DI UN CANALE *******");

            int paginaCorrente;
            int pagineTotali;
            int scelta;
            int messaggiPerPagina = 5;
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n******* Lista dei canali di cui fai parte *******");
            List<Canale> canali = ImpiegatoDAO.listaCanali();
            if(canali.isEmpty()){
                System.out.println("Non fai parte di nessun canale.\n");
                return codiceCanale;
            }
            stampaListaCanali(canali);

            System.out.print("\nScrivi il codice del canale per vederne i messaggi:");
            codiceCanale = scanner.nextInt();
            scanner.nextLine();

            List<Messaggio> messaggi = ImpiegatoDAO.leggiMessaggi(codiceCanale);

            if (messaggi.isEmpty()) {
                System.out.println("Non ci sono messaggi in questo canale.");
                return codiceCanale;
            }

            pagineTotali = (int) Math.ceil((double) messaggi.size() / messaggiPerPagina); //arrotondo per eccesso
            paginaCorrente = pagineTotali; //mi posiziono all'ultima pagina

            boolean running = true;
            while (running) {
                stampaMessaggiPerPagina(messaggi, paginaCorrente, messaggiPerPagina);
                System.out.println("1) Pagina precedente | 2) Pagina successiva | 3) Invia un messaggio nel canale | 4) Rispondi ad un messaggio | 5) Aggiorna messaggi | 6) Torna al menù principale\nScegli una tra le opzioni: ");
                while (!scanner.hasNextInt()) { //se inserisco lettere
                    System.out.println("Errore: devi inserire un numero valido! Riprova: ");
                    scanner.nextLine();
                }
                scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1:
                        if (paginaCorrente > 1) {
                            paginaCorrente--;
                        } else {
                            System.out.println("Sei già alla prima pagina.");
                        }
                        break;
                    case 2:
                        if (paginaCorrente < pagineTotali) {
                            paginaCorrente++;
                        } else {
                            System.out.println("Sei già all'ultima pagina.");
                        }
                        break;
                    case 3:
                        inviaMessaggio2(codiceCanale);
                        messaggi = ImpiegatoDAO.leggiMessaggi(codiceCanale); //aggiorno i messaggi
                        pagineTotali = (int) Math.ceil((double) messaggi.size() / messaggiPerPagina);
                        paginaCorrente = pagineTotali;
                        break;
                    case 4:
                        rispondiAMessaggio();
                        messaggi = ImpiegatoDAO.leggiMessaggi(codiceCanale);
                        pagineTotali = (int) Math.ceil((double) messaggi.size() / messaggiPerPagina);
                        paginaCorrente = pagineTotali;
                        break;
                    case 5:
                        messaggi = ImpiegatoDAO.leggiMessaggi(codiceCanale);
                        pagineTotali = (int) Math.ceil((double) messaggi.size() / messaggiPerPagina);
                        paginaCorrente = pagineTotali;
                        break;
                    case 6:
                        running = false;
                        System.out.println("Tornando al menu principale...");
                        break;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            }
        } catch (DAOException e) {
            System.out.println("Errore durante il recupero dei dati: " + e.getMessage());
        }
        return codiceCanale;
    }

    private static void stampaMessaggiPerPagina(List<Messaggio> messaggi, int pagina, int messaggiPerPagina) {
        int start = (pagina - 1) * messaggiPerPagina;
        int end = Math.min(start + messaggiPerPagina, messaggi.size());
        System.out.println("\n******* Messaggi Pagina " + pagina + " di " + (int) Math.ceil((double) messaggi.size() / messaggiPerPagina) + " *******\n");

        for (int i = start; i < end; i++) {
            Messaggio msg = messaggi.get(i);
            System.out.printf("[%d] | %s\n", msg.getCodiceMSG(), msg.getMittente());
            stampaTestoACapo(msg.getParole());
            System.out.printf("%s | %s\n", msg.getDataInvio(), msg.getOraInvio());
            System.out.println("---------------------------------------------------------------\n");
        }
    }

    private static void stampaTestoACapo(String testo) {
        while (testo.length() > 80) {
            System.out.println(testo.substring(0, 80));
            testo = testo.substring(80);
        }
        System.out.println(testo);
    }

    public static void inviaMessaggio2(int codiceCanale) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Scrivi il testo del messaggio: ");
        String testo = scanner.nextLine();
        try {
            ImpiegatoDAO.inviaMessaggio(codiceCanale, testo);
            System.out.println("\nMessaggio inviato con successo!\n");
        } catch (DAOException e) {
            System.out.println("Errore nell'invio del messaggio: " + e.getMessage());
        }
    }

    public static void rispondiAMessaggio() {
        Scanner scanner = new Scanner(System.in);
        int codiceMessaggio;
        String testo;
        String tipoMessaggio;
        String tipoRisposta;
        ImpiegatoDAO impiegatoDAO = new ImpiegatoDAO();

        System.out.print("Scrivi il codice del messaggio a cui vuoi rispondere: ");
        while (!scanner.hasNextInt()) { //se inserisco lettere
            System.out.println("Errore: Devi inserire un numero valido!");
            scanner.next();
            System.out.print("Scrivi il codice del messaggio a cui vuoi rispondere: ");
        }
        codiceMessaggio = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Rispondere in modo (P)ubblico o (R)iservato? ");
        tipoRisposta = scanner.nextLine().trim().toUpperCase();
        if(tipoRisposta.equals("P")) {
            tipoMessaggio = "Pubblico";
        }else if (tipoRisposta.equals("R")) {
            tipoMessaggio = "Privato";
        } else {
            System.out.println("Si deve inserire 'P' per pubblico o 'R' per riservato.");
            return;
        }

        System.out.print("Scrivi il testo del messaggio: ");
        testo = scanner.nextLine();

        try {
            impiegatoDAO.rispondiAMessaggio(codiceMessaggio, testo, tipoMessaggio);
            System.out.println("\nMessaggio inviato con successo!\n");
        } catch (DAOException e) {
            System.out.println("Errore nell'invio del messaggio: " + e.getMessage());
        }
    }

    public static void leggiMessaggiPrivati() throws DAOException {
        try {
            System.out.println("\n******* LEGGI MESSAGGI PRIVATI DI UN CANALE *******");

            int codiceCanale;
            int paginaCorrente;
            int pagineTotali;
            int scelta;
            int messaggiPerPagina = 5;
            Scanner scanner = new Scanner(System.in);

            System.out.println("******* Lista dei canali *******");
            List<Canale> canali = ImpiegatoDAO.listaCanali();

            if(canali.isEmpty()){
                System.out.println("Non ci sono canali privati al momento");
                return;
            }
            stampaListaCanali(canali);
            System.out.print("\nScrivi il codice del canale per vederne i messaggi:");
            codiceCanale = scanner.nextInt();
            scanner.nextLine();

            List<Messaggio> messaggi = ImpiegatoDAO.leggiMessaggi(codiceCanale);

            pagineTotali = (int) Math.ceil((double) messaggi.size() / messaggiPerPagina);
            paginaCorrente = pagineTotali;
            boolean running = true;

            while (running) {
                stampaMessaggiPerPagina(messaggi, paginaCorrente, messaggiPerPagina);
                System.out.println("1) Pagina successiva | 2) Pagina precedente | 3) Torna al menù principale\nScegli una tra le opzioni: ");
                scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1:
                        if (paginaCorrente < pagineTotali) {
                            paginaCorrente++;
                        } else {
                            System.out.println("Sei già all'ultima pagina");
                        }
                        break;
                    case 2:
                        if (paginaCorrente > 1) {
                            paginaCorrente--;
                        } else {
                            System.out.println("Sei già alla prima pagina");
                        }
                        break;
                    case 3:
                        running = false;
                        System.out.println("Tornando al menu principale...");
                        break;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            }
        }catch(DAOException e){
            System.out.println("Errore durante il recupero dei dati: " + e.getMessage());
        }
    }

    public static void listaProgetti() throws DAOException {
        System.out.println("\n******* LISTA PROGETTI *******");
        List<Progetto> progetti = CapoProgettoDAO.listaProgetti();
        if (progetti.isEmpty()) {
            System.out.println("Non sei responsabile di nessun progetto.\n");
            return;
        }
        stampaListaProgetti(progetti);
    }

    private static void stampaListaCanali(List<Canale> canali) {
        for (Canale canale : canali) {
            System.out.println("*******************************");
            System.out.println("Codice Canale: " + canale.getCodiceCanale());
            System.out.println("Nome Canale: " + canale.getNome());
            System.out.println("Tipo: " + canale.getTipo());
            System.out.println("Progetto Associato: " + canale.getProgetto());
            System.out.println("*******************************\n");
        }
    }

    private static void stampaListaProgetti(List<Progetto> progetti) {
        for (Progetto progetto : progetti) {
            System.out.println("*******************************");
            System.out.println("Codice Progetto: " + progetto.getCodiceProgetto());
            System.out.println("Nome: " + progetto.getNome());
            System.out.println("Data Scadenza: " + progetto.getDataScadenza());
            System.out.println("Capo Progetto: " + progetto.getCapoProgetto());
            System.out.println("*******************************\n");
        }
    }

    private static void stampaListaImpiegati(List<Impiegato> impiegati) {
        for (Impiegato impiegato : impiegati) {
            System.out.println("*******************************");
            System.out.println("Codice Fiscale: " + impiegato.getCodiceFiscale());
            System.out.println("Nome: " + impiegato.getNome());
            System.out.println("Cognome: " + impiegato.getCognome());
            System.out.println("Ruolo: " + impiegato.getRuolo());
            System.out.println("*******************************\n");
        }
    }

}
