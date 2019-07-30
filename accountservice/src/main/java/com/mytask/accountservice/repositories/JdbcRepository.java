package com.mytask.accountservice.repositories;

import com.mytask.accountservice.accountservice.Account;
import com.mytask.accountservice.accountservice.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Simple jdbc repository, uses h2 specific syntax
 * <p> Caches {@link Account} by id
 */

//  For current implementation data consistency is guaranteed only by java code;
//  don't make any updates to Account table other than AccountService;
//  don't use multiple instances of AccountService

//  To make possible multiple instances of AccountService updates, consider using db pessimistic locking;
//  e.g: SELECT * FROM ACCOUNTS WHERE id = ? FOR UPDATE; UPDATE ACCOUNTS SET id = ?, value = ? WHERE id = ?
@Repository
public class JdbcRepository implements AccountRepository {

    private static final RowMapper<Account> ACCOUNT_ROW_MAPPER = new AccountMapper();
    private static final String SELECT_FROM_ACCOUNTS_WHERE_ID = "SELECT * FROM ACCOUNTS WHERE id = ?";

    //  !H2 specific syntax; make sure desired db allows this syntax before db migration
    private static final String MERGE_INTO_ACCOUNTS_KEY_ID_VALUES = "MERGE INTO ACCOUNTS KEY(id) VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    @Cacheable(cacheNames = "account_cache")
    public Optional<Account> getById(Integer id) {
        List<Account> accounts = jdbcTemplate.query(SELECT_FROM_ACCOUNTS_WHERE_ID, new Object[]{id}, ACCOUNT_ROW_MAPPER);
        if (accounts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(accounts.get(0));
    }

    @Override
    @CachePut(cacheNames = "account_cache", key = "#account.id")
    public Account save(Account account) {
        jdbcTemplate.update(MERGE_INTO_ACCOUNTS_KEY_ID_VALUES, account.getId(), account.getValue());
        return account;
    }

    private static class AccountMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Account(rs.getInt("id"), rs.getLong("value"));
        }
    }
}
