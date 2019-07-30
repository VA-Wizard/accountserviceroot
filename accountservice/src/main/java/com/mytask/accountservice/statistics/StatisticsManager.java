package com.mytask.accountservice.statistics;

/**
 * Gather statistics of method invocation
 */
public interface StatisticsManager {

    /**
     * Register method invoke
     *
     * @param methodName name of method invocation
     */
    void registerInvoke(String methodName);

    /**
     * Reset statistics (all invocations will set to 0)
     */
    void resetStatistics();

    /**
     * Get method invocation rate per second
     *
     * @param methodName method name
     * @return invocation rate per second
     */
    double getInvocationRate(String methodName);

    /**
     * Get overall invocation count of specified method
     *
     * @param methodName method name
     * @return invocation count
     */
    int getInvocationCount(String methodName);
}
