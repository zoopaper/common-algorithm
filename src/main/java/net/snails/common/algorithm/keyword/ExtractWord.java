package net.snails.common.algorithm.keyword;

import java.util.List;

/**
 * @author krisjin
 * @date 2015-3-9
 */
public interface ExtractWord {

	public List<String> extract(String docs, int limitSize);

}
