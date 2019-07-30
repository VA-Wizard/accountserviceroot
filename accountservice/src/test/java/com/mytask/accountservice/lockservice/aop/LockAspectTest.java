package com.mytask.accountservice.lockservice.aop;

import com.mytask.accountservice.lockservice.LockService;
import com.mytask.accountservice.lockservice.aop.components.TestLockComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.IncompleteAnnotationException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestLockComponent.class)
public class LockAspectTest {

    @MockBean
    private LockService lockService;

    @Autowired
    private TestLockComponent testLockComponent;

    @Test(expected = IncompleteAnnotationException.class)
    public void wrongAnnotatedMethod() {
        testLockComponent.wrongAnnotatedMethod();
    }

    @Test
    public void checkSimpleLock() {
        testLockComponent.lockByFirstParameter("key", "param");
        verify(lockService, times(1)).lock("key");
        verify(lockService, times(1)).unlock("key");
    }

    @Test
    public void checkReentrantLock() {
        testLockComponent.lockByFirstParameter("key", "param");
        testLockComponent.lockBySecondParameter("param", "key");
        verify(lockService, times(2)).lock("key");
        verify(lockService, times(2)).unlock("key");
    }

}