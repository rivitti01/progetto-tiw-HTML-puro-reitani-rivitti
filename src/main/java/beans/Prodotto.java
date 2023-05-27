package beans;

public class Prodotto {
    private int codice_prodotto;
    private String nome_prodotto;
    private String categoria;
    private String foto;
    private String descrizione;

    public int getCodice_prodotto() {
        return codice_prodotto;
    }

    public void setCodice_prodotto(int codice_prodotto) {
        this.codice_prodotto = codice_prodotto;
    }

    public String getNome_prodotto() {
        return nome_prodotto;
    }

    public void setNome_prodotto(String nome_prodotto) {
        this.nome_prodotto = nome_prodotto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
