package dao;

import beans.Utente;
import beans.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckCredentials {
    private Connection con;
    public CheckCredentials(Connection connection) {
        this.con = connection;
    }
    public Utente checkCredentials(String email, String password) throws SQLException {
        String query = "SELECT * FROM utente WHERE email = ? AND password =?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, email);
            pstatement.setString(2, password);
            try (ResultSet result = pstatement.executeQuery();) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    Utente user = new Utente();
                    user.setEmail(result.getString("email"));
                    user.setNome(result.getString("nome"));
                    user.setCognome(result.getString("cognome"));
                    user.setIndirizzo(result.getString("indirizzo"));
                    user.setPassword(result.getString("password"));
                    return user;
                }
            }
        }
    }
}
