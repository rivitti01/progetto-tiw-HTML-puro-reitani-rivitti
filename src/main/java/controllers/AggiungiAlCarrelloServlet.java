package controllers;

import beans.CarrelloFornitore;
import dao.CarrelloFornitoreDAO;
import dao.FornitoreDAO;
import dao.ProdottoDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AggiungiAlCarrello")
public class AggiungiAlCarrelloServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public AggiungiAlCarrelloServlet() {
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        FornitoreDAO fornitoreDAO = new FornitoreDAO(connection);
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);

        //Verifico che già esista un carrello nella sessione. Se non esiste lo creo
        HashMap<Integer, CarrelloFornitore> carrello = (HashMap<Integer, CarrelloFornitore>) session.getAttribute("carrello");
        if(carrello == null){
            carrello = new HashMap<Integer, CarrelloFornitore>();
            session.setAttribute("carrello", carrello);
        }

        //prendo il carrello del fornitore, se non c'è lo creo
        int IDFornitore = Integer.parseInt(request.getParameter("codiceFornitore"));
        CarrelloFornitore carrelloFornitore = carrello.get(IDFornitore);
        if(!carrello.keySet().contains(IDFornitore)){
            try {
                carrelloFornitore = new CarrelloFornitore(fornitoreDAO.getFornitore(IDFornitore));
                carrello.put(IDFornitore, carrelloFornitore);
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }
        }

        //aggiungo il prodotto al carrello
        CarrelloFornitoreDAO carrelloFornitoreDAO = new CarrelloFornitoreDAO(carrelloFornitore);
        try {
            carrelloFornitoreDAO.addProdotto( prodottoDAO.getInformation(Integer.parseInt(request.getParameter("codiceProdotto"))) , Integer.parseInt(request.getParameter("quantità")), connection);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        //aggiorno il carrello nella sessione
        session.setAttribute("carrello", carrello);

        //aggiorno la pagina
        ctx.setVariable("carrello", carrello);
        String path = getServletContext().getContextPath() + "/Carrello";
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
