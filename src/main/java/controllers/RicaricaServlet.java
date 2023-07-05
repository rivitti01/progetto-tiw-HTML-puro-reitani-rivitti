package controllers;

import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/Ricarica")
public class RicaricaServlet extends ServletPadre{

    public RicaricaServlet() {
        super();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        WebContext ctx = (WebContext) session.getAttribute("ctx");

        templateEngine.process("WEB-INF/risultati.html", ctx, response.getWriter());
    }

}
