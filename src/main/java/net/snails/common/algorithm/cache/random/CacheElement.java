package net.snails.common.algorithm.cache.random;

/**
 * 缓存实体
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 10:33
 */
public class CacheElement {

    private Object key;

    private Object value;

    private int index;

    private int hitCount;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }
}
