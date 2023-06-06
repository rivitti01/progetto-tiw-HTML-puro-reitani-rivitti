package controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import beans.Fasce;
import beans.Fornitore;
import beans.Prodotto;
import beans.Utente;
import dao.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;
import utils.Risultato;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet("/Ricerca")
public class RicercaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public RicercaServlet() {
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

        //controllo che i codici dei prodotti espansi siano validi
        String[] scodiciDaEspandere = request.getParameterValues("codiceProdottoEspanso");
        List<Integer> codiciDaEspndere = new ArrayList<>();
        if(scodiciDaEspandere != null) {
            for (String c : scodiciDaEspandere) {
                int cod;
                try {
                    cod = Integer.parseInt(c);
                } catch (NumberFormatException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "il codice prodotto non è un numero");
                    return;
                }

                if (cod <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "il codice prodotto non è un numero valido");

                    return;
                }
                codiciDaEspndere.add(cod);
            }
        }


        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        String word = request.getParameter("word");

        //Verifica che word sia presente
        if (word == null || word.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il campo di ricerca è vuoto");
            return;
        }

        //ricerca i prodotti per parola
        RisultatoDAO risultatoDAO = new RisultatoDAO(connection);
        List<Risultato> risultati;
        try {
            risultati = risultatoDAO.searchByWord(word);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Query ricerca fallita");
            return;
        }

        //verifica che i codici prodotto da espandere siano effettivamente presenti nella ricerca
        for(Integer codice : codiciDaEspndere){
            if(risultati.stream().filter(r -> r.getCodiceProdotto() == codice).count() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "il codice prodotto non è valido");
                return;
            }
        }

        //setta gli attributi per la visualizzazione dei risultati
        for(Risultato r : risultati){
            if(codiciDaEspndere.contains(r.getCodiceProdotto())){
                r.setEspandere(true);
            }
        }

        //crea le mappe per la visualizzazione dei risultati
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        FasceDAO fasceDAO = new FasceDAO(connection);
        VendeDAO vendeDAO = new VendeDAO(connection);
        FornitoreDAO fornitoreDAO = new FornitoreDAO(connection);

        HashMap<Prodotto, List<Fornitore>> fornitoreMap = new HashMap<>();
        HashMap < Fornitore, List<Fasce>> fasceMap = new HashMap<>();
        HashMap < Risultato, Prodotto> prodottoMap = new HashMap<>();
        HashMap < Fornitore, HashMap > prezzoUnitarioMap = new HashMap<>();
        for(Risultato r : risultati){
            if (r.isEspandere()){
                try {
                    Prodotto p = prodottoDAO.getInformation(r.getCodiceProdotto());
                    List<Fornitore> fornitori = vendeDAO.getFornitori(p.getCodiceProdotto());
                    for (Fornitore f : fornitori){
                        HashMap <Risultato, Integer> ausiliariaMap = new HashMap<>();
                        List<Fasce> fasce = fasceDAO.getFasce(f.getCodiceFornitore());
                        fasceMap.put(f, fasce);

                        int prezzoUnitario = vendeDAO.getPrice(f.getCodiceFornitore(), r.getCodiceProdotto());
                        ausiliariaMap.put(r, prezzoUnitario);
                        prezzoUnitarioMap.put(f , ausiliariaMap);
                    }

                    prodottoMap.put(r, p);
                    fornitoreMap.put(p, fornitori);
                } catch (SQLException e) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            }
        }
        ctx.setVariable("fornitoreMap", fornitoreMap);
        ctx.setVariable("fasceMap", fasceMap);
        ctx.setVariable("prodottoMap", prodottoMap);
        ctx.setVariable("prezzoUnitarioMap", prezzoUnitarioMap);
        ctx.setVariable("risultati", risultati);
        ctx.setVariable("word", word);


        templateEngine.process("WEB-INF/risultati.html", ctx, response.getWriter());

    }

    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
