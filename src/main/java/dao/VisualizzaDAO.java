package dao;

import beans.Visualizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaDAO {

    private Connection con;
    public VisualizzaDAO(Connection connection) {
        this.con = connection;
    }

    public List<Visualizza> getLAstFive (String email) throws SQLException{
        List<Visualizza> visualizzazioni = new ArrayList<Visualizza>();

        String query = "SELECT * FROM visualizza WHERE email = ? LIMIT 5";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, email);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Visualizza visualizza = mapRowToVisualizza(result);
                    visualizzazioni.add(visualizza);
                }
            }

        }
        return visualizzazioni;
    }


    private Visualizza mapRowToVisualizza(ResultSet result) throws SQLException {
        Visualizza visualizza = new Visualizza();
        visualizza.setEmail(result.getString("email"));
        visualizza.setCodiceProdotto(result.getInt("codice_prodotto"));
        return visualizza;
    }

}
