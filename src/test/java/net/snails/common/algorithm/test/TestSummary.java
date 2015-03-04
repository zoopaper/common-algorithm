package net.snails.common.algorithm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import net.snails.common.algorithm.summary.TextRankSummary;
import net.snails.common.algorithm.util.HtmlUtil;

public class TestSummary {

	public static void main(String[] args) {

		String str = HtmlUtil.removeAllHtmlTag(readText());
		String summary =new TextRankSummary().summary(str, 5);
		System.out.println(summary);

	}

	public static String readText() {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("e:/test.txt"))));

			String line = "";
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
