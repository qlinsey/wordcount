package question;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WordMerger extends Thread {

	private ConcurrentLinkedQueue<SortedWordChain> stream;
	private boolean quit = false;
	
	public WordMerger(ConcurrentLinkedQueue<SortedWordChain> stream) {
		this.stream = stream;
	}

	@Override
	public void run() {
		while (!quit || stream.size() > 0) {
			SortedWordChain first = stream.poll();
			if (first != null) {
				System.out.println(first);
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
