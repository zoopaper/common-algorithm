package net.snails.common.algorithm.cache.fifo;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;
import net.snails.common.algorithm.cache.random.CacheElement;

import java.util.List;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 15:01
 */
public class FIFOCacheImpl implements FIFOCache {

    private final LinkedListMultimap<Object, CacheElement> cache = LinkedListMultimap.create();

    private final int MAX_CAPACITY = 100;


    @Override
    public void put(Object key, Object value) {

        Preconditions.checkArgument(key != null, "key not allow null");

        List<CacheElement> list = (List<CacheElement>) cache.get(key);

        if (list.size() > 0) {

            CacheElement cacheElement = (CacheElement) list.get(0);
            cacheElement.setKey(key);
            cacheElement.setValue(value);
            updateKey(key, cacheElement);
            return;
        }

        if (isFull()) {
            Object firstKey = cache.entries().get(0).getKey();
            Object firstObj = cache.entries().get(0).getValue();
            cache.remove(firstKey, firstObj);
        }

        cache.put(key, (CacheElement) value);

    }

    @Override
    public CacheElement get(Object key) {
        Preconditions.checkArgument(key != null, "key not allow null");
        return (CacheElement) cache.get(key).get(0);
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }


    private void updateKey(Object key, Object obj) {

        cache.put(key, (CacheElement) obj);
    }

    private boolean isFull() {

        if (cache.size() >= MAX_CAPACITY) {
            return true;
        }

        return false;
    }
}
