package com.mytask.accountservice.accountservice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;


    @Test
    public void getAmount() {
        when(accountRepository.getById(1)).thenReturn(Optional.of(new Account(1, 100L)));
        long amount = accountService.getAmount(1);
        Assert.assertEquals(amount, 100L);
    }

    @Test
    public void getAmountNew() {
        when(accountRepository.getById(1)).thenReturn(Optional.empty());
        long amount = accountService.getAmount(1);
        Assert.assertEquals(amount, 0L);
    }

    @Test
    public void addAmountUpdate() {
        Account account = new Account(1, 100L);
        when(accountRepository.getById(1)).thenReturn(Optional.of(account));
        accountService.addAmount(1, -50L);
        Assert.assertEquals(account.getValue().longValue(), 50L);
    }

}