package net.snails.common.algorithm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StopWord {

	private static List<String> getDefaultStopWord() {

		List<String> stopWordList = new ArrayList<String>();

		InputStream is = StopWord.class.getResourceAsStream("/stopword.dic");

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line;

		try {
			while ((line = br.readLine()) != null) {
				stopWordList.add(line);
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stopWordList;
	}

	public static boolean contains(String term) {

		return getDefaultStopWord().contains(term);

	}

}
