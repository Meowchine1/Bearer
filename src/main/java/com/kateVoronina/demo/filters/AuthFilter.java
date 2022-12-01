package com.kateVoronina.demo.filters;

import com.kateVoronina.demo.Constants;
import com.kateVoronina.demo.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpRequest.getHeader("Authorization");

        if(authHeader != null){
            String[] authheaderArr = authHeader.split("Bearer ");
            if(authHeader.length() > 1 && authheaderArr[1] != null){
                String token = authheaderArr[1];
                try{
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();
                    httpRequest.setAttribute("id", Integer.parseInt(claims.get("id").toString()));

                }
                catch(Exception e){
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid token");
                    return;
                }
            }
            else{
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Autorization token must be Bearer");
            }
        }
        else{
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Autorization token must be provided");

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
