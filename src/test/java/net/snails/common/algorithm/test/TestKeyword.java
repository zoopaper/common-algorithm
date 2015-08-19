package net.snails.common.algorithm.test;

import java.util.List;

import net.snails.common.algorithm.keyword.ExtractWord;
import net.snails.common.algorithm.keyword.TFIDFExtractKeyword;
import net.snails.common.algorithm.util.CorpusLoad;

public class TestKeyword {
	
	public static void main(String[] args) {
		ExtractWord ew =new TFIDFExtractKeyword();
		List<String>  keywords =ew.extract(CorpusLoad.getText("extract.txt"), 12);
		for(String keyword:keywords){
			System.out.print(keyword+" | ");
		}
	}

}
