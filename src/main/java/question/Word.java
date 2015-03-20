package question;

public final class Word {

	private final String word;
	
	private final int count;
	
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
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.word);
		buffer.append(",");
		buffer.append(this.count);
		return buffer.toString();
	}
}
