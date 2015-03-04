package net.snails.common.algorithm.consistenthash;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author krisjin
 * @date 2015年1月21日
 */
public class Test {
	public static void main(String[] args) {
		// 定义几个服务器的名称，存放到集合中
		Collection<String> nodes = new HashSet<String>();
		nodes.add("192.0.0.1");
		nodes.add("192.0.0.2");
		nodes.add("192.0.0.3");
		nodes.add("192.0.0.4");
		nodes.add("192.0.0.5");
		nodes.add("192.0.0.6");
		// MD5压缩算法实现的hash函数
		MD5Hash hashFunction = new MD5Hash();
		ConsistentHash<String> cHash = new ConsistentHash<String>(hashFunction, 4, nodes);
		// 对象的key值为"google_baidu"
		String key[] = { "google", "163", "baidu", "sina" };
		// 利用一致性哈希，得到该对象应该存放的服务器
		String server[] = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			server[i] = cHash.get(key[i]);
			System.out.println("对象 " + key[i] + " 存放于服务器： " + server[i]);
		}
	}
}
