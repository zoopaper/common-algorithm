package net.snails.common.algorithm.cache.fifo;

import net.snails.common.algorithm.cache.random.CacheElement;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 16:11
 */
public class Main {

    public static void main(String[] args) {

        FIFOCacheImpl fifoCache = new FIFOCacheImpl();


        for (int i = 1; i <= 100; i++) {
            CacheElement cacheElement = new CacheElement();
            cacheElement.setKey(i);
            cacheElement.setValue("v" + i);
            fifoCache.put(i, cacheElement);

        }
        CacheElement cacheElement = new CacheElement();
        cacheElement.setKey(9);
        cacheElement.setValue("xxxxxxxxxxxxxxxxxxxxxxx");
        fifoCache.put(9, cacheElement);

        CacheElement cacheElement2 = new CacheElement();
        cacheElement2.setKey(101);
        cacheElement2.setValue("v101");
        fifoCache.put(101, cacheElement2);

        System.out.println(fifoCache.size());

    }
}
