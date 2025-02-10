package controller;

import dao.SessionManager;
import exception.DAOException;
import dao.LoginProcedureDAO;
import exception.LoginException;
import model.Credenziali;
import view.LoginView;

import java.io.IOException;

public class LoginController implements Controller{
    Credenziali cred = null;

    public void start() {

        try {
            cred = LoginView.authenticate();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        try {
            cred = new LoginProcedureDAO().execute(cred.getUsername(), cred.getPassword());
            SessionManager session = SessionManager.getInstance();
            session.authenticateUser(cred);

        } catch(LoginException | DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public Credenziali getCred() {
        return cred;
    }
}
