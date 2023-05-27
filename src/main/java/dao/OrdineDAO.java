package dao;

import beans.Ordine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {

    private Connection con;
    public OrdineDAO(Connection connection) {
        this.con = connection;
    }

    public List<Ordine> getOrdersByEmail (String email) throws SQLException{
        List<Ordine> ordini = new ArrayList<Ordine>();
        String query = "SELECT * FROM ordine WHERE email = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, email);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Ordine ordine = mapRowToOrdine(result);
                    ordini.add(ordine);
                }
            }

        }
        return ordini;
    }



    private Ordine mapRowToOrdine(ResultSet result) throws SQLException {
        Ordine ordine = new Ordine();
        ordine.setCodiceOrdine(result.getInt("codice_ordine"));
        ordine.setEmail(result.getString("email"));
        ordine.setDataSpedizione(result.getDate("data_spedizione"));
        ordine.setNomeFornitore(result.getString("nome_fornitore"));
        ordine.setDataSpedizione(result.getDate("data_spedizione"));
        return ordine;
    }

}
