package com.mytask.accountservice.controller;

import com.mytask.accountservice.accountservice.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;

/**
 * Controller for Account operations
 */
@Slf4j
@RestController
@RequestMapping(value = "account")
@Validated
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "getAmount")
    public Long getAmount(@RequestParam @Min(1) Integer id) {
        log.info("getAmount.enter; id={}", id);
        Long amount = accountService.getAmount(id);
        log.info("getAmount.exit; amount={}", amount);
        return amount;
    }

    @PostMapping(value = "addAmount")
    public void addAmount(@RequestParam @Min(1) int id, @RequestParam long addValue) {
        log.info("addAmount.enter; id={}; addValue={}", id, addValue);
        accountService.addAmount(id, addValue);
        log.info("addAmount.exit;");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)   //  add client specific exceptions here
    public String clientError(HttpServletRequest req, Exception ex) {
        log.error("clientError; request: {}; exception: {}", req.getRequestURL(), ex);
        return "constraint violation; " + ex.getMessage();
    }

    //  Handle all unhandled exceptions to hide internal details
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception ex) {
        log.error("handleError; request: {}; exception: {}", req.getRequestURL(), ex);
        return "internal server error";
    }
}
