package net.snails.common.algorithm.test;

import net.snails.common.algorithm.consistenthash.ConsistentHash;
import net.snails.common.algorithm.consistenthash.MD5Hash;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TestConsistenHash {

    @Test
    public void test() {
        // 定义几个服务器的名称，存放到集合中
        Collection<String> nodes = new HashSet<String>();
        nodes.add("192.0.0.1");
        nodes.add("192.0.0.2");
        nodes.add("192.0.0.3");
        nodes.add("192.0.0.4");
        nodes.add("192.0.0.5");
        nodes.add("192.0.0.6");

        MD5Hash hashFunc = new MD5Hash();
        ConsistentHash<String> cHash = new ConsistentHash<String>(hashFunc, 4, nodes);

        Map<String, Object> objectMap = new HashMap<String, Object>();
        for (int i = 0; i < 1000; i++) {
            objectMap.put("title" + i, "NEWS_OBJECT");
        }
        for (Map.Entry entry : objectMap.entrySet()) {
            String server = cHash.get(entry.getKey());
            System.out.println("对象:" + entry.getKey() + ":存于服务器:" + server);
        }
    }
}
