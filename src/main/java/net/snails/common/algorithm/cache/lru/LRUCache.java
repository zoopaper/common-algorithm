package net.snails.common.algorithm.cache.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 17:12
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int MAX_CACHE_SIZE;

    public LRUCache(int maxCacheSize) {
        super((int) Math.ceil(maxCacheSize / 0.75) + 1, 0.75f, true);
        MAX_CACHE_SIZE = maxCacheSize;
    }


    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {

        return size() > MAX_CACHE_SIZE;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<K, V> entry : entrySet())
            sb.append(String.format("%s:%s", entry.getKey(), entry.getValue()));

        return sb.toString();
    }


}
