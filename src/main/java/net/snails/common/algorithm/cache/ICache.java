package net.snails.common.algorithm.cache;

import net.snails.common.algorithm.cache.random.CacheElement;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 10:48
 */
public interface ICache {


    public void put(Object key, Object value);

    public CacheElement get(Object key);


}
