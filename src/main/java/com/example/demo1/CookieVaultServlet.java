package com.example.demo1;

import java.io.*;
import java.time.LocalDate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "cookieVaultServlet", value = "/the-cookie-vault")
public class CookieVaultServlet extends HttpServlet {

    public static final Algorithm ALGORITHM = Algorithm.HMAC256("tomcat");
    public static final String RIDDLE = "Avast, what be cracklin' when ye heat it in the galley, a favored snack while watchin' moving pictures";
    private String message;

    public void init() {
        message = "Hello Mate!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        String jwt = JWT.create()
                .withClaim("Secret", "Hello Hacker. This is your price.")
                .withClaim(RIDDLE, "")
                .sign(ALGORITHM);
        PrintWriter out = response.getWriter(); //todo html
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<div>"+ "you fond your JWT! "
                +jwt
                +" I Stored it in your cookies."
                +"<div>");
        out.println("</body></html>");
        Cookie cookie = new Cookie("JWT", jwt);
        response.addCookie(cookie);
    }
}