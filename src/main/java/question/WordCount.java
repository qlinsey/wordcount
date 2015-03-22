package question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordCount extends Thread {

	private final File file;
	private final ExecutorService executor = Executors.newFixedThreadPool(1000);
	final WordMerger [] mergers;

	public WordCount(WordMerger [] mergers, File file) {
		this.file = file;
		this.mergers = mergers;
	}
	
	public ExecutorService getExecutor() {
		return executor;
	}

	@Override
	public void run() {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					// Each LineProcessor thread will handle each line
					LineProcessor p = new LineProcessor(this.mergers, line);
					executor.execute(p);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
