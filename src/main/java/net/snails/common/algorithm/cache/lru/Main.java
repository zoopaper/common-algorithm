package net.snails.common.algorithm.cache.lru;

import net.snails.common.algorithm.cache.random.CacheElement;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 17:29
 */
public class Main {

    public static void main(String[] args) {

        LRUCache<Object, CacheElement> lruCache = new LRUCache<Object, CacheElement>(10000);

        LRUCacheDelegation<Object, CacheElement> cacheDelegation = new LRUCacheDelegation<Object, CacheElement>(10000);


//        for (int i = 0; i < 20000; i++) {
//
//            CacheElement ce = new CacheElement();
//            ce.setKey(i);
//            ce.setValue("V" + i);
//
//            lruCache.put(i, ce);
//
//        }

        for (int i = 0; i < 20000; i++) {

            CacheElement ce = new CacheElement();
            ce.setKey(i);
            ce.setValue("V" + i);

            cacheDelegation.put(i, ce);

        }
        CacheElement cacheElement = cacheDelegation.get(10009);
        System.out.println(String.format("%s:%s", cacheElement.getKey(), cacheElement.getValue()));
    }
}
