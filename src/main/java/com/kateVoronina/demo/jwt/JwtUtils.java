package com.kateVoronina.demo.jwt;

import com.kateVoronina.demo.Constants;
import com.kateVoronina.demo.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JwtUtils {


    }