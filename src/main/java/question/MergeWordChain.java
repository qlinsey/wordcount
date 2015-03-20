package question;

import java.util.concurrent.SynchronousQueue;


public class MergeWordChain extends Thread {

	private SynchronousQueue<SortedWordChain> stream;
	private boolean stop = false;
	public MergeWordChain(SynchronousQueue<SortedWordChain> stream) {
		this.stream = stream;
	}
	@Override
	public void run() {
		try {
			while (!stream.isEmpty()) {
				
			}
			SortedWordChain first = stream.take();
			SortedWordChain second = stream.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void quit() {
		this.stop = true;
	}
}
