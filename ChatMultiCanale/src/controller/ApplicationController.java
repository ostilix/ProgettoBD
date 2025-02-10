package controller;

import model.Credenziali;

public class ApplicationController {

    public void start() {
        LoginController loginController = new LoginController();
        Credenziali cred;

        while(true){
            loginController.start();
            cred = loginController.getCred();

            if(cred.getRuolo() != null) {
                break;
            } else if (cred.getRuolo() == null) {
                System.out.println("Le credenziali non sono valide.");
            }
        }

        switch(cred.getRuolo()) {
            case AMMINISTRATORE -> new AmministratoreController().start();
            case CAPOPROGETTO -> new CapoProgettoController().start();
            case DIPENDENTEBASE -> new DipendenteBaseController().start();
            default -> throw new RuntimeException("Le credenziali non sono valide.");
        }
    }
}