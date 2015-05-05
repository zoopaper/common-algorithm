package net.snails.common.algorithm.cache;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 10:51
 */
public class RandomCacheImpl implements RandomCache {

    private ConcurrentHashMap<Object, Object> cache = new ConcurrentHashMap<Object, Object>();

    private final int maxCapacity = 1000;

    public AtomicInteger cacheSize = new AtomicInteger();

    @Override
    public void put(Object key, Object value) {

        if (key == null) {
            throw new NullPointerException("key is null");
        }

        Object obj = cache.get(key);

        if (obj != null) {
            CacheElement cacheElement = (CacheElement) obj;
            cacheElement.setKey(key);
            cacheElement.setValue(value);
            return;
        }

        if (!isFull()) {
            cacheSize.getAndIncrement();
        } else {
            Random random = new Random();
            int index = (int) (cacheSize.get() * random.nextFloat());

            System.out.println("生成随机索引：" + index);
            cache.remove(index);
        }
        cache.put(key, value);
    }

    @Override
    public CacheElement get(Object key) {
        return (CacheElement)cache.get(key);
    }

    private boolean isFull() {
        if (cache.size() >= maxCapacity) {
            return true;
        }
        return false;
    }

}
