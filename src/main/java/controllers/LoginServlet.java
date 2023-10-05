package controllers;

import beans.Utente;
import dao.UtenteDAO;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class LoginServlet extends ServletPadre {

    public LoginServlet() {
        super();
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String error;
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            error = "Email e password sono obbligatorie";
            ctx.setVariable("error", error);
            templateEngine.process("/index.html", ctx, response.getWriter());
            return;
        }

        // Verifica le credenziali nel database
        boolean checkLogin;
        try {
            checkLogin = checkCredentials(email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String path;
        if (checkLogin) {

            // resetto la sessione esistente
            HttpSession session = request.getSession();
            while(session.getAttributeNames().hasMoreElements()){
                session.removeAttribute(session.getAttributeNames().nextElement());
            }

            //aggiorno la sessione
            session.setAttribute("email", email);
            path = getServletContext().getContextPath() + "/Home";

            // Mostra la pagina di benvenuto
            response.sendRedirect(path);
        } else {
            // Mostra un messaggio di errore
            error = "Email e/o password non validi.";
            ctx.setVariable("error", error);
            templateEngine.process("/index.html", ctx, response.getWriter());
        }
    }

    private boolean checkCredentials(String email, String password) throws SQLException {
        UtenteDAO checkCredentials = new UtenteDAO(connection);
        Utente utente = checkCredentials.checkCredentials(email, password);
        // Effettua la verifica delle credenziali nel database
        return utente != null;
    }


}
