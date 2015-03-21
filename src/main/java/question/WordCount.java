package question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WordCount extends Thread {

	private final File file;
	final ConcurrentLinkedQueue<Thread> register;
	final ConcurrentLinkedQueue<SortedWordChain> stream;

	public WordCount(ConcurrentLinkedQueue<Thread> register,
			ConcurrentLinkedQueue<SortedWordChain> stream, File file) {
		this.file = file;
		this.register = register;
		this.stream = stream;
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
					LineProcessor p = new LineProcessor(this.stream, line);
					p.start();
					register.add(p);
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

	public static void main(String[] args) {

		File inputFolder = new File(".", "wc_input");

		if (!inputFolder.exists() || !inputFolder.isDirectory()) {
			System.out.println("Cannot find input folder[wc_input]");
			System.exit(1);
		}

		final ConcurrentLinkedQueue<Thread> register = new ConcurrentLinkedQueue<Thread>();
		final ConcurrentLinkedQueue<SortedWordChain> stream = new ConcurrentLinkedQueue<SortedWordChain>();

		MergeWordChain merger = new MergeWordChain(stream);
		merger.start();

		for (File file : inputFolder.listFiles()) {
			if (file.isFile()) {
				WordCount wc = new WordCount(register, stream, file);
				wc.start();
				register.add(wc);
			}
		}

		try {
			for (Thread t : register){
				t.join();
			}
			while (!stream.isEmpty()) {
				stream.wait(100);
			}
			merger.quit();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
