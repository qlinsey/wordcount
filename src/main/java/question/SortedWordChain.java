package question;

/**
 * 
 * Immutable Sorted word chain containing of words in ascending order. 
 */
public class SortedWordChain {

	private Word word;
	private SortedWordChain next;
	
	public SortedWordChain(Word word, SortedWordChain next) {
		this.word = word;
		this.next = next;
	}

	public SortedWordChain join (SortedWordChain next) {
		SortedWordChain tmp = this;
		while (tmp.next != null) tmp = tmp.next;
		tmp.next = next;
		return this;
	}
	
	public Word getWord() {
		return word;
	}

	public SortedWordChain getNext() {
		return next;
	}
	
	public SortedWordChain merge (SortedWordChain chain) {
		SortedWordChain head = new SortedWordChain(new Word("dummy",0),null);
		SortedWordChain tmp = head;
		SortedWordChain left = this;
		SortedWordChain right = chain;
		while (left != null && right != null) {
			if (left.getWord().getWord().compareTo(right.getWord().getWord()) == 0) {
				SortedWordChain newnode = new SortedWordChain(new Word(left.getWord().getWord(),
							left.getWord().getCount()+right.getWord().getCount()),null);
				tmp.next = newnode;
				tmp = newnode;
				right = right.next;
				left = left.next;
			} else if (left.getWord().getWord().compareTo(right.getWord().getWord()) < 0) {
				SortedWordChain newleft = new SortedWordChain(new Word(left.getWord().getWord(),
						left.getWord().getCount()),null);
				tmp.next = newleft;
				tmp = newleft;
				left = left.next;
			} else {
				SortedWordChain newright = new SortedWordChain(new Word(right.getWord().getWord(),
						right.getWord().getCount()),null);
				tmp.next = newright;
				tmp = newright;
				right = right.next;
			}
		}
		if (left != null) {
			while (left != null) {
				SortedWordChain newleft = new SortedWordChain(new Word(left.getWord().getWord(),
						left.getWord().getCount()),null);
				tmp.next = newleft;
				tmp = newleft;
				left = left.getNext();
			}
		}
		if (right != null) {
			while (right != null) {
				SortedWordChain newright = new SortedWordChain(new Word(right.getWord().getWord(),
						right.getWord().getCount()),null);
				tmp.next = newright;
				tmp = newright;
				right = right.getNext();
			}
		}

		return head.next;
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
