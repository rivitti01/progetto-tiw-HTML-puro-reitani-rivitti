package controllers;

import beans.Ordine;
import beans.Prodotto;
import dao.OrdineDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/Ordini")
public class OrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public OrdiniServlet() {
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
        /*if (session.isNew() || session.getAttribute("email") == null) {
            String loginpath = getServletContext().getContextPath() + "/index.html";
            response.sendRedirect(loginpath);
            return;
        }*/
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        String email = (String) session.getAttribute("email");
        /*List<Ordine> ordini;
        try {
            ordini = getOrdini(email);
            if (ordini == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No orders found");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        Map<Ordine,List<Prodotto>> ordini;
        try {
            ordini = getOrdini2(email);
            if (ordini == null) {
                ctx.setVariable("error", "No orders found");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ctx.setVariable("ordini", ordini);
        try {
            templateEngine.process("WEB-INF/ordini.html", ctx, response.getWriter());
        }catch (Exception ex){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }

    }

    private List<Ordine> getOrdini(String email) throws SQLException {
        OrdineDAO ordineDAO = new OrdineDAO(connection);
        List<Ordine> ordini = ordineDAO.getOrdersByEmail(email);
        return ordini;
    }
    private Map<Ordine,List<Prodotto>> getOrdini2(String email) throws SQLException {
        OrdineDAO ordineDAO = new OrdineDAO(connection);
        Map<Ordine,List<Prodotto>> ordini2 = ordineDAO.getOrdersByEmail2(email);
        return ordini2;
    }
    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
