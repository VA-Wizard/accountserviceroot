package com.mytask.accountservice.lockservice;

/**
 * Lock service locks by specified keys
 * <p>
 * usage:
 * {@code lockService.lock("key");
 * try{
 * ...
 * }finally{
 * lockService.unlock("key");
 * }}
 */
public interface LockService {

    /**
     * Lock by key
     *
     * @param key key
     */
    void lock(String key);

    /**
     * Unlock by key. Do nothing on unlocking without {@link #lock(String)} call
     *
     * @param key key
     */
    void unlock(String key);
}
