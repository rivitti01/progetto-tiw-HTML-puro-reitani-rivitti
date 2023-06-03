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
        // Controlla se l'utente è già loggato, in caso positivo va direttamente alla home
        HttpSession session = request.getSession();
        /*if (session.isNew() || session.getAttribute("email") == null) {
            String loginpath = getServletContext().getContextPath() + "/index.html";
            response.sendRedirect(loginpath);
            return;
        }*/
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        String email = (String) session.getAttribute("email");

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
            throw new RuntimeException(e);
        }
        ctx.setVariable("risultati", risultati);


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
