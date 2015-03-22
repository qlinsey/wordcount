package question;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class TestSortedWordChain {

	@Test
	public void testMergeTwoListeWithSingleElementThatAreEqual() {
		SortedWordChain right = new SortedWordChain(new Word("a",1),null);
		SortedWordChain left = new SortedWordChain(new Word("a",6),null);
		SortedWordChain leftmerge = left.merge(right);
		
		Assert.assertTrue(leftmerge.getWord().getWord().equals("a"));
		Assert.assertTrue(leftmerge.getWord().getCount() == 7);
		Assert.assertTrue(leftmerge.getNext() == null);
		
		SortedWordChain rightmerge = right.merge(left);
		
		Assert.assertTrue(rightmerge.getWord().getWord().equals("a"));
		Assert.assertTrue(rightmerge.getWord().getCount() == 7);
		Assert.assertTrue(rightmerge.getNext() == null);

	}

	@Test
	public void testMergeTwoListeWithSingleElementThatAreNotEqual() {
		SortedWordChain right = new SortedWordChain(new Word("a",1),null);
		SortedWordChain left = new SortedWordChain(new Word("b",2),null);
		SortedWordChain leftmerge = left.merge(right);
		
		Assert.assertTrue(leftmerge.getWord().getWord().equals("a"));
		Assert.assertTrue(leftmerge.getWord().getCount() == 1);
		Assert.assertTrue(leftmerge.getNext().getWord().getWord().equals("b"));
		Assert.assertTrue(leftmerge.getNext().getWord().getCount() == 2);
		
		SortedWordChain rightmerge = right.merge(left);
		
		Assert.assertTrue(rightmerge.getWord().getWord().equals("a"));
		Assert.assertTrue(rightmerge.getWord().getCount() == 1);
		Assert.assertTrue(rightmerge.getNext().getWord().getWord().equals("b"));
		Assert.assertTrue(rightmerge.getNext().getWord().getCount() == 2);

	}
	
	@Test
	public void testMergeMultipleElements() {
		SortedWordChain left = new SortedWordChain(new Word("a",1),null);
		left.join(new SortedWordChain(new Word("c",3),null)).join(new SortedWordChain(new Word("d",4),null));
		
		SortedWordChain right = new SortedWordChain(new Word("b",2),null);
		right.join(new SortedWordChain(new Word("e",5),null)).join(new SortedWordChain(new Word("f",6),null));
		
		SortedWordChain leftmerge = left.merge(right);
		
		List<Word> list = new ArrayList<Word>();
		
		SortedWordChain tmp = leftmerge;
		
		while (tmp != null) {
			list.add(tmp.getWord());
			tmp = tmp.getNext();
		}
		
		Assert.assertTrue(list.size() == 6);
		Assert.assertTrue(list.get(0).getWord().equals("a"));
		Assert.assertTrue(list.get(1).getWord().equals("b"));
		Assert.assertTrue(list.get(2).getWord().equals("c"));
		Assert.assertTrue(list.get(3).getWord().equals("d"));
		Assert.assertTrue(list.get(4).getWord().equals("e"));
		Assert.assertTrue(list.get(5).getWord().equals("f"));
		
		Assert.assertTrue(list.get(0).getCount() == 1);
		Assert.assertTrue(list.get(1).getCount() == 2);
		Assert.assertTrue(list.get(2).getCount() == 3);
		Assert.assertTrue(list.get(3).getCount() == 4);
		Assert.assertTrue(list.get(4).getCount() == 5);
		Assert.assertTrue(list.get(5).getCount() == 6);
		
		
	}
}
