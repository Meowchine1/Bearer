package com.kateVoronina.demo.services;

import com.kateVoronina.demo.domain.User;
import com.kateVoronina.demo.exceptions.EtAuthException;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    User validateUser(String login, String password) throws EtAuthException, NoSuchAlgorithmException;

    User registerUser(String login, String password) throws EtAuthException, NoSuchAlgorithmException;
}
