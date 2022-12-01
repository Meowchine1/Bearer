package com.kateVoronina.demo.services;

import com.kateVoronina.demo.domain.RefreshToken;
import com.kateVoronina.demo.exceptions.EtAuthException;
import com.kateVoronina.demo.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
@Transactional
public class JwtService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    public Integer validateLogin(String login) throws EtAuthException{
        if(login != null){

            return refreshTokenRepository.findCountByLogin(login);
        }
        throw new EtAuthException("token is null objects");
    }

    public RefreshToken createRefreshToken(String login, String password) throws EtAuthException, NoSuchAlgorithmException {
        if(login != null & password != null){
            if(refreshTokenRepository.findCountByLogin(login) > 0){
                throw new EtAuthException("Login already in use");
            }
            Integer userId = refreshTokenRepository.create(login, password);
            return refreshTokenRepository.findById(userId);
        }
        else{
            throw new EtAuthException("login and/or password are null objects");
        }
    }

    public void updateRefreshToken(String login, String token){
        if(login != null && token != null){
            if(refreshTokenRepository.findCountByLogin(login) > 0){
                refreshTokenRepository.updateToken(login, token);
            }
            else{
                throw new EtAuthException("login unreal");
            }
        }
        else{
            throw new EtAuthException("login and/or password are null objects");
        }

    }

    public RefreshToken findByToken(String requestRefreshToken) {
       return refreshTokenRepository.findByToken(requestRefreshToken);
    }



    public String getTokenById(String login) {
        return refreshTokenRepository.findTokenById(login);
    }
}