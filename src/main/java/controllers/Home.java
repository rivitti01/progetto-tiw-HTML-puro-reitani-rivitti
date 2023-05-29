package controllers;

import beans.Prodotto;
import beans.Utente;
import beans.Visualizza;
import dao.ProdottoDAO;
import dao.UserDAO;
import dao.VisualizzaDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/Home")
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;
    public Home() {
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
        if (session.isNew() || session.getAttribute("email") == null) {
            String loginpath = getServletContext().getContextPath() + "/index.html";
            response.sendRedirect(loginpath);
            return;
        }
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        String email = (String) session.getAttribute("email");
        List<Prodotto> products;
        try {
            products = getFiveProducts(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ctx.setVariable("products", products);


        // Passa i prodotti alla vista Thymeleaf
        //ctx.setAttribute("products", products);
        //request.getRequestDispatcher("home.html").forward(request, response);
        templateEngine.process("WEB-INF/home.html", ctx, response.getWriter());
    }
    private List<Prodotto> getFiveProducts(String email) throws SQLException {
        VisualizzaDAO visualizzaDAO = new VisualizzaDAO(connection);
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        List<Visualizza> visualizza = null;
        try {
            visualizza = visualizzaDAO.getLAstFive(email);
            return prodottoDAO.getFiveVisualizedProduct(visualizza);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
