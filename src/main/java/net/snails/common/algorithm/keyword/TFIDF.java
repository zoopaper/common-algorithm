package net.snails.common.algorithm.keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.snails.common.algorithm.summary.StopWord;
import net.snails.common.algorithm.util.CorpusLoad;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.google.common.base.Strings;

/**
 * TF-IDF关键词提取
 * @author kris
 *
 */
public class TFIDF {

	private int docNum;

	/**
	 * 文档中划分的句子的词频统计
	 */
	private Map<String, Integer>[] stf;

	private Map<String, Integer> atf;

	private Map<String, Double> idf;

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

		String[] senArr = doc.split("[，,。:：“”？?！!；;]");

		for (String sen : senArr) {
			sen = sen.trim();
			if (Strings.isNullOrEmpty(sen))
				continue;
			sentences.add(sen);
		}

		return sentences;
	}

	private void processTFIDF(List<List<String>> wordFragment) {
		stf = new Map[wordFragment.size()];
		atf = new TreeMap<String, Integer>();
		idf = new TreeMap<String, Double>();
		int index = 0;
		for (List<String> frag : wordFragment) {
			Map<String, Integer> tf = new TreeMap<String, Integer>();
			for (String word : frag) {
				Integer freq = tf.get(word);
				freq = (freq == null ? 0 : freq) + 1;
				tf.put(word, freq);
			}
			stf[index] = tf;

			for (Map.Entry<String, Integer> entry : tf.entrySet()) {
				String word = entry.getKey();
				Integer freq = atf.get(word);

				freq = (freq == null ? 0 : freq) + 1;
				atf.put(word, freq);

			}
			++index;
		}

		for (Map.Entry<String, Integer> entry : atf.entrySet()) {
			String word = entry.getKey();
			Integer freq = entry.getValue();

			idf.put(word, Math.log(docNum - freq + 0.5) - Math.log(freq + 0.5));

		}

	}

	private Map<String, Double> getKeywordList() {

		Map<String, Double> result = new TreeMap<String, Double>();

		for (Map.Entry<String, Integer> entry : atf.entrySet()) {

			String word = entry.getKey();
			Integer freq = entry.getValue();

			if (idf.containsKey(word)) {
				double val = ((double) freq / (double) atf.size()) * (idf.get(word));
				result.put(word, val);
			} else {
				result.put(word, ((double) freq / atf.size()));
			}

		}

		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(result.entrySet());

		Collections.sort(list, new MapValueComparator());

		return result;
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

		docNum = wordFragment.size();

		return wordFragment;
	}

	public List<String> extractKeyword(String doc, int size) {

		List<List<String>> fragList = getParticipleFragment(getFragmentList(doc));

		processTFIDF(fragList);

		Map<String, Double> list = getKeywordList();

		return null;
	}

	public static boolean validateWord(Term term) {
		if (term.getNatureStr().startsWith("n") || term.getNatureStr().startsWith("v") || term.getNatureStr().startsWith("d")
				|| term.getNatureStr().startsWith("a")) {
			if (!StopWord.contains(term.getName())) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {

		TFIDF ti = new TFIDF();

		String corpus = CorpusLoad.getText("extract.txt");

		ti.extractKeyword(corpus, 12);

		int aa = 99;

	}

}

class MapValueComparator implements Comparator<Map.Entry<String, Double>> {

	public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
		return o2.getValue().compareTo(o1.getValue());
	}

}