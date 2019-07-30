package com.mytask.accountservice.repositories;

import com.mytask.accountservice.accountservice.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcRepositoryTest {

    @Autowired
    private JdbcRepository jdbcRepository;

    @Test
    public void getByIdEmpty() {
        Optional<Account> optionalAccount = jdbcRepository.getById(1);
        Assert.assertFalse(optionalAccount.isPresent());
    }

    @Test
    public void saveAndGet() {
        Account account = new Account(1, 100L);
        jdbcRepository.save(account);

        Optional<Account> optionalAccount = jdbcRepository.getById(1);
        Assert.assertTrue(optionalAccount.isPresent());
        Assert.assertEquals(optionalAccount.get(), account);
    }

    @Test
    public void saveUpdateAndGet() {
        jdbcRepository.save(new Account(1, 100L));
        jdbcRepository.save(new Account(1, 200L));

        Optional<Account> optionalAccount = jdbcRepository.getById(1);
        Assert.assertTrue(optionalAccount.isPresent());
        Assert.assertEquals(optionalAccount.get(), new Account(1, 200L));
    }
}