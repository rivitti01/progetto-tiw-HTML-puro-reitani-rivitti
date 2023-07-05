package controllers;

import beans.Prodotto;
import beans.Visualizza;
import dao.ProdottoDAO;
import dao.VisualizzaDAO;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/Home")
public class HomeServlet extends ServletPadre {
    public HomeServlet() {
        super();
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
        List<Prodotto> products;
        try {
            products = getFiveProducts(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ctx.setVariable("products", products);


        // Passa i prodotti alla vista Thymeleaf
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
