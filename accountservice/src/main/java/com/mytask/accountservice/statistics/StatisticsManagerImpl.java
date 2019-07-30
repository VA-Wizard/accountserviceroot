package com.mytask.accountservice.statistics;

import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StatisticsManagerImpl implements StatisticsManager {

    private final Clock clock;

    public StatisticsManagerImpl() {
        this.clock = Clock.systemDefaultZone();
    }

    public StatisticsManagerImpl(Clock clock) {
        this.clock = clock;
    }

    private final ConcurrentHashMap<String, StatisticsDetails> statisticsMap = new ConcurrentHashMap<>();

    @Override
    public void registerInvoke(String methodName) {
        statisticsMap.compute(methodName, (key, details) -> {
            if (details == null) {
                details = new StatisticsDetails(clock.millis(), new AtomicInteger(1));
            } else {
                details.getInvocationCount().incrementAndGet();
            }
            return details;
        });
    }

    @Override
    public void resetStatistics() {
        statisticsMap.clear();
    }

    @Override
    public double getInvocationRate(String methodName) {
        StatisticsDetails statisticsDetails = statisticsMap.get(methodName);
        if (statisticsDetails == null) {
            return 0;
        }
        long now = clock.millis();
        return (double) statisticsDetails.getInvocationCount().get() * 1000 /
                ((now - statisticsDetails.getInvocationStartTime()));
    }

    @Override
    public int getInvocationCount(String methodName) {
        StatisticsDetails statisticsDetails = statisticsMap.get(methodName);
        if (statisticsDetails == null) {
            return 0;
        }
        return statisticsDetails.getInvocationCount().get();
    }

    @Value
    private class StatisticsDetails {
        long invocationStartTime;
        AtomicInteger invocationCount;  //  AtomicInteger for mutability
    }
}
