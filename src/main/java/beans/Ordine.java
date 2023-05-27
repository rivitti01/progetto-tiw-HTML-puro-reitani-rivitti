package beans;

import java.util.Date;

public class Ordine {
    private int codice_ordine;
    private String nome_fornitore;
    private Date data_spedizione;
    private int prezzo_totale;
    private String email;

    public int getCodice_ordine() {
        return codice_ordine;
    }

    public void setCodice_ordine(int codice_ordine) {
        this.codice_ordine = codice_ordine;
    }

    public String getNome_fornitore() {
        return nome_fornitore;
    }

    public void setNome_fornitore(String nome_fornitore) {
        this.nome_fornitore = nome_fornitore;
    }

    public Date getData_spedizione() {
        return data_spedizione;
    }

    public void setData_spedizione(Date data_spedizione) {
        this.data_spedizione = data_spedizione;
    }

    public int getPrezzo_totale() {
        return prezzo_totale;
    }

    public void setPrezzo_totale(int prezzo_totale) {
        this.prezzo_totale = prezzo_totale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
