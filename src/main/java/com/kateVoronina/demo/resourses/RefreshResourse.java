package com.kateVoronina.demo.resourses;


import com.kateVoronina.demo.Constants;
import com.kateVoronina.demo.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("api/tokens")
public class RefreshResourse {

    @GetMapping("")
    public String tokens(HttpServletRequest request){
        int userid = (Integer) request.getAttribute("id");
        return"Autentificated! User ID is " + userid;
    }



    private  String generateJWTToken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)  //  podpis
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("id", user.getUserId())
                .claim("login", user.getLogin())
                .claim("hash", user.getHash())
                .claim("salt", user.getSalt())
                .compact();

        return token;
    }
    public String doGenerateRefreshToken(User user) {

        return Jwts.builder().claim("id", user.getUserId())
                .claim("login", user.getLogin())
                .claim("hash", user.getHash())
                .claim("salt", user.getSalt())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, Constants.API_SECRET_KEY).compact();

    }
}
