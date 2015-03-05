package net.snails.common.algorithm.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import net.snails.common.algorithm.test.TestSummary;

public class CorpusLoad {

	public static String getText(String path) {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(TestSummary.class.getResourceAsStream("/" + path)));

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

		return sb.toString().replaceAll("\\s*", "").replaceAll("　　", "");

	}

}
