//TODO: aggiustare come salvare il prezzo unitario

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

        HttpSession session = request.getSession();
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        List<Risultato> risultati = (List<Risultato>) session.getAttribute("risultati");

        //cerco il risultato da modificare il valore espandere
        for(Risultato r : risultati){
            if (r.getCodiceProdotto() == ( Integer.parseInt(request.getParameter("codiceProdotto")))){
                r.setEspandere(!r.isEspandere());
            }
        }
        session.setAttribute("risultati", risultati);
        ctx.setVariable("risultati", risultati);


        //create the maps for expanding the results
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        FasceDAO fasceDAO = new FasceDAO(connection);
        VendeDAO vendeDAO = new VendeDAO(connection);
        FornitoreDAO fornitoreDAO = new FornitoreDAO(connection);
        HashMap<Prodotto, List<Fornitore>> fornitoreMap = new HashMap<>();
        HashMap < Fornitore, List<Fasce>> fasceMap = new HashMap<>();
        HashMap < Fornitore, HashMap > prezzoUnitarioMap = new HashMap<>();
        HashMap < Risultato, Prodotto> prodottoMap = new HashMap<>();
        for(Risultato r : risultati){
            if (r.isEspandere()){
                try {
                    HashMap <Risultato, Integer> ausiliariaMap = new HashMap<>();
                    Prodotto p = prodottoDAO.getInformation(r.getCodiceProdotto());
                    List<Fornitore> fornitori = vendeDAO.getFornitori(p.getCodiceProdotto());
                    for (Fornitore f : fornitori){
                        List<Fasce> fasce = fasceDAO.getFasce(f.getCodiceFornitore());
                        fasceMap.put(f, fasce);

                        int prezzoUnitario = vendeDAO.getPrice(f.getCodiceFornitore(), r.getCodiceProdotto());
                        ausiliariaMap.put(r, prezzoUnitario);
                        prezzoUnitarioMap.put(f , ausiliariaMap);
                    }

                    prodottoMap.put(r, p);
                    fornitoreMap.put(p, fornitori);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ctx.setVariable("fornitoreMap", fornitoreMap);
        ctx.setVariable("fasceMap", fasceMap);
        ctx.setVariable("prezzoUnitarioMap", prezzoUnitarioMap);
        ctx.setVariable("prodottoMap", prodottoMap);

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
