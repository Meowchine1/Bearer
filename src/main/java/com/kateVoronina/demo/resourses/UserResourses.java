package com.kateVoronina.demo.resourses;

import com.kateVoronina.demo.Constants;
import com.kateVoronina.demo.domain.RefreshToken;
import com.kateVoronina.demo.domain.TokenRefreshRequest;
import com.kateVoronina.demo.domain.User;
import com.kateVoronina.demo.services.JwtService;
import com.kateVoronina.demo.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserResourses {

    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletResponse response, @RequestBody Map<String, Object> tokenMap) {
        String requestRefreshToken = (String) tokenMap.get("token");
        User user = userService.getUserByLogin(getUsernameFromToken(requestRefreshToken));
        if (jwtService.validateLogin(user.getLogin()) > 0){
            //RefreshToken refreshToken = jwtService.findByToken(requestRefreshToken);
            //User user = userService.getUserById(refreshToken.getId());

            String accessToken = generateAccessJWTToken(user);
            String newRefreshToken = doGenerateRefreshToken(user.getLogin());

            jwtService.updateRefreshToken(user.getLogin(), newRefreshToken);
            Cookie cookie = new Cookie("accessToken", accessToken);
            cookie.setPath("/api/users/login");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            Cookie cookieRefresh = new Cookie("newrefreshtoken", newRefreshToken);
            cookieRefresh.setPath("/api/users/*");
            cookieRefresh.setHttpOnly(true);
            response.addCookie(cookieRefresh);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(HttpServletResponse response, @RequestBody Map<String, Object> userMap) throws NoSuchAlgorithmException {
        String login = (String) userMap.get("login");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(login, password);

        String accessToken = generateAccessJWTToken(user);
        String refreshToken = doGenerateRefreshToken(user.getLogin());

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setPath("/api/users/login");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);


        if(jwtService.validateLogin(user.getLogin()) > 0){
            Cookie cookieRefresh = new Cookie("refreshtoken", jwtService.getTokenById(user.getLogin()).trim());
            cookieRefresh.setPath("/api/users/*");
            cookieRefresh.setHttpOnly(true);
            response.addCookie(cookieRefresh);

        }
        else{
            Cookie cookieRefresh = new Cookie("refreshtoken", refreshToken);
            cookieRefresh.setPath("/api/users/*");
            cookieRefresh.setHttpOnly(true);
            response.addCookie(cookieRefresh);
            jwtService.createRefreshToken(user.getLogin(), refreshToken);//refresh
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(HttpServletResponse response,
                                                             @RequestBody Map<String, Object> userMap) throws NoSuchAlgorithmException {
        String login = (String) userMap.get("login");
        String password = (String) userMap.get("password");
       User user = userService.registerUser(login, password);
       return new ResponseEntity<>(HttpStatus.OK);
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    private  String generateAccessJWTToken(User user){
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

    public String doGenerateRefreshToken(String username) {

        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + Constants.TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, Constants.API_SECRET_KEY)
                .compact();

    }

}
