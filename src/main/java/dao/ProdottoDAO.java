package dao;

import beans.Prodotto;
import beans.Visualizza;
import utils.Constants;

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
        try (PreparedStatement pstatement = con.prepareStatement(query)) {
            pstatement.setInt(1, codiceProdotto);
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    return mapRowToProdotto(result);
                }
            }
        }
    }

    public List<Prodotto> completeListVisualized(List<Prodotto> prodotti) throws SQLException {
        List<Prodotto> moreProducts = new ArrayList<>();
        String query = "SELECT distinct(prodotto.codice_prodotto),prodotto.nome_prodotto,prodotto.categoria,prodotto.descrizione,prodotto.foto FROM prodotto join vende on prodotto.codice_prodotto = vende.codice_prodotto WHERE vende.sconto > 0 AND prodotto.categoria = ? ORDER BY RAND() LIMIT 5";
        PreparedStatement pstatement = con.prepareStatement(query);
        pstatement.setString(1, Constants.DEFAULT_CATEGORY);
        ResultSet result = pstatement.executeQuery();
        while (result.next()){
            Prodotto prodotto = mapRowToProdotto(result);
            moreProducts.add(prodotto);
        }

        for (Prodotto prodotto : prodotti) {
            for (int j = 0; j < moreProducts.size(); j++) {
                if (prodotto.getCodiceProdotto() == moreProducts.get(j).getCodiceProdotto()) {
                    moreProducts.remove(j);
                    j--;
                }
            }
        }
        prodotti.addAll(moreProducts);

        while(prodotti.size() > Constants.NUMBER_HOME_PRODUCT){
            prodotti.remove(Constants.NUMBER_HOME_PRODUCT);
        }


        return prodotti;
    }

    private Prodotto mapRowToProdotto(ResultSet result) throws SQLException {

        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceProdotto(result.getInt("codice_prodotto"));
        prodotto.setNomeProdotto(result.getString("nome_prodotto"));
        prodotto.setCategoria(result.getString("categoria"));
        prodotto.setFoto(result.getBlob("foto"));
        prodotto.setDescrizione(result.getString("descrizione"));
        return prodotto;
    }
    public List<Prodotto> getFiveVisualizedProduct(List<Visualizza> visualizzati) throws SQLException {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto WHERE codice_prodotto = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query)) {
            for (Visualizza visualizza : visualizzati) {
                pstatement.setInt(1, visualizza.getCodiceProdotto());
                try (ResultSet result = pstatement.executeQuery()) {
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
