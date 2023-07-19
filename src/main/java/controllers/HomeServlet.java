package controllers;

import beans.Prodotto;
import beans.Visualizza;
import dao.ProdottoDAO;
import dao.VisualizzaDAO;
import org.thymeleaf.context.WebContext;
import utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
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
        HttpSession session = request.getSession();
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        String email = (String) session.getAttribute("email");


        List<Prodotto> products;
        try {
            // Preleva i prodotti dal DB
            products = getFiveProducts(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Map<Integer,String> fotoMap = new LinkedHashMap<>();
        for (Prodotto prodotto : products){
            try {
                fotoMap.put(prodotto.getCodiceProdotto(),Base64.getEncoder().encodeToString(prodotto.getFoto().getBytes(1, (int) prodotto.getFoto().length())));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        ctx.setVariable("fotoMap", fotoMap);
        ctx.setVariable("products", products);


        // Passa i prodotti alla vista Thymeleaf
        templateEngine.process("WEB-INF/home.html", ctx, response.getWriter());
    }
    private List<Prodotto> getFiveProducts(String email) throws SQLException {
        VisualizzaDAO visualizzaDAO = new VisualizzaDAO(connection);
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        List<Visualizza> visualizza = null;
        List<Prodotto> prodotti;
        try {
            // Preleva gli ultimi 5 prodotti visiti dal DB
            visualizza = visualizzaDAO.getLAstFive(email);

            //prendo i dati dei prodotti
            prodotti = prodottoDAO.getFiveVisualizedProduct(visualizza);

            //se i prodotti sono meno di 5 prendo completo la lista con dei prodotti scontati
            if (prodotti.size()< Constants.NUMBER_HOME_PRODUCT){
                prodotti = prodottoDAO.completeListVisualized(prodotti);
            }
            return prodotti;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
