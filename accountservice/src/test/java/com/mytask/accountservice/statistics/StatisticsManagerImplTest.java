package com.mytask.accountservice.statistics;

import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsManagerImplTest {

    @Test
    public void registerInvoke() {
        StatisticsManagerImpl statisticsManager = new StatisticsManagerImpl();
        statisticsManager.registerInvoke("abc");
        statisticsManager.registerInvoke("def");
        Assert.assertEquals(1, statisticsManager.getInvocationCount("abc"));
        Assert.assertEquals(1, statisticsManager.getInvocationCount("def"));
    }

    @Test
    public void resetStatistics() {
        StatisticsManagerImpl statisticsManager = new StatisticsManagerImpl();
        statisticsManager.registerInvoke("abc");
        statisticsManager.resetStatistics();
        Assert.assertEquals(0, statisticsManager.getInvocationCount("abc"));
    }

    @Test
    public void getInvocationRate() {
        Clock clock = mock(Clock.class);
        when(clock.millis()).thenReturn(0L);
        StatisticsManagerImpl statisticsManager = new StatisticsManagerImpl(clock);
        statisticsManager.registerInvoke("abc");
        statisticsManager.registerInvoke("abc");
        statisticsManager.registerInvoke("abc");
        when(clock.millis()).thenReturn(2000L);
        Assert.assertEquals(3d/2, statisticsManager.getInvocationRate("abc"), 0.00001);
    }

}