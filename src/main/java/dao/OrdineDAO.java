package dao;

import beans.Ordine;
import beans.Prodotto;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class OrdineDAO {

    private Connection con;
    public OrdineDAO(Connection connection) {
        this.con = connection;
    }

    public Map<Ordine,List<Prodotto>> getOrdersByEmail(String email) throws SQLException{
        Map<Ordine,List<Prodotto>> prodottiPerOdine = new LinkedHashMap<>();
        List<Ordine> ordini = new ArrayList<>();
        String query = "SELECT * FROM ordini WHERE email = ? ORDER BY data_spedizione DESC";
        try (PreparedStatement pstatement = con.prepareStatement(query)) {
            pstatement.setString(1, email);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Ordine ordine = mapRowToOrdine(result);
                    ordini.add(ordine);
                }
            }

        }
        String query2 = "SELECT codice_prodotto, nome, categoria, descrizione FROM informazioni WHERE codice_ordine = ?";

        for (Ordine ordine : ordini) {
            List<Prodotto> prodotti = new ArrayList<>();
            try (PreparedStatement pstatement = con.prepareStatement(query2)) {
                pstatement.setInt(1, ordine.getCodiceOrdine());
                try (ResultSet result = pstatement.executeQuery();) {
                    while (result.next()) {
                        Prodotto prodotto = mapRowToProdotto(result);
                        prodotti.add(prodotto);
                    }
                    prodottiPerOdine.put(ordine, prodotti);
                }
            }
        }
        return prodottiPerOdine;
    }

    public void createOrder(Ordine ordine) throws SQLException {
        String query = "INSERT INTO ordine (nome_fornitore, data_spedizione, prezzo_totale, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query)) {
            pstatement.setString(1, ordine.getNomeFornitore());
            pstatement.setDate(2, ordine.getDataSpedizione());
            pstatement.setInt(3, ordine.getPrezzoTotale());
            pstatement.setString(4, ordine.getEmail());
            pstatement.executeUpdate();
        }
    }


    private Ordine mapRowToOrdine(ResultSet result) throws SQLException {
        Ordine ordine = new Ordine();
        ordine.setCodiceOrdine(result.getInt("codice_ordine"));
        ordine.setEmail(result.getString("email"));
        ordine.setDataSpedizione(result.getDate("data_spedizione"));
        ordine.setNomeFornitore(result.getString("nome_fornitore"));
        ordine.setPrezzoTotale(result.getInt("prezzo_totale"));
        return ordine;
    }
    private Prodotto mapRowToProdotto(ResultSet result) throws SQLException {
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceProdotto(result.getInt("codice_prodotto"));
        prodotto.setNomeProdotto(result.getString("nome"));
        prodotto.setCategoria(result.getString("categoria"));
        prodotto.setDescrizione(result.getString("descrizione"));
        return prodotto;
    }

}
