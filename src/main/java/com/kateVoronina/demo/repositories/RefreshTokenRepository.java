package com.kateVoronina.demo.repositories;

import com.kateVoronina.demo.domain.RefreshToken;
import com.kateVoronina.demo.exceptions.EtAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class RefreshTokenRepository {
    private static final String SQL_CREATE = "INSERT INTO JWT (login, token) " +
            " VALUES(?, ?)";
    private static final String SQL_UPDATE = "UPDATE JWT " +
            "SET token = ? where login = ?";

    private static final String SQL_FIND_BY_ID = "SELECT id, login, token  " +
            " FROM JWT WHERE id = ?";

    private static final String SQL_FIND_TOKEN_BY_LOGIN = "SELECT token  " +
            " FROM JWT WHERE login = ?";

    private static final String SQL_COUNT_BY_LOGIN = "SELECT COUNT(*) " +
            " FROM JWT WHERE login = ?";

    private static final String SQL_FIND_BY_TOKEN = "SELECT id, login, token " +
            " FROM JWT WHERE token = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;


    public void updateToken(String login, String token){
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_UPDATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, login);
                ps.setString(2, token);
                return ps;
            }, keyHolder);
        } catch (EtAuthException e){
            throw new EtAuthException("Failed to update refresh token");
        }

    }

    public Integer create(String login, String token) {

        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, login);
                ps.setString(2, token);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("id");
        } catch (EtAuthException e){
            throw new EtAuthException("Failed to create account");
        }
    }


    public Integer findCountByLogin(String login) throws EtAuthException {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_LOGIN, new Object[]{login}, Integer.class);

    }

    public RefreshToken findById(Integer id) throws EtAuthException {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, tokenRowMapper);
    }

    public String findTokenById(String login) throws EtAuthException {
        return jdbcTemplate.queryForObject(SQL_FIND_TOKEN_BY_LOGIN, new Object[]{login}, String.class);
    }


    public RefreshToken findByToken(String token) throws EtAuthException {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_TOKEN, new Object[]{token}, tokenRowMapper);
    }

    private RowMapper<RefreshToken> tokenRowMapper = ((rs, rowNum) -> {
        return new RefreshToken(rs.getInt("id"),
                rs.getString("login"),
                rs.getString("token"));
    });
}
