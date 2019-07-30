package com.mytask.accountservice.statistics.aop;

import com.mytask.accountservice.statistics.StatisticsManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestAuditComponent.class)
public class AuditableAspectTest {

    @Autowired
    private TestAuditComponent testAuditComponent;

    @MockBean
    private StatisticsManager statisticsManager;

    @Test
    public void checkSimpleLock() {
        testAuditComponent.auditableMethod();
        verify(statisticsManager, times(1)).registerInvoke("auditableMethod");
    }
}