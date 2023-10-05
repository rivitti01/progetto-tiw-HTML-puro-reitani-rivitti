package controllers;

import beans.Prodotto;
import utils.CarrelloFornitore;
import dao.CarrelloFornitoreDAO;
import dao.FornitoreDAO;
import dao.ProdottoDAO;
import dao.VendeDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AggiungiAlCarrello")
public class AggiungiAlCarrelloServlet extends ServletPadre{

    public AggiungiAlCarrelloServlet() {
        super();
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        FornitoreDAO fornitoreDAO = new FornitoreDAO(connection);

        //controlla che il codice del fornitore sia un numero
        int IDFornitore;
        try {
            IDFornitore = Integer.parseInt(request.getParameter("codiceFornitore"));
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il codice fornitore non è un numero");
            return;
        }

        //controllo che il fornitore esista
        try {
            if(fornitoreDAO.getFornitore(IDFornitore) == null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("il fornitore non esiste");
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        //controllo che il codice del prodotto sia un numero
        int IDProdotto;
        try {
            IDProdotto = Integer.parseInt(request.getParameter("codiceProdotto"));
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("il codice prodotto non è un numero");
            return;
        }

        //controllo che il prodotto esista
        try{
            if(prodottoDAO.getInformation(IDProdotto) == null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("il prodotto non esiste");
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //controllo che la quantita sia un numero
        int quantita;
        try {
            quantita = Integer.parseInt(request.getParameter("quantita"));
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("la quantita non è un numero");
            return;
        }

        //controllo che la quantita sia maggiore di 0
        if(quantita <= 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("la quantita non può essere minore di 1");
            return;
        }

        //controllo che il fornitore venda quel prodotto
        VendeDAO vendeDAO = new VendeDAO(connection);
        try {
            if(vendeDAO.getPrice(IDProdotto,IDFornitore) == -1){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("il fornitore non vende il prodotto selezionato");
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Verifico che già esista un carrello nella sessione. Se non esiste lo creo
        HashMap<Integer, CarrelloFornitore> carrello = (HashMap<Integer, CarrelloFornitore>) session.getAttribute("carrello");
        if (carrello == null) {
            carrello = new HashMap<>();
        }

        //prendo il carrello del fornitore, se non c'è lo creo
        CarrelloFornitore carrelloFornitore = carrello.get(IDFornitore);
        if (!carrello.keySet().contains(IDFornitore)) {
            try {
                carrelloFornitore = new CarrelloFornitore(fornitoreDAO.getFornitore(IDFornitore));
                carrello.put(IDFornitore, carrelloFornitore);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Errore nella creazione del cartello del fornitore");
                return;
            }
        }

        //controllo che il prodotto non sia già nel carrello
        try {
            Prodotto prodotto = prodottoDAO.getInformation(IDProdotto);
            boolean trovato = false;
            int veccchiaQuantita = 0;
            for (Prodotto p : carrelloFornitore.getProdotti().keySet()) {
                if (p.getCodiceProdotto()==(prodotto.getCodiceProdotto())) {
                    veccchiaQuantita = carrelloFornitore.getProdotti().get(p);
                    carrelloFornitore.getProdotti().remove(p);
                    trovato = true;
                    break;
                }
            }
            if(trovato){
                //se è presente aggiorno la quantita
                carrelloFornitore.getProdotti().put(prodotto, veccchiaQuantita + quantita);
            }else{
                //altrimenti lo aggiungo al carrello
                CarrelloFornitoreDAO carrelloFornitoreDAO = new CarrelloFornitoreDAO(carrelloFornitore);
                carrelloFornitoreDAO.addProdotto(prodottoDAO.getInformation(IDProdotto), quantita, connection);
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Errore nel caricamento del prodotto nel carrello");
            return;
        }

        //aggiorno il carrello nella sessione
        session.setAttribute("carrello", carrello);

        //aggiorno la pagina
        String path = getServletContext().getContextPath() + "/Carrello";
        response.sendRedirect(path);
    }

}
