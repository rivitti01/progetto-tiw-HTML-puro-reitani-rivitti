package dao;

import beans.Prodotto;
import beans.Vende;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendeDAO {
    private Connection con;
    public VendeDAO(Connection connection) {
        this.con = connection;
    }

    public Vende getMinPrice(int codiceProdotto) throws SQLException{
        String query = "SELECT * FROM vende WHERE codice_prodotto = ? ORDER BY prezzo ASC LIMIT 1";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, codiceProdotto);
            try (ResultSet result = pstatement.executeQuery();) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    return mapRowToVende(result);
                }
            }
        }
    }

    public List<Vende> getFiveDiscountedProducts() throws SQLException{
        List<Vende> prodotti = new ArrayList<Vende>();
        String query = "SELECT codice_prodotto, codice_forniotre, prezzo, sconto FROM vende v JOIN prodotto p ON p.codice_prodotto = v.codice_prodotto WHERE categoria = 'Informatica' AND sconto > 0 ORDER BY RAND() LIMIT 5";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Vende prodotto = mapRowToVende(result);
                    prodotti.add(prodotto);
                }
            }

        }
        return prodotti;
    }

    private Vende mapRowToVende(ResultSet result) throws SQLException {
        Vende vende = new Vende();
        vende.setCodiceProdotto(result.getInt("codice_prodotto"));
        vende.setCodiceFornitore(result.getInt("codice_fronitore"));
        vende.setPrezzo(result.getInt("prezzo"));
        vende.setSconto(result.getFloat("sconto"));
        return vende;
    }
}
