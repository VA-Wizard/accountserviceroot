package com.mytask.accountservice.accountservice;

import com.mytask.accountservice.accountservice.annotations.Auditable;
import com.mytask.accountservice.accountservice.annotations.LockByParameter;
import com.mytask.accountservice.accountservice.annotations.LockKey;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    @Auditable
    @LockByParameter
    public Long getAmount(@LockKey @NonNull Integer id) {
        log.debug("getAmount.enter; id={}", id);
        Long amount = accountRepository.getById(id)
                .map(Account::getValue)
                .orElse(0L);
        log.debug("getAmount.exit; amount={}", amount);
        return amount;
    }


    //  Assume we can store negative balance, no checks
    @Override
    @Auditable
    @LockByParameter
    public void addAmount(@LockKey @NonNull Integer id, Long value) {
        log.debug("addAmount.enter; id={}; addValue={}", id, value);
        Account account = accountRepository.getById(id).orElse(new Account(id, 0L));
        account.setValue(account.getValue() + value);
        accountRepository.save(account);
        log.debug("addAmount.exit;");
    }
}
