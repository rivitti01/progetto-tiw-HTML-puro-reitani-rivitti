package dao;

import beans.Prodotto;
import utils.Risultato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RisultatoDAO {

    private Connection con;
    public RisultatoDAO(Connection connection) {
        this.con = connection;
    }

    public List<Risultato> searchByWord (String word) throws SQLException {
        List<Risultato> risultati = new ArrayList<Risultato>();
        String query = "SELECT vende.codice_prodotto,nome_prodotto, prezzo, sconto\n" +
                "FROM vende JOIN (SELECT codice_prodotto, nome_prodotto FROM prodotto WHERE nome_prodotto LIKE ? OR descrizione LIKE ?) as p\n" +
                "on vende.codice_prodotto = p.codice_prodotto\n" +
                "WHERE prezzo = (SELECT MIN(prezzo) FROM vende WHERE codice_prodotto = p.codice_prodotto)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, "%" + word + "%");
            pstatement.setString(2, "%" + word + "%");
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Risultato risultato = new Risultato();
                    risultato.setCodiceProdotto(result.getInt("codice_prodotto"));
                    risultato.setNomeProdotto(result.getString("nome_prodotto"));
                    risultato.setPrezzo(result.getInt("prezzo")*(1-result.getFloat("sconto")));
                    risultati.add(risultato);
                }
            }

        }
        return risultati;
    }

}
