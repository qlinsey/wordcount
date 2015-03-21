package question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class WordCount extends Thread {

	private boolean finish = false;
	private final File file;
	final List<LineProcessor> register;
	final WordMerger [] mergers;

	public WordCount(List<LineProcessor> register, WordMerger [] mergers, File file) {
		this.file = file;
		this.register = register;
		this.mergers = mergers;
	}

	public boolean isFinish() {
		return finish;
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
					// Register the LineProcessor thread so that we can keep track of it
					register.add(p);
					p.start();
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
			finish = true;
		}

	}

}
