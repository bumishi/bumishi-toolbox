package org.bumishi.toolbox.concurrent;

/**
 * @author qiang.xie
 * @date 2016/10/28
 */

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 并发调用中保证某个指定的task的调用只产生一次。
 * 比如：根据同一个繁星id，从soa获取酷狗id的任务，并发执行时只会真正调用soa一次
 * 不同类型的任务最好创建不同的此类实例，以免造成key冲突
 *
 * @author qiang.xie
 * @date 2016/10/27
 */
public class ConcurrentCallOnlyOnceTask<Key, Value> {
    protected Logger logger = LoggerFactory.getLogger(ConcurrentCallOnlyOnceTask.class);
    //key的任务条目
    private ConcurrentHashMap<Key, FutureTask<Value>> map = new ConcurrentHashMap<>(100);
    //用它仅仅解决map中的key过期问题，map只是处理并发访问，其中的key只需很短的存活时间就可以了
    private Cache<Key, Integer> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).removalListener(new RemovalListener<Key, Integer>() {
        @Override
        public void onRemoval(RemovalNotification<Key, Integer> notification) {
            map.remove(notification.getKey());
        }
    }).build();
    private ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);

    public ConcurrentCallOnlyOnceTask() {
        init();
    }

    public Value callTask(Key key, Callable<Value> task) {
        FutureTask<Value> futureTask = new FutureTask<>(task);
        //如果map中没有此key则放入，并返回原来的值
        FutureTask<Value> existTask = map.putIfAbsent(key, futureTask);
        if (existTask == null) {
            existTask = futureTask;
            cache.put(key, 1);//将key放入cache，让cache管理key在map中的过期，防止map中放置过多无用的key
            futureTask.run();
        }
        try {
            return existTask.get();
        } catch (Exception e) {
            logger.error("callTask error", e);
            map.remove(key);
            cache.invalidate(key);
            return null;
        }
    }

    //清理过期数据
    private void init() {
        pool.scheduleWithFixedDelay(() -> cache.cleanUp(), 120, 20, TimeUnit.SECONDS);
    }

    public void destroy() {
        cache.invalidateAll();
        map.clear();
        pool.shutdownNow();
    }
}