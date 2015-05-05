package net.snails.common.algorithm.cache.lru;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 17:47
 */
public class LRUCacheDelegation<K, V> {


    private final int MAX_CACHE_SIZE;

    private final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected Map<K, V> map;

    public LRUCacheDelegation(int maxCacheSize) {
        MAX_CACHE_SIZE = maxCacheSize;
        int capacity = (int) Math.ceil(maxCacheSize / DEFAULT_LOAD_FACTOR) + 1;
        map = Collections.synchronizedMap(new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        });
//        map = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTOR, true) {
//            @Override
//            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
//                return size() > MAX_CACHE_SIZE;
//            }
//        };

    }

    public void put(K key, V value) {
        Preconditions.checkArgument(key != null, "key not allow null");
        map.put(key, value);
    }

    public V get(K key) {
        Preconditions.checkArgument(key != null, "key not allow null");
        return map.get(key);
    }


    public int size() {
        return map.size();
    }

    public void remove(K key) {
        Preconditions.checkArgument(key != null, "key not allow null");
        map.remove(key);
    }
}
