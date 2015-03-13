package net.snails.common.algorithm.summary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import net.snails.common.algorithm.util.StopWord;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * @author krisjin
 * @date 2015-1-15
 */
public class TextRankSummary implements Summary{

	/**
	 * 阻尼系数（Damping Factor）一般取值为0.85
	 */
	private final double d = 0.85f;

	/**
	 * 最大迭代次数
	 */
	private final int MAX_ITER = 200;

	private final double MIN_DIFF = 0.001f;

	/**
	 * 语料文档数
	 */
	private int DOCS_NUM;

	/**
	 * 拆分为[句子[单词]]形式的文档
	 */
	List<List<String>> docs;

	/**
	 * 排序后的最终结果 score <-> index
	 */
	TreeMap<Double, Integer> top;

	/**
	 * 句子和其他句子的相关程度
	 */
	double[][] weight;

	/**
	 * 该句子和其他句子相关程度之和
	 */
	double[] weightSum;

	/**
	 * 迭代之后收敛的权重
	 */
	double[] vertex;

	/**
	 * BM25相似度
	 */
	BM25SimilarityScore bm25;

	public TextRankSummary() {
	}



	private void solve() {
		int cnt = 0;
		for (List<String> sentence : docs) {
			double[] scores = bm25.simAll(sentence);
			
			weight[cnt] = scores;
			weightSum[cnt] = sum(scores) - scores[cnt]; // 减掉自己，自己跟自己肯定最相似
			vertex[cnt] = 1.0;
			++cnt;
		}
		for (int _ = 0; _ < MAX_ITER; ++_) {
			double[] m = new double[DOCS_NUM];
			double max_diff = 0;
			for (int i = 0; i < DOCS_NUM; ++i) {
				m[i] = 1 - d;
				for (int j = 0; j < DOCS_NUM; ++j) {
					if (j == i || weightSum[j] == 0)
						continue;
					m[i] += (d * weight[j][i] / weightSum[j] * vertex[j]);
				}
				double diff = Math.abs(m[i] - vertex[i]);
				if (diff > max_diff) {
					max_diff = diff;
				}
			}
			vertex = m;
			if (max_diff <= MIN_DIFF)
				break;
		}
		
		for (int i = 0; i < DOCS_NUM; ++i) {
			top.put(vertex[i], i);
		}
	}

	/**
	 * 获取前几个关键句子
	 * 
	 * @param size
	 *            要几个
	 * @return 关键句子的下标
	 */
	public int[] getSentence(int size) {
		Collection<Integer> values = top.values();
		size = Math.min(size, values.size());
		int[] indexArray = new int[size];
		Iterator<Integer> it = values.iterator();
		for (int i = 0; i < size; ++i) {
			indexArray[i] = it.next();
		}
		return indexArray;
	}

	
	private  double sum(double[] array) {
		double total = 0;
		for (double v : array) {
			total += v;
		}
		return total;
	}

	private List<String> spiltSentence(String doc) {
		List<String> sentences = new ArrayList<String>();
		if (doc == null)
			return sentences;
		
		String[] fragArr =doc.split("[，,。:：“”？?！!；;]");
			for (String sent : fragArr) {
				sent = sent.trim();
				if (Strings.isNullOrEmpty(sent))
					continue;
			sentences.add(sent);
		}

		return sentences;
	}

	/**
	 * 词性属于名词、动词、副词、形容词的加入计算
	 * 
	 * @param term
	 * @return
	 */
	public  boolean shouldInclude(Term term) {
		if (term.getNatureStr().startsWith("n") || term.getNatureStr().startsWith("v") || term.getNatureStr().startsWith("d")
				|| term.getNatureStr().startsWith("a")) {
			if (!StopWord.contains(term.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param sentenceList
	 * @return
	 */
	private List<List<String>> getParticipleSentence(List<String> sentenceList) {
		List<List<String>> participleList = new ArrayList<List<String>>();
		for (String sentence : sentenceList) {
			List<Term> termList = ToAnalysis.parse(sentence);
			List<String> wordList = new LinkedList<String>();
			for (Term term : termList) {
				if (shouldInclude(term)) {
					wordList.add(term.getRealName());
				}
			}
			participleList.add(wordList);
		}
		return participleList;
	}

	public List<String> getSentenceList(String document, int size) {
		List<String> sentenceList = spiltSentence(document);
		List<List<String>> docs = getParticipleSentence(sentenceList);
		init(docs);
		int[] topSentence = getSentence(size);
		List<String> resultList = new LinkedList<String>();
		for (int i : topSentence) {
			resultList.add(sentenceList.get(i).trim());
		}
		return resultList;
	}

	public String toSummary(String doc, int size) {

		List<String> list = this.getSentenceList(doc, size);

		String summary = Joiner.on("，").join(list);

		summary = summary.replaceAll("\\s*", "").replaceAll("　　", "") + "。";

		return summary;
	}

	private void init(List<List<String>> docs) {

		this.docs = docs;
		bm25 = new BM25SimilarityScore(docs);
		DOCS_NUM = docs.size();
		weight = new double[DOCS_NUM][DOCS_NUM];
		weightSum = new double[DOCS_NUM];
		vertex = new double[DOCS_NUM];
		top = new TreeMap<Double, Integer>(Collections.reverseOrder());
		solve();

	}
}
