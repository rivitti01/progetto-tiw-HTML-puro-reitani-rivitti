package controllers;

import beans.CarrelloFornitore;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

@WebServlet("/CreaOrdine")
public class CreaOrdineServlet extends ServletPadre{

    public CreaOrdineServlet() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        //controlla che il codice del fornitore sia un numero
        int IDFornitore;
        try {
            IDFornitore = Integer.parseInt(request.getParameter("codiceFornitore"));
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il codice fornitore non è un numero");
            return;
        }

        //controllo che il fornitore sia presente nel carrello
        HashMap<Integer, CarrelloFornitore> carrello = (HashMap<Integer, CarrelloFornitore>) session.getAttribute("carrello");
        if(!carrello.containsKey(IDFornitore)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il fornitore non è presente nel carrello");
            return;
        }

        //creo un nuovo ordine per quel fornitore
        try {
            connection.setAutoCommit(false);
            //aggiorna le tabelle


            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }


        //aggiorno il carrello
        carrello.remove(IDFornitore);

        //aggiorno la sessione
        session.setAttribute("carrello", carrello);

        //mostro la nuova pagina degli ordini
        String path = getServletContext().getContextPath() + "/Ordini";
        response.sendRedirect(path);


    }

}
