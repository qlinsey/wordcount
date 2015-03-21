package question;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StartCount {

	public static void main(String[] args) {

		File inputFolder = new File(".", "wc_input");

		if (!inputFolder.exists() || !inputFolder.isDirectory()) {
			System.out.println("Cannot find input folder[wc_input]");
			System.exit(1);
		}

		// Thread register to keep track of all the LineProcessor threads
		final List<LineProcessor> register = new ArrayList<LineProcessor>();

		// Thread register to keep track of all the WordCount threads
		final List<WordCount> wcRegister = new ArrayList<WordCount>();
		
		// Create 26 bucket of streams for pruning word beginning with letter a - z
		WordMerger [] mergers = new WordMerger[26];
		
		// Initialize each bucket with each appropriate merger
		// Each merger has its own stream
		for (int i = 0; i < 26; i++) {
			mergers[i] = new WordMerger(new ConcurrentLinkedQueue<SortedWordChain>());
			mergers[i].start();
		}
		
		// Processing each file in the input directory
		for (File file : inputFolder.listFiles()) {
			if (file.isFile()) {
				// Each WordCount thread will handle one file
				WordCount wc = new WordCount(register, mergers, file);
				wc.start();
				wcRegister.add(wc);
			}
		}
		
		// Wait until all the WordCount threads have finish processing
		for (WordCount t : wcRegister) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Wait until all the LineProcessor threads have finish processing
		for (LineProcessor t : register) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Quit the merger thread
		for (int i = 0; i < mergers.length; i++) {
			mergers[i].quit();
			try {
				mergers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
