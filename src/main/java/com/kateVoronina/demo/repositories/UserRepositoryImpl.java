package com.kateVoronina.demo.repositories;

import com.kateVoronina.demo.domain.User;
import com.kateVoronina.demo.exceptions.EtAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private static final String SQL_CREATE = "INSERT INTO public.information (login, hash, salt) " +
            " VALUES(?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT id, login, hash, salt " +
            " FROM public.information WHERE id = ?";

    private static final String SQL_COUNT_BY_LOGIN = "SELECT COUNT(*) " +
            " FROM public.information WHERE login = ?";

    private static final String SQL_FIND_BY_LOGIN_AND_HASH = "SELECT id, login, hash, salt " +
            " FROM public.information WHERE id = ? and hash = ?";

    private static final String SQL_FIND_SALT_BY_LOGIN = "SELECT SALT " +
            " FROM public.information WHERE login = ?";
    private static final String SQL_FIND_HASH_BY_LOGIN = "SELECT HASH " +
            " FROM public.information WHERE login = ?";


    private static final String SQL_FIND_BY_LOGIN = "SELECT id, login, hash, salt " +
            " FROM public.information WHERE login = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String login, String password) {

        String salt = BCrypt.gensalt(10);
        String hashedPassword = BCrypt.hashpw(password, salt);
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, login);
                ps.setString(2, hashedPassword);
                ps.setString(3, salt);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("id");
        } catch (EtAuthException e){
            throw new EtAuthException("Failed to create account");
        }
    }

    @Override
    public Integer findByLogin(String login) throws EtAuthException {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_LOGIN, new Object[]{login}, Integer.class);

    }

    @Override
    public User findById(Integer id) throws EtAuthException {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, userRowMapper);
    }

    @Override
    public User findByLoginAndHash(String login, String password) throws EtAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_LOGIN, new Object[]{login}, userRowMapper);

            String salt = getSaltByLogin(login);
            String hashedpass = BCrypt.hashpw(password, salt);
         if(hashedpass.equals(getHashByLogin(login).trim())){
            return user;
         }
         else{
                throw new EtAuthException("Invalid login or password ");
            }
        } catch(EmptyResultDataAccessException e){
            throw new EtAuthException("user not found ");
        }
    }

    @Override
    public String getSaltByLogin(String login) throws EtAuthException {
       try{
           return jdbcTemplate.queryForObject(SQL_FIND_SALT_BY_LOGIN, new Object[]{login}, String.class);
       }
       catch(EmptyResultDataAccessException e){
           throw new EtAuthException("user not found ");
       }

    }


    @Override
    public String getHashByLogin(String login) throws EtAuthException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_HASH_BY_LOGIN, new Object[]{login}, String.class);
        }
        catch(EmptyResultDataAccessException e){
            throw new EtAuthException("user not found ");
        }

    }


    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getInt("id"),
                rs.getString("login"),
                rs.getString("hash"),
                rs.getString("salt"));
    });
}
