package com.mytask.accountservice.controller;

import com.mytask.accountservice.statistics.StatisticsManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsManager statisticsManager;

    @Test
    public void getInvocationCount() throws Exception {
        when(statisticsManager.getInvocationCount("method")).thenReturn(5);
        mockMvc.perform(get("/statistics/getInvocationCount?methodName=method"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("5"));
    }

    @Test
    public void getInvocationRate() throws Exception {
        when(statisticsManager.getInvocationRate("method")).thenReturn(5.5);
        mockMvc.perform(get("/statistics/getInvocationRate?methodName=method"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("5.5"));
    }

    @Test
    public void resetStatistics() throws Exception {

        mockMvc.perform(post("/statistics/resetStatistics"))
                .andDo(print()).andExpect(status().isOk());

        verify(statisticsManager, times(1)).resetStatistics();
    }


}