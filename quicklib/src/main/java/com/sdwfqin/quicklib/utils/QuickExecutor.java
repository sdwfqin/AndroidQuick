package com.sdwfqin.quicklib.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 描述：线程池
 * <p>
 * 只有一个核心线程，确保所有任务都在同一线程中按顺序完成。因此不需要处理线程同步的问题。
 *
 * @author zhangqin
 * @date 2018/6/2
 */
public class QuickExecutor {

    private ExecutorService mSingleThreadPool;

    public static QuickExecutor getInstance() {
        return CbtExecutorHolder.CBT_EXECUTOR;
    }

    private static class CbtExecutorHolder {
        private static final QuickExecutor CBT_EXECUTOR = new QuickExecutor();
    }

    /**
     * corePoolSize：核心线程数。
     * maximumPoolSize：“线程池允许创建的最大线程数。
     * keepAliveTime：非核心线程闲置的超时时间。超过这个时间则回收。
     * TimeUnit：keepAliveTime参数的时间单位。
     * workQueue：任务队列。
     * ThreadFactory：线程工厂。
     * RejectedExecutionHandler：饱和策略。
     */
    private QuickExecutor() {
        mSingleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                r -> {
                    Thread thread = new Thread(r);
                    // thread.setName("sdwfqin");
                    return thread;
                }, new ThreadPoolExecutor.AbortPolicy());
    }

    public void execute(Runnable runnable) {
        mSingleThreadPool.execute(runnable);
    }

    public void shutdown() {
        mSingleThreadPool.shutdown();
    }

}
