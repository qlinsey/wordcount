package question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LineProcessor implements Runnable {

	private final String line;
	final WordMerger [] mergers;

	public LineProcessor(WordMerger [] mergers, String line) {

		// Perform lowercase and trimming of line
		String lc = line.toLowerCase().trim();
		char[] chars = lc.toCharArray();

		// Removing non-alphanumeric characters
		int ptr = 0;
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] == ' ') || (chars[i] >= 'a' && chars[i] <= 'z'))
				chars[ptr++] = chars[i];
		}

		this.line = new String(chars, 0, ptr);
		this.mergers= mergers;
	}

	public String getLine() {
		return line;
	}

	@Override
	public void run() {

		// Sort words in each line and aggregate their count
		Map<String, Integer> sortedMap = sortWords(line);
		
		// Chain up the word lists and drop them to the correct bucket based on the first letter of the word, using pruning strategy
		List<String> keys = new ArrayList<String>(sortedMap.keySet());
		
		SortedWordChain head = null;
		int bucket = -1;
		
		// Start from the largest word in this line
		for (int i = keys.size() - 1; i >= 0; i--) {
			
			Word word = new Word(keys.get(i), sortedMap.get(keys.get(i)));
			
			if (bucket != word.getBucket()) {
				if (head != null) dropWords(head);
				head = null;
				bucket = word.getBucket();
			} 
			
			head = new SortedWordChain(word, head);
		}
		
		if (head != null) dropWords(head);
		
	}
	
	private void dropWords(SortedWordChain head) {
		ConcurrentLinkedQueue<SortedWordChain> stream = mergers[head.getWord().getBucket()].getStream();
		synchronized (stream) {
			stream.offer(head);
			stream.notify();
		}
	}
	
	private Map<String, Integer> sortWords (String line) {
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
		return sortedMap;
	}
}