package com.kateVoronina.demo.repositories;

import com.kateVoronina.demo.domain.User;
import com.kateVoronina.demo.exceptions.EtAuthException;

public interface UserRepository {

    Integer create(String login, String hash);

    Integer findCountByLogin(String login) throws EtAuthException;

    User findByLogin(String login) throws EtAuthException;

    User findById(Integer id) throws EtAuthException;

    User findByLoginAndHash(String login, String password) throws EtAuthException;

    String getSaltByLogin(String login) throws EtAuthException;

    String getHashByLogin(String login) throws EtAuthException;
}
