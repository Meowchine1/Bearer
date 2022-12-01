package com.kateVoronina.demo.resourses;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.PushBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/refresh")
public class RegreshResourse {


    @GetMapping("")
    public String getIdByCookie(HttpServletRequest request){
            int userid = (Integer) request.getAttribute("id");
            return"Autentificated! User ID is " + userid;
    }




}
