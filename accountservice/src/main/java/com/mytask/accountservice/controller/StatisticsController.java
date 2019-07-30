package com.mytask.accountservice.controller;

import com.mytask.accountservice.statistics.StatisticsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for statistics operations
 */
@Slf4j
@RestController
@RequestMapping(value = "statistics")
public class StatisticsController {

    private final StatisticsManager statisticsManager;

    @Autowired
    public StatisticsController(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    @PostMapping(value = "resetStatistics")
    public void resetStatistics() {
        log.info("resetStatistics.enter");
        statisticsManager.resetStatistics();
        log.info("resetStatistics.exit");
    }

    @GetMapping(value = "getInvocationRate")
    public double getInvocationRate(@RequestParam String methodName) {
        log.info("getInvocationRate.enter; methodName={}", methodName);
        double rate = statisticsManager.getInvocationRate(methodName);
        log.info("getInvocationRate.exit; rate={}", rate);
        return rate;
    }

    @GetMapping(value = "getInvocationCount")
    public int getInvocationCount(@RequestParam String methodName) {
        log.info("getInvocationCount.enter; methodName={}", methodName);
        int count = statisticsManager.getInvocationCount(methodName);
        log.info("getInvocationCount.exit; count={}", count);
        return count;
    }
}
