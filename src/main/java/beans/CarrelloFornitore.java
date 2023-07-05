package beans;

import beans.Fornitore;
import beans.Prodotto;
import dao.FasceDAO;

import java.util.HashMap;
import java.util.Map;

public class CarrelloFornitore {
    private final Fornitore fornitore;
    private int prezzoSpedizione = 0;
    private int prezzoTotaleProdotti = 0;
    private int quantitaTotaleProdotti = 0;
    private Map<Prodotto, Integer> prodotti = new HashMap<Prodotto , Integer>();

    public CarrelloFornitore(Fornitore fornitore){
        this.fornitore = fornitore;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public int getPrezzoSpedizione() {
        return prezzoSpedizione;
    }

    public void setPrezzoSpedizione(int prezzoSpedizione) {
        this.prezzoSpedizione = prezzoSpedizione;
    }

    public int getPrezzoTotaleProdotti() {
        return prezzoTotaleProdotti;
    }

    public void setPrezzoTotaleProdotti(int prezzoTotaleProdotti) {
        this.prezzoTotaleProdotti = prezzoTotaleProdotti;
    }

    public int getQuantitaTotaleProdotti() {
        return quantitaTotaleProdotti;
    }

    public void setQuantitaTotaleProdotti(int quantitaTotaleProdotti) {
        this.quantitaTotaleProdotti = quantitaTotaleProdotti;
    }

    public Map<Prodotto, Integer> getProdotti() {
        return prodotti;
    }
}
