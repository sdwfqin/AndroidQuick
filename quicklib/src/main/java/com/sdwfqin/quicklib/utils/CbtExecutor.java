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
public class CbtExecutor {

    private ExecutorService mSingleThreadPool;

    public static CbtExecutor getInstance() {
        return CbtExecutorHolder.CBT_EXECUTOR;
    }

    private static class CbtExecutorHolder {
        private static final CbtExecutor CBT_EXECUTOR = new CbtExecutor();
    }

    private CbtExecutor() {
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
