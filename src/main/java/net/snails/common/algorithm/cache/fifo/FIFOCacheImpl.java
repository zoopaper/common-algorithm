package net.snails.common.algorithm.cache.fifo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.snails.common.algorithm.cache.random.CacheElement;

import java.util.LinkedHashMap;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 15:01
 */
public class FIFOCacheImpl implements FIFOCache {

    private final LinkedHashMap<Object, Object> cache = Maps.newLinkedHashMap();

    private final int MAX_CAPACITY = 100;


    @Override
    public void put(Object key, Object value) {

        Preconditions.checkArgument(key != null, "key not allow null");

        CacheElement cacleElement = (CacheElement) cache.get(key);

        if (cacleElement != null) {

            CacheElement cacheElement = (CacheElement) cacleElement;
            cacheElement.setKey(key);
            cacheElement.setValue(value);
            updateKey(key, cacheElement);
            return;
        }

        if (isFull()) {

            Object firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }

        cache.put(key, value);

    }

    @Override
    public CacheElement get(Object key) {
        Preconditions.checkArgument(key != null, "key not allow null");
        return (CacheElement) cache.get(key);
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }


    private void updateKey(Object key, Object obj) {

        Preconditions.checkArgument(key != null, "key not allow null");
        cache.put(key, obj);

    }

    private boolean isFull() {

        if (cache.size() >= MAX_CAPACITY) {
            return true;
        }

        return false;
    }
}
