package question;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class StartCount {

	public static void main(String[] args) {

		File inputFolder = new File(".", "wc_input");

		if (!inputFolder.exists() || !inputFolder.isDirectory()) {
			System.out.println("Cannot find input folder[wc_input]");
			System.exit(1);
		}

		// Thread register to keep track of all the WordCount threads
		final ConcurrentLinkedQueue<WordCount> wcRegister = new ConcurrentLinkedQueue<WordCount>();
		
		// Create 26 bucket of streams for pruning word beginning with letter a - z
		WordMerger [] mergers = new WordMerger[26];
		
		// Initialize each bucket with each appropriate merger
		// Each merger has its own stream
		for (int i = 0; i < 26; i++) {
			mergers[i] = new WordMerger(new ConcurrentLinkedQueue<SortedWordChain>());
			mergers[i].start();
		}
		
		// Processing each file in the input directory
		File[] files = inputFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				// Each WordCount thread will handle one file
				WordCount wc = new WordCount(mergers, files[i]);
				wcRegister.add(wc);
			}
			
		}

		
		// Wait until all the threads have finish processing
		for (WordCount t : wcRegister) {
			try {
				t.start();
				t.join();
				t.getExecutor().shutdown();
				t.getExecutor().awaitTermination(60, TimeUnit.HOURS);
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
		
		PrintWriter out = null;
		
		try
		{
		out = new PrintWriter(new BufferedWriter(new FileWriter(new File("wc_output","wc_result.txt"))));
		
		// Collect the merged results from the mergers
		// Each merge potentially can be deployed onto multiple JVM
		// For this demo, all the mergers are deployed on the same JVM
		// thefore there are a limit to the test input file size.
		for (int i = 0; i < mergers.length; i++) {
			SortedWordChain sortList = mergers[i].getHead();
			while (sortList != null) {
				out.print(sortList.getWord().getWord());
				out.print(",");
				out.println(sortList.getWord().getCount());
				sortList = sortList.getNext();
			}
		}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}

}
