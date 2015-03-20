package question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.SynchronousQueue;

public class WordCount extends Thread {

	private final File file;
	final SynchronousQueue<Thread> register;
	final SynchronousQueue<SortedWordChain> stream;
	
	public WordCount(SynchronousQueue<Thread> register, SynchronousQueue<SortedWordChain> stream, File file) {
		this.file = file;
		this.register = register;
		this.stream = stream;
	}

	@Override
	public void run() {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					LineProcessor p = new LineProcessor(this.register, this.stream, line);
					p.start();
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

	public static void main(String[] args) {

		File inputFolder = new File(".", "wc_input");

		if (!inputFolder.exists() || !inputFolder.isDirectory()) {
			System.out.println("Cannot find input folder[wc_input]");
			System.exit(1);
		}

		final SynchronousQueue<Thread> register = new SynchronousQueue<Thread>();
		final SynchronousQueue<SortedWordChain> stream = new SynchronousQueue<SortedWordChain>();
		
		MergeWordChain merger = new MergeWordChain(stream);
		merger.start();
		
		
		for (File file : inputFolder.listFiles()) {
			if (file.isFile()) {
				WordCount wc = new WordCount(register, stream, file);
				wc.start();
			}
		}
		
		try {
			merger.quit();
			merger.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class LineProcessor extends Thread {

		private final String line;
		final SynchronousQueue<Thread> register;
		final SynchronousQueue<SortedWordChain> stream;
		
		public LineProcessor(SynchronousQueue<Thread> register, SynchronousQueue<SortedWordChain> stream, String line) {

			String lc = line.toLowerCase().trim();
			char[] chars = lc.toCharArray();

			int ptr = 0;
			for (int i = 0; i < chars.length; i++) {
				if ((chars[i] == ' ') || (chars[i] >= 'a' && chars[i] <= 'z'))
					chars[ptr++] = chars[i];
			}

			this.line = new String(chars, 0, ptr);
			this.register = register;
			this.stream = stream;
		}

		@Override
		public void run() {

			String[] words = line.split(" ");
			Map<String, Integer> sortedMap = new TreeMap<String, Integer>();
			for (String word : words) {
				if (word.length() > 0) {
					Integer cnt = sortedMap.get(word);
					if (cnt == null)
						cnt = new Integer(0);
					sortedMap.put(word, cnt.intValue() + 1);
				}
			}
			List<String> keys = new ArrayList<String>(sortedMap.keySet());
			SortedWordChain head = null;
			int cnt = 0;
			for (int i = keys.size()-1; i >= 0; i--){
				Word word = new Word(keys.get(i), sortedMap.get(keys.get(i)));
				Word largestWord = null;
				if (head == null) largestWord = word;
				else largestWord = head.getLargestWord();
				head = new SortedWordChain(word, ++cnt, largestWord, head);
			}
			System.out.println (head);
		}
	}

}
