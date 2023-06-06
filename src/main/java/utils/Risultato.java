package utils;

import beans.Prodotto;

public class Risultato {

    private int codiceProdotto;
    private String nomeProdotto;
    private int prezzo;
    private boolean espandere = false;

    public boolean isEspandere() {
        return espandere;
    }
    public Risultato(){}
    public void setEspandere(boolean espandere) {
        this.espandere = espandere;
    }

    public int getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setCodiceProdotto(int codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

}
