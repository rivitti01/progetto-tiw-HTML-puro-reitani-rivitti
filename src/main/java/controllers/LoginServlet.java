package controllers;

import beans.Utente;
import dao.UserDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
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
        String error;
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            error = "Email e password sono obbligatorie";
            ctx.setVariable("error", error);
            templateEngine.process("/index.html", ctx, response.getWriter());
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
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            path = getServletContext().getContextPath() + "/Home";
            response.sendRedirect(path);
        } else {
            // Mostra un messaggio di errore
            error = "Email e/o password non validi.";
            ctx.setVariable("error", error);
            templateEngine.process("/index.html", ctx, response.getWriter());
        }
    }

    private boolean checkCredentials(String email, String password) throws SQLException {
        UserDAO checkCredentials = new UserDAO(connection);
        Utente utente = checkCredentials.checkCredentials(email, password);
        // Effettua la verifica delle credenziali nel database
         if(utente != null){
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
