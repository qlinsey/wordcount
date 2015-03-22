package question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.Test;

public class TestWordCount {

	@Test
	public void test() {
		
		Map<String, Integer> sortedMap = new TreeMap<String, Integer>();
		
		File inputFolder = new File(".", "wc_input");
		
		File[] files = inputFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
					String line = "";
					while (line != null) {
						line = br.readLine();
						if (line != null) {
							LineProcessor p = new LineProcessor(null, line);
							String[] words = p.getLine().split(" ");
							for (String word : words) {
								if (word.length() > 0) {
									Integer cnt = sortedMap.get(word);
									if (cnt == null)
										cnt = new Integer(0);
									sortedMap.put(word, cnt.intValue() + 1);
								}
							}
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		 
			}
			
		}
		
		File outputFolder = new File("wc_output", "wc_output.txt");
		Map<String, Integer> testMap = new TreeMap<String, Integer>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(outputFolder.getAbsolutePath()));
			String line = "";
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					String[]  words = line.split(",");
					Assert.assertTrue(words.length == 2);
					testMap.put(words[0], new Integer(words[1]));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println (sortedMap);
		System.out.println (testMap);
		
		Assert.assertTrue(sortedMap.size() == testMap.size());
		Assert.assertTrue(sortedMap.keySet().containsAll(testMap.keySet()));
		Assert.assertTrue(testMap.keySet().containsAll(sortedMap.keySet()));
		
		for (String key : sortedMap.keySet()) {
			Assert.assertTrue(sortedMap.get(key).intValue() == testMap.get(key).intValue());
		}
	}
}
