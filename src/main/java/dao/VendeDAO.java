package dao;

import beans.Prodotto;
import beans.Vende;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private Vende mapRowToVende(ResultSet result) throws SQLException {
        Vende vende = new Vende();
        vende.setCodiceProdotto(result.getInt("codice_prodotto"));
        vende.setCodiceFornitore(result.getInt("codice_negozio"));
        vende.setPrezzo(result.getInt("prezzo"));
        vende.setSconto(result.getFloat("sconto"));
        return vende;
    }
}
