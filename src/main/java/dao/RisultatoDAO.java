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
        String query = "SELECT p.codice_prodotto,p.nome_prodotto, MIN(prezzo) FROM prodotto as p, vende as v WHERE p.codice_prodotto = v.codice_prodotto AND p.nome_prodotto LIKE ? OR p.descrizione LIKE ? group by p.codice_prodotto order by MIN(prezzo)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, "%" + word + "%");
            pstatement.setString(2, "%" + word + "%");
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Risultato risultato = new Risultato();
                    risultato.setCodiceProdotto(result.getInt("codice_prodotto"));
                    risultato.setNomeProdotto(result.getString("nome_prodotto"));
                    risultato.setPrezzo(result.getInt("prezzo"));
                    risultati.add(risultato);
                }
            }

        }
        return risultati;
    }

}
