package net.snails.common.algorithm.keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.snails.common.algorithm.util.StopWord;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.google.common.base.Strings;

/**
 * TF-IDF关键字提取
 * 
 * @author krisjin
 * 
 */
public class TFIDFExtractKeyword implements ExtractWord {

	/**
	 * 语料文档总数
	 */
	private int DOC_NUM;

	/**
	 * 文档中划分的句子的词频统计
	 */
	private Map<String, Integer>[] FRAGMENT_TF;

	/**
	 * 词频
	 */
	private Map<String, Integer> TF;

	/**
	 * 逆文档词频
	 */
	private Map<String, Double> IDF;

	/**
	 * 将文档 进行片段式的划分
	 * 
	 * @param doc
	 * @return
	 */
	private List<String> getFragmentList(String doc) {
		List<String> sentences = new ArrayList<String>();

		if (doc == null)
			return sentences;

		String[] fragArr = doc.split("[，,。:：“”？?！!；;]");

		for (String sen : fragArr) {
			sen = sen.trim();
			if (Strings.isNullOrEmpty(sen))
				continue;
			sentences.add(sen);
		}

		return sentences;
	}

	private void process(List<List<String>> wordFragment) {
		FRAGMENT_TF = new Map[wordFragment.size()];
		TF = new TreeMap<String, Integer>();
		IDF = new TreeMap<String, Double>();
		int index = 0;

		for (List<String> frag : wordFragment) {
			Map<String, Integer> tmpTF = new TreeMap<String, Integer>();
			for (String word : frag) {
				Integer freq = tmpTF.get(word);
				freq = (freq == null ? 0 : freq) + 1;
				tmpTF.put(word, freq);
			}
			FRAGMENT_TF[index] = tmpTF;

			for (Map.Entry<String, Integer> entry : tmpTF.entrySet()) {
				String word = entry.getKey();
				Integer freq = TF.get(word);

				freq = (freq == null ? 0 : freq) + 1;
				TF.put(word, freq);

			}
			++index;
		}

		for (Map.Entry<String, Integer> entry : TF.entrySet()) {
			String word = entry.getKey();

			IDF.put(word, Math.log(DOC_NUM / (getIncludeWordDocs(FRAGMENT_TF, word) + 1)));

		}

	}

	private int getIncludeWordDocs(Map<String, Integer>[] fragmentDocs, String word) {
		AtomicInteger ai = new AtomicInteger();
		for (Map<String, Integer> frag : fragmentDocs) {

			if (frag.containsKey(word)) {
				ai.incrementAndGet();
			}
		}
		return ai.get();
	}

	private List<Map.Entry<String, Double>> getKeywordList() {

		Map<String, Double> result = new TreeMap<String, Double>();

		for (Map.Entry<String, Integer> entry : TF.entrySet()) {

			String word = entry.getKey();
			Integer freq = entry.getValue();

			if (IDF.containsKey(word)) {
				double val = ((double) freq / (double) TF.size()) * (IDF.get(word));
				result.put(word, val);
			} else {
				result.put(word, ((double) freq / TF.size()));
			}

		}

		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(result.entrySet());

		Collections.sort(list, new MapValueComparator());
		return list;
	}

	private List<List<String>> getParticipleFragment(List<String> sentences) {

		List<List<String>> wordFragment = new ArrayList<List<String>>();

		for (String sens : sentences) {
			List<String> wordList = new LinkedList<String>();
			List<Term> termList = ToAnalysis.parse(sens);

			for (Term term : termList) {
				if (validateWord(term)) {
					if (!(term.item().name == null)) {
						wordList.add(term.getName());
					}
				}
			}

			if (wordList.size() > 0)
				wordFragment.add(wordList);
		}

		DOC_NUM = wordFragment.size();

		return wordFragment;
	}

	private List<String> extractKeyword(String doc, int size) {

		List<String> result = new ArrayList<String>();

		List<List<String>> fragList = getParticipleFragment(getFragmentList(doc));

		process(fragList);

		List<Map.Entry<String, Double>> keywords = getKeywordList();

		if (keywords.size() <= size) {
			for (Map.Entry<String, Double> entry : keywords) {
				result.add(entry.getKey());
			}
		} else {
			int i = 1;
			for (Map.Entry<String, Double> entry : keywords) {
				result.add(entry.getKey());
				if (i == size)
					break;
				i++;
			}
		}

		return result;
	}

	private  boolean validateWord(Term term) {
		if (term.getNatureStr().startsWith("n") || term.getNatureStr().startsWith("v") || term.getNatureStr().startsWith("d")
				|| term.getNatureStr().startsWith("a")) {
			if (!StopWord.contains(term.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> extract(String docs, int limitSize) {

		return extractKeyword(docs, limitSize);
	}

}

class MapValueComparator implements Comparator<Map.Entry<String, Double>> {

	public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
		return o2.getValue().compareTo(o1.getValue());
	}

}