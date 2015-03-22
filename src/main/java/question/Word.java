package question;

/**
 * Object of a word containing the number of occurances for that word 
 *
 */
public final class Word {

	private final String word;
	
	private int count;
	
	public Word(String word, int count) {
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public int getBucket() {
		if (this.word != null && this.word.length() > 0)
			return this.word.toCharArray()[0]-'a';
		else return -1;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.word);
		buffer.append(",");
		buffer.append(this.count);
		return buffer.toString();
	}
}
