package com.example.demo1;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet(name = "supersecret", value = "/secret/supersecret")
public class SuperSecretServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JWTVerifier jwtVerifier = JWT.require(CookieVaultServlet.ALGORITHM).build();
        Optional<Boolean> secretValue;
        try { // todo error handling
            secretValue = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("JWT"))
                    .findAny()
                    .map(Cookie::getValue)
                    .map(jwtVerifier::verify)
                    .map(DecodedJWT::getClaims)
                    .map(map -> map.get(CookieVaultServlet.RIDDLE))
                    .map(Claim::asString)
                    .map(answer -> answer.equals("popcorn"));
        } catch (JWTVerificationException verificationException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Wrong Answer... or Secret?" + verificationException.getMessage());
            return;
        }
        if (secretValue.isPresent() || !secretValue.get()) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Wrong Answer!! Maybe watch some movies...");
            return;
        }
        String pirateShip =
                "                   |    |    |                   \n" +
                        "                   )_)  )_)  )_)                   \n" +
                        "                  )___))___))___)\\                \n" +
                        "                 )____)____)_____)\\              \n" +
                        "               _____|____|____|____\\\\__\\__          \n" +
                        "  ~~~~~~~~\\`\\`\\`\\`\\`\\`\\`\\`\\`\\`\\`\\`|~~~~~/ /~~~~~~~ \n" +
                        "           \\`\\`\\`\\`\\`\\`\\`\\`\\`\\`\\`\\`\\`/ /          \n" +
                        "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
        response.setContentType("text/plain");
        response.getWriter().println("Arrr, ye found ye way to the super secret treasure!");
        response.getWriter().println(pirateShip);
    }
}
