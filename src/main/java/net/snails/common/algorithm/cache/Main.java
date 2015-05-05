package net.snails.common.algorithm.cache;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 13:07
 */
public class Main {

    public static void main(String[] args) {

        RandomCacheImpl radomCache = new RandomCacheImpl();


        for (int i = 1; i <= 1001; i++) {
            CacheElement cacheElement = new CacheElement();
            cacheElement.setKey(i);
            cacheElement.setValue("name1");
            radomCache.put(i, cacheElement);
        }

        CacheElement cacheElement =radomCache.get(100);

        System.out.println("key:"+cacheElement.getKey()+" value:"+cacheElement.getValue());

        radomCache.put(100,"name100x");

        CacheElement cacheElement2 =radomCache.get(100);

        System.out.println("key:"+cacheElement2.getKey()+" value:"+cacheElement2.getValue());

        System.out.println(radomCache.cacheSize.get());


    }
}
