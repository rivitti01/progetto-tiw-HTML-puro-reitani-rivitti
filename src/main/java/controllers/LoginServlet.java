package controllers;

import beans.Utente;
import dao.CheckCredentials;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public LoginServlet() {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email e password non possono essere vuoti");
            return;
        }

        // Verifica le credenziali nel database
        boolean checkLogin = false;
        try {
            checkLogin = checkCredentials(email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String path;
        if (checkLogin) {
            // Mostra la pagina di benvenuto
            //response.sendRedirect("home.html");

            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/WEB-INF/home.html");
        } else {
            // Mostra un messaggio di errore
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Credenziali errate</h2>");
            out.println("</body></html>");
        }
    }

    private boolean checkCredentials(String email, String password) throws SQLException {
        CheckCredentials checkCredentials = new CheckCredentials(connection);
        // Effettua la verifica delle credenziali nel database

         if(checkCredentials.checkCredentials(email, password) != null){
             return true;
         }
         else{
             return false;
         }
    }

    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
