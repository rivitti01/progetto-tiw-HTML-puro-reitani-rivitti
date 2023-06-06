//TODO: aggiustare perchè il prezzo del primo fornitore a volte è -1

package controllers;

import beans.Fasce;
import beans.Fornitore;
import beans.Prodotto;
import dao.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;
import utils.Risultato;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/Espandi")
public class EspandiServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public EspandiServlet() {
        super();
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //controlla che ci sia la parola
        String word = request.getParameter("word");
        if(word == null || word.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il campo di ricerca è vuoto");
            return;
        }

        //controlla che ci sia il codice del prodotto da espandere
        String scodiceProdottoDaEspandere = request.getParameter("codiceProdotto");
        if(scodiceProdottoDaEspandere == null || scodiceProdottoDaEspandere.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il codice prodotto non può essere vuoto");
            return;
        }

        //controlla che il codice del prodotto da espandere sia un numero
        Integer codiceProdottoDaEspandere;
        try{
            codiceProdottoDaEspandere = Integer.parseInt(scodiceProdottoDaEspandere);
        }catch (NumberFormatException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il codice prodotto non è un numero");
            return;
        }

        String[] scodiciProdottiGiaEspansi = request.getParameterValues("codiceProdottoEspanso");
        if(scodiciProdottiGiaEspansi == null){
            scodiciProdottiGiaEspansi = new String[0];
        }

        //controlla che i codici dei prodotti gia espansi siano numeri
        List<Integer> codiciProdottiGiaEspansi = new ArrayList<>();
        for(String scodice : scodiciProdottiGiaEspansi){
            try{
                codiciProdottiGiaEspansi.add(Integer.parseInt(scodice));
            }catch (NumberFormatException ex){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("il codice prodotto non è un numero");
                return;
            }
        }

        RisultatoDAO risultatoDAO = new RisultatoDAO(connection);
        List<Risultato> risultati;

        //prende i risultati dalla parola
        try{
            risultati = risultatoDAO.searchByWord(word);
        }catch(SQLException ex){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Errore nel caricamento dei risultati");
            return;
        }

        //controlla che il codice del prodotto da espandere sia attinente alla ricerca
        if(risultati.stream().filter(x -> x.getCodiceProdotto() == codiceProdottoDaEspandere).count() == 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Codice prodotto da espandere non attinente la ricerca");
            return;
        }
        for(Integer i : codiciProdottiGiaEspansi){
            if(risultati.stream().filter(x -> x.getCodiceProdotto() == i).count() == 0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Codice prodotto non attinente la ricerca");
                return;
            }
        }

        //TODO: Aggiungi nel database che hai visualizzato il prodotto (solo se non era già aperto)

        //modificare il valore espandere nei risultati da espandere
        for(Risultato r : risultati){
            boolean espandere = (
                    codiciProdottiGiaEspansi.contains(r.getCodiceProdotto()) &&
                    r.getCodiceProdotto() != (codiceProdottoDaEspandere))

                    || (r.getCodiceProdotto() == (codiceProdottoDaEspandere) &&
                            !codiciProdottiGiaEspansi.contains(r.getCodiceProdotto()));
            r.setEspandere(espandere);
        }

        //creo il path per mandare alla servlet Ricerca tutti i dati
        String path = getServletContext().getContextPath() + "/Ricerca?word=" + word ;
        for(int i = 0;i<risultati.size();i++){
            if(risultati.get(i).isEspandere()){
                path += "&codiceProdottoEspanso=" + risultati.get(i).getCodiceProdotto();
            }
        }

        response.sendRedirect(path);

    }

        public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
