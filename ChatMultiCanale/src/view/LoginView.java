package view;
import model.Credenziali;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginView {
    public static Credenziali authenticate() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("********** LOGIN **********: ");
        System.out.print("Username: ");
        String username = reader.readLine();
        System.out.print("\nPassword: ");
        String password = reader.readLine();

        return new Credenziali(username, password, null, null);
    }
}

