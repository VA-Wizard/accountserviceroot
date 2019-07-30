package com.mytask.accountservice.lockservice;

import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple implementation of {@link LockService} using {@link ConcurrentHashMap}
 */
@Service
public class LockServiceImpl implements LockService {

    private final ConcurrentHashMap<String, LockWithCounter> lockMap = new ConcurrentHashMap<>();

    @Override
    public void lock(String key) {
        lockMap.compute(key, (k, lockCounter) -> {
            if (lockCounter == null) {
                lockCounter = new LockWithCounter(new ReentrantLock(), new AtomicInteger(1));
            } else {
                lockCounter.getCounter().incrementAndGet();
            }
            return lockCounter;
        }).getLock().lock();
    }

    @Override
    public void unlock(String key) {
        lockMap.compute(key, (k, lockCounter) -> {
            if (lockCounter == null) {
                return null;
            }
            lockCounter.getLock().unlock();
            int count = lockCounter.getCounter().decrementAndGet();
            if (count == 0) {
                return null;
            }
            return lockCounter;
        });
    }

    @Value
    private static class LockWithCounter {
        Lock lock;
        AtomicInteger counter;
    }
}
