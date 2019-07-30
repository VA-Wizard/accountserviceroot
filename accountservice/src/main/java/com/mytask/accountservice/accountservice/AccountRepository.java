package com.mytask.accountservice.accountservice;

import java.util.Optional;

/**
 * Repository for {@link Account} objects
 */
public interface AccountRepository {

    /**
     * Get Account by id
     *
     * @param id id
     * @return Optional of Account where it was found or not
     */
    Optional<Account> getById(Integer id);

    /**
     * Save of update account to repository
     * Use only returned object after method call
     *
     * @param account account object
     * @return saved object
     */
    Account save(Account account);

}
