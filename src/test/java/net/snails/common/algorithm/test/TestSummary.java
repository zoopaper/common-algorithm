package net.snails.common.algorithm.test;

import net.snails.common.algorithm.summary.TextRankSummary;
import net.snails.common.algorithm.util.CorpusLoad;
import net.snails.common.algorithm.util.HtmlUtil;

public class TestSummary {

	public static void main(String[] args) {
		
		String corpus = CorpusLoad.getText("extract.txt");
		String str = HtmlUtil.removeAllHtmlTag(corpus);
		String summary = new TextRankSummary().toSummary(str, 5);
		System.out.println(summary);

	}
}