package com.kateVoronina.demo.services;

import com.kateVoronina.demo.domain.User;
import com.kateVoronina.demo.exceptions.EtAuthException;
import com.kateVoronina.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
@Transactional
public class UserServiceImpl implements UserService{


    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String login, String password) throws EtAuthException, NoSuchAlgorithmException {
        if(login != null & password != null){

         //   String salt = userRepository.getSaltByLogin(login);
        //    String hashedPassword = BCrypt.hashpw(password, salt);

            return userRepository.findByLoginAndHash(login, password);
        }
        throw new EtAuthException("login and/or password are null objects");
    }

    @Override
    public User registerUser(String login, String password) throws EtAuthException, NoSuchAlgorithmException {
        if(login != null & password != null){
            if(userRepository.findCountByLogin(login) > 0){
                throw new EtAuthException("Login already in use");
            }
            Integer userId = userRepository.create(login, password);
            return userRepository.findById(userId);
        }
        else{
            throw new EtAuthException("login and/or password are null objects");
        }
    }

    @Override
    public  User getUserById(int id){
        return userRepository.findById(id);

    }

    @Override
    public  User getUserByLogin(String login){
        return userRepository.findByLogin(login);

    }
}
