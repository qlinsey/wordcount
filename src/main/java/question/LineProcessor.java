package question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LineProcessor extends Thread {

	private final String line;
	final ConcurrentLinkedQueue<SortedWordChain> stream;

	public LineProcessor(ConcurrentLinkedQueue<SortedWordChain> stream,
			String line) {

		String lc = line.toLowerCase().trim();
		char[] chars = lc.toCharArray();

		int ptr = 0;
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] == ' ') || (chars[i] >= 'a' && chars[i] <= 'z'))
				chars[ptr++] = chars[i];
		}

		this.line = new String(chars, 0, ptr);
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
		for (int i = keys.size() - 1; i >= 0; i--) {
			Word word = new Word(keys.get(i), sortedMap.get(keys.get(i)));
			Word largestWord = null;
			if (head == null)
				largestWord = word;
			else
				largestWord = head.getLargestWord();
			head = new SortedWordChain(word, ++cnt, largestWord, head);
		}
		
		if (head != null)
			stream.offer(head);
	}
}