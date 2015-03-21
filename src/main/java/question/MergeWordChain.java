package question;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MergeWordChain extends Thread {

	private ConcurrentLinkedQueue<SortedWordChain> stream;
	private boolean quit = false;
	
	public MergeWordChain(ConcurrentLinkedQueue<SortedWordChain> stream) {
		this.stream = stream;
	}

	@Override
	public void run() {
		while (!quit || !stream.isEmpty()) {
			SortedWordChain first = stream.poll();
			if (first != null) {
				System.out.println(first);
			}
		}
	}

	public void quit() {
		quit = true;
	}
}
