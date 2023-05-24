package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void init() throws ServletException {
        // Inizializza il database
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Verifica le credenziali nel database
        boolean loginSuccessful = checkCredentials(email, password);

        if (loginSuccessful) {
            // Mostra la pagina di benvenuto
            response.setContentType("text/html"); // qui in realtà dovrebbe essere un redirect --> response.sendRedirect("home.html"); ma non va
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Benvenuto " + email + "</h2>");
            out.println("</body></html>");
        } else {
            // Mostra un messaggio di errore
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Credenziali errate</h2>");
            out.println("</body></html>");
        }
    }

    private boolean checkCredentials(String email, String password) {
        // Effettua la verifica delle credenziali nel database
        // Restituisce true se la corrispondenza avviene con successo, altrimenti false

        // Esempio di implementazione fittizia:
        return email.equals("io@email.com") && password.equals("pass");
    }
}
