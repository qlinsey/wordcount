package question;

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
		buffer.append("t=");
		buffer.append(this.getWordCounts());
		buffer.append(",l=");
		buffer.append(this.largestWord);
		buffer.append(",w=");
		buffer.append(word);
		SortedWordChain tmp = next;
		while (tmp != null) {
			buffer.append("->");
			buffer.append("t=");
			buffer.append(tmp.getWordCounts());
			buffer.append(",l=");
			buffer.append(tmp.largestWord);
			buffer.append(",w=");
			buffer.append(tmp.getWord());
			tmp = tmp.next;
		}
		return buffer.toString();
	}
}
