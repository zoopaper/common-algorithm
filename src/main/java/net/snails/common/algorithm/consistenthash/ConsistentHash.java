package net.snails.common.algorithm.consistenthash;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 利用一致性hash，计算得到对象要存放的服务器
 *
 * @author krisjin
 * @date 2015年1月21日
 */
public class ConsistentHash<T> {

	private final MD5Hash md5Hash;

	// 虚拟节点个数
	private final int numberOfReplicas;

	private final SortedMap<String, T> circle = new TreeMap<String, T>();

	public ConsistentHash(MD5Hash md5Hash, int numberOfReplicas, Collection<T> nodes) {
		this.md5Hash = md5Hash;
		this.numberOfReplicas = numberOfReplicas;
		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * 添加服务器节点
	 * 
	 * @param node
	 */
	public void add(T node) {
		String key;
		// 虚拟节点所在的hash处，存放对应的实际的节点服务器
		for (int i = 0; i < numberOfReplicas; i++) {
			key = node.toString() + i;
			circle.put(md5Hash.hash(key), node);
		}
	}

	/**
	 * 移除服务器节点
	 * 
	 * @param node
	 */
	public void remove(T node) {
		String key;
		for (int i = 0; i < numberOfReplicas; i++) {
			key = node.toString() + i;
			circle.remove(md5Hash.hash(key));
		}
	}

	/**
	 * 根据对象的key值，映射到hash表中，得到与对象hash值最近的服务器，就是对象待存放的服务器
	 * 
	 * @param key
	 * @return
	 */
	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		// 得到对象的hash值，根据该hash值找hash值最接近的服务器
		String hash = md5Hash.hash((String) key);
		// 以下为核心部分，寻找与上面hash最近的hash指向的服务器
		// 如果hash表circle中没有该hash值
		if (!circle.containsKey(hash)) {
			// tailMap为大于该hash值的circle的部分
			SortedMap<String, T> tailMap = circle.tailMap(hash);
			// tailMap.isEmpty()表示没有大于该hash的hash值
			// 如果没有大于该hash的hash值，那么从circle头开始找第一个；如果有大于该hash值得hash，那么就是第一个大于该hash值的hash为服务器
			// 既逻辑上构成一个环，如果达到最后，则从头开始
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

}