package com.kateVoronina.demo.resourses;

import com.kateVoronina.demo.Constants;
import com.kateVoronina.demo.domain.User;
import com.kateVoronina.demo.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserResourses {

    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) throws NoSuchAlgorithmException {

        String login = (String) userMap.get("login");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(login, password);
        Map<String, String> map = new HashMap<>();
        map.put("token", generateAccessJWTToken(user));
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) throws NoSuchAlgorithmException {

        String login = (String) userMap.get("login");
        String password = (String) userMap.get("password");

       User user = userService.registerUser(login, password);
       String accessToken = generateAccessJWTToken(user);
       String refreshToken = doGenerateRefreshToken(user);

       Map<String, String> tokens = new HashMap<>();
       tokens.put("acess token", accessToken);
       tokens.put("refresh token", refreshToken);
       return new ResponseEntity<>(tokens, HttpStatus.OK);
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

    //Рефреш токен (RT) — эти токены выполняют только одну специфичную задачу — получение нового токена доступа.
    // И на этот раз без сервера авторизации не обойтись. Они долгоживущие, но одноразовые.

    //Ключевая идея разделения токенов состоит в том, что, с одной стороны,
    // токены авторизации позволяют нам легко проверять пользователя без участия сервера авторизации, просто сравнением подписей.
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
