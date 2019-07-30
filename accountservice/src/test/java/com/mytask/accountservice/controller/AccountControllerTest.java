package com.mytask.accountservice.controller;

import com.mytask.accountservice.accountservice.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void getAmount() throws Exception {
        when(accountService.getAmount(1)).thenReturn(5L);
        mockMvc.perform(get("/account/getAmount?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("5"));
    }

    @Test
    public void addAmount() throws Exception {

        mockMvc.perform(post("/account/addAmount")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("id=1&addValue=5"))
                .andDo(print()).andExpect(status().isOk());

        verify(accountService, times(1)).addAmount(1, 5L);
    }

    @Test
    public void handleClientError() throws Exception {
        mockMvc.perform(post("/account/addAmount")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("id=-1&addValue=5"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void handleServerError() throws Exception {
        when(accountService.getAmount(100)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/account/getAmount?id=100"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}