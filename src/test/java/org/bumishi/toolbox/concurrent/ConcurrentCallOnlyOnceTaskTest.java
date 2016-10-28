package org.bumishi.toolbox.concurrent;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiang.xie
 * @date 2016/10/27
 */
public class ConcurrentCallOnlyOnceTaskTest {

    @Test
    public void callTask() throws InterruptedException {
        AtomicInteger callCount = new AtomicInteger(0);//调用次数计数器
        ConcurrentCallOnlyOnceTask<String, String> onlyOnceTask = new ConcurrentCallOnlyOnceTask();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        //模拟10个线程并发调用目标方法
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                onlyOnceTask.callTask("a", () -> {
                    callCount.incrementAndGet();
                    Thread.sleep(2000);
                    return "a";
                });
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        Assert.assertEquals("a", "a");
        //期望只调用一次
        Assert.assertEquals(1, callCount.get());
        onlyOnceTask.destroy();
    }
}