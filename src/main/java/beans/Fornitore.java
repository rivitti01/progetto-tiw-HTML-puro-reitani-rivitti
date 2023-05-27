package beans;

public class Fornitore {
    private int codice_fornitore;
    private String nome_fornitore;
    private int valutazione;
    private int soglia;

    public int getCodice_fornitore() {
        return codice_fornitore;
    }

    public void setCodice_fornitore(int codice_fornitore) {
        this.codice_fornitore = codice_fornitore;
    }

    public String getNome_fornitore() {
        return nome_fornitore;
    }

    public void setNome_fornitore(String nome_fornitore) {
        this.nome_fornitore = nome_fornitore;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public int getSoglia() {
        return soglia;
    }

    public void setSoglia(int soglia) {
        this.soglia = soglia;
    }
}
