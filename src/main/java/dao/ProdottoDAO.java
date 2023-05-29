package dao;

import beans.Prodotto;
import beans.Utente;
import beans.Visualizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAO {

    private Connection con;
    public ProdottoDAO(Connection connection) {
        this.con = connection;
    }

    public Prodotto getInformation(int codiceProdotto) throws SQLException {
        String query = "SELECT * FROM prodotto WHERE codice_prodotto = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, codiceProdotto);
            try (ResultSet result = pstatement.executeQuery();) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    return mapRowToProdotto(result);
                }
            }
        }
    }

    public List<Prodotto> searchByWord (String word) throws SQLException {
        List<Prodotto> prodotti = new ArrayList<Prodotto>();
        String query = "SELECT codice_prodtto,nome_prodotto FROM prodotto WHERE nome_prodotto LIKE ? OR descrizione LIKE ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, "%" + word + "%");
            pstatement.setString(2, "%" + word + "%");
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Prodotto prodotto = mapRowToProdotto(result);
                    prodotti.add(prodotto);
                }
            }

        }
        return prodotti;
    }
/*
        SELECT p.codice_prodotto, p.nome_prodotto, MIN(prezzo)
        FROM prodotto as p, vende as v
        where ( nome_prodotto LIKE '%Carbon%' OR descrizione LIKE '%Carbon%')
        AND p.codice_prodotto = v.codice_prodotto group by p.codice_prodotto, p.nome_prodotto
        ORDER BY MIN(prezzo) ASC;
*/

    private Prodotto mapRowToProdotto(ResultSet result) throws SQLException {
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceProdotto(result.getInt("codice_prodotto"));
        prodotto.setNomeProdotto(result.getString("nome_prodotto"));
        prodotto.setCategoria(result.getString("categoria"));
        prodotto.setFoto(result.getString("foto"));
        prodotto.setDescrizione(result.getString("descrizione"));
        return prodotto;
    }
    public List<Prodotto> getFiveVisualizedProduct(List<Visualizza> visualizzati) throws SQLException {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto WHERE codice_prodotto = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            for (Visualizza visualizza : visualizzati) {
                pstatement.setInt(1, visualizza.getCodiceProdotto());
                try (ResultSet result = pstatement.executeQuery();) {
                    if (!result.isBeforeFirst()) // no results, credential check failed
                        return null;
                    else {
                        result.next();
                        Prodotto prodotto = mapRowToProdotto(result);
                        prodotti.add(prodotto);
                    }
                }
            }
        }
        return prodotti;
    }

}
