package view;

import dao.ImpiegatoDAO;
import exception.DAOException;
import model.*;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DipendenteBaseView {
    public static void showTitle() {
        System.out.println("*********************");
        System.out.println("*     Benvenuto     *");
        System.out.println("*********************\n");
    }

    public static int showMenu() throws IOException {
        System.out.println("1) Visualizza i canali di cui fai parte");
        System.out.println("2) Visualizza membri di un canale");
        System.out.println("3) Invia un messaggio in un canale");
        System.out.println("4) Visualizza le conversazioni dei canali a cui appartieni");
        System.out.println("5) Esci\n");

        Scanner input = new Scanner(System.in);
        int choice;

        while (true) {
            try {
                System.out.println("Digita il numero dell'operazione che vuoi effettuare: ");
                choice = input.nextInt();
                if (choice >= 1 && choice <= 5) {
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("Numero non valido. Inserire un numero compreso tra 1 e 5.\n Riprova.");
            }
        }
        return choice;
    }

    public static void listaCanali() throws DAOException {
        System.out.println("\n******* LISTA CANALI *******");
        List<Canale> canali = ImpiegatoDAO.listaCanali();

        if (canali.isEmpty()) {
            System.out.println("Non sei iscritto a nessun canale.\n");
            return;
        }
        stampaListaCanali(canali);
    }

    public static Object[] listaMembriCanale() throws DAOException {
        int codiceCanale;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nLista dei canali di cui fai parte:");
        List<Canale> canali = ImpiegatoDAO.listaCanali();

        if (canali.isEmpty()) {
            System.out.println("Non sei iscritto a nessun canale.\n");
            return new Object[0];
        }
        stampaListaCanali(canali);

        System.out.print("\nInserisci il codice del canale da selezionare: ");
        codiceCanale = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nLista dei membri del canale selezionato:");
        List<Impiegato> impiegati = ImpiegatoDAO.listaMembriCanale(codiceCanale);
        stampaListaImpiegati(impiegati);

        return new Object[]{codiceCanale};
    }

    public static Object[] inviaMessaggio() throws DAOException {
        System.out.println("\n******* INVIA UN MESSAGGIO *******");
        int codiceCanale;
        String testo;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nLista dei canali di cui fai parte:");
        List<Canale> canali = ImpiegatoDAO.listaCanali();

        if(canali.isEmpty()) {
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

    public static Object[] leggiMessaggiCanale() throws DAOException {
        int codiceCanale = -1;
        try {
            System.out.println("\n******* LEGGI MESSAGGI DI UN CANALE *******");

            int paginaCorrente;
            int pagineTotali;
            int scelta;
            int messaggiPerPagina = 5;
            Scanner scanner = new Scanner(System.in);

            System.out.println("\nLista dei canali di cui fai parte:");
            List<Canale> canali = ImpiegatoDAO.listaCanali();

            if(canali.isEmpty()){
                System.out.println("Non fai parte di nessun canale.\n");
                return new Object[0];
            }
            stampaListaCanali(canali);

            System.out.print("\nScrivi il codice del canale per vederne i messaggi:");
            codiceCanale = scanner.nextInt();
            scanner.nextLine();

            List<Messaggio> messaggi = ImpiegatoDAO.leggiMessaggi(codiceCanale);

            if (messaggi.isEmpty()) {
                System.out.println("Non ci sono messaggi in questo canale.");
                return new Object[]{codiceCanale};
            }

            pagineTotali = (int) Math.ceil((double) messaggi.size() / messaggiPerPagina);
            paginaCorrente = pagineTotali;

            boolean running = true;

            while (running) {
                mostraMessaggiPerPagina(messaggi, paginaCorrente, messaggiPerPagina);
                System.out.println("1) Pagina precedente | 2) Pagina successiva | 3) Invia un messaggio nel canale | 4) Rispondi ad un messaggio | 5) Aggiorna messaggi | 6) Torna al menù principale\nScegli una tra le opzioni: ");
                while (!scanner.hasNextInt()) {
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
                        messaggi = ImpiegatoDAO.leggiMessaggi(codiceCanale);
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
        return new Object[]{codiceCanale};
    }

    private static void mostraMessaggiPerPagina(List<Messaggio> messaggi, int pagina, int messaggiPerPagina) {
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
            System.out.println("Messaggio inviato con successo!");
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
        while (!scanner.hasNextInt()) {
            System.out.println("Errore: Devi inserire un numero valido!");
            scanner.next();
            System.out.print("Scrivi il codice del messaggio a cui vuoi rispondere: ");
        }
        codiceMessaggio = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Rispondere in modo (P)ubblico o (R)iservato? ");
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
            System.out.println("Messaggio inviato con successo!");
        } catch (DAOException e) {
            System.out.println("Errore nell'invio del messaggio: " + e.getMessage());
        }
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