package com.mytask.accountservice.lockservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockServiceImplTest {

    @Autowired
    private LockService lockService;

    @Test(timeout = 5000)
    public void lockConcurrentTest() throws Exception {
        Counter counter = new Counter();
        CyclicBarrier barrier = new CyclicBarrier(50);
        List<Future> futures = new ArrayList<>();
        ExecutorService service = Executors.newCachedThreadPool();
        for(int i = 0; i < 50; i++){
            futures.add(service.submit(() -> {
                try {
                    barrier.await();
                    lockService.lock("abc");
                    try {
                        counter.increment();
                        counter.decrement();
                    } finally {
                        lockService.unlock("abc");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        service.shutdown();
        for (Future future : futures) {
            future.get();
        }
    }

    @Test
    public void unlockWithoutLock() throws Exception {
        lockService.unlock("abc");
    }

    private class Counter{
        private volatile int i;
        public void increment(){
            i++;
        }
        public void decrement(){
            if(i <= 0){
                throw new AssertionError("Counter is negative");
            }
            i--;
        }
    }
}