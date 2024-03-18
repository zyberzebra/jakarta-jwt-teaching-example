package com.example.demo1;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

@WebServlet(name = "secretServlet", value = "/secret")
public class SecretServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JWTVerifier jwtVerifier = JWT.require(CookieVaultServlet.ALGORITHM).build();
        Optional<String> jwt = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("JWT"))
                .findAny()
                .map(Cookie::getValue)
                .map(jwtVerifier::verify)
                .map(DecodedJWT::getClaims)
                .map(map -> map.get("Secret"))
                .map(Claim::asString);

        PrintWriter out = response.getWriter();
        jwt.ifPresentOrElse((secret) -> {
            out.println("<html><body>");
            out.println("<h1>" + "Here is the secret I hid in your cookie" + "</h1>");
            out.println("<div>" + secret + "</div>");
            out.println("</body></html>");
        },()->out.println("<html><body>I told you to get your cookie first mate...</html></body>"));

    }
}
