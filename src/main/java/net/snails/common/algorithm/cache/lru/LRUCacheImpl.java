package net.snails.common.algorithm.cache.lru;

import java.util.HashMap;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/5
 * Time: 19:28
 */
public class LRUCacheImpl<K, V> {

    private final int MAX_CACHE_SIZE;

    private Entry first;
    private Entry last;

    private HashMap<K, Entry<K, V>> hashMap;

    public LRUCacheImpl(int maxCacheSize) {
        MAX_CACHE_SIZE = maxCacheSize;
        hashMap = new HashMap<K, Entry<K, V>>();
    }


    public void put(K key, V value) {
        Entry entry = getEntry(key);

        if (entry == null) {
            if (hashMap.size() > MAX_CACHE_SIZE) {
                hashMap.remove(last.key);
                removeLast();
            }
        }
        entry = new Entry();
        entry.key = key;
        entry.value = value;

        moveToFirst(entry);
        hashMap.put(key, entry);
    }


    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) return null;
        moveToFirst(entry);
        return entry.value;
    }

    private Entry<K, V> getEntry(K key) {
        return hashMap.get(key);
    }

    public void remove(K key) {
        Entry entry = getEntry(key);
        if (entry != null) {
            if (entry.pre != null) entry.pre.next = entry.next;
            if (entry.next != null) entry.next.pre = entry.pre;
            if (entry == first) first = entry.next;
            if (entry == last) last = entry.pre;
        }
        hashMap.remove(key);
    }
    private void removeLast() {
        if (last != null) {
            last = last.pre;
            if (last == null) {
                first = null;
            } else {
                last.next = null;
            }
        }
    }

    private void moveToFirst(Entry entry) {
        if (entry == first)
            return;
        if (entry.pre != null)
            entry.pre.next = entry.next;

        if (entry.next != null)
            entry.next.pre = entry.pre;

        if (entry == last)
            last = last.pre;

        if (first == null || last == null) {
            first = last = entry;
            return;
        }

        entry.next = first;
        first.pre = entry;
        first = entry;
        entry.pre = null;
    }

    class Entry<K, V> {

        public Entry pre;
        public Entry next;
        public K key;
        public V value;


    }
}
