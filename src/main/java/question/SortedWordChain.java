package question;

/**
 * 
 * Immutable sorted word chain containing of words in ascending order. Each Chain know how many Words are in this node chain and the
 * largest Word in this chain.
 */
public class SortedWordChain {

	private final Word word;
	private final int wordCounts;
	private final Word largestWord;
	private final SortedWordChain next;
	
	public SortedWordChain(Word word, int wordCounts, Word largestWord, SortedWordChain next) {
		this.word = word;
		this.wordCounts = wordCounts;
		this.largestWord = largestWord;
		this.next = next;
	}

	public Word getWord() {
		return word;
	}

	public SortedWordChain getNext() {
		return next;
	}
	
	public int getWordCounts() {
		return wordCounts;
	}

	public Word getLargestWord() {
		return largestWord;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(word);
		SortedWordChain tmp = next;
		while (tmp != null) {
			buffer.append("->");
			buffer.append(tmp.getWord());
			tmp = tmp.next;
		}
		return buffer.toString();
	}
}
