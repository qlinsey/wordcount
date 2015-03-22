package question;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WordMerger extends Thread {

	private SortedWordChain head = null;
	
	private ConcurrentLinkedQueue<SortedWordChain> stream;
	private boolean quit = false;
	
	public WordMerger(ConcurrentLinkedQueue<SortedWordChain> stream) {
		this.stream = stream;
	}

	public SortedWordChain getHead() {
		return head;
	}

	@Override
	public void run() {
		while (!quit || stream.size() > 0) {
			SortedWordChain first = stream.poll();
			if (first != null) {
				//System.out.println (first);
				if (head == null) 
					head = first;
				else {
					try {
						head = head.merge(first);
					} catch (Exception e) {
						e.printStackTrace();
						this.quit();
						
					}
				}
			} else {
				synchronized (stream) {
					try {
						stream.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	
	public void quit() {
		quit = true;
		synchronized(this.stream) {
			this.stream.notify();
		}
	}

	public ConcurrentLinkedQueue<SortedWordChain> getStream() {
		return stream;
	}
	
	
}
