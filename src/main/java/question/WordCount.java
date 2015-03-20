package question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WordCount extends Thread {

	private final File file;

	public WordCount(File file) {
		this.file = file;
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
					LineProcessor p = new LineProcessor(line);
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
		}

	}

	public static void main(String[] args) {

		File inputFolder = new File(".", "wc_input");

		if (!inputFolder.exists() || !inputFolder.isDirectory()) {
			System.out.println("Cannot find input folder[wc_input]");
			System.exit(1);
		}

		for (File file : inputFolder.listFiles()) {
			if (file.isFile()) {
				WordCount wc = new WordCount(file);
				wc.start();
			}
		}
	}

	class LineProcessor extends Thread {

		private final String line;

		public LineProcessor(String line) {

			String lc = line.toLowerCase().trim();
			char[] chars = lc.toCharArray();

			int ptr = 0;
			for (int i = 0; i < chars.length; i++) {
				if ((chars[i] == ' ') || (chars[i] >= 'a' && chars[i] <= 'z'))
					chars[ptr++] = chars[i];
			}

			this.line = new String(chars, 0, ptr);
		}

		@Override
		public void run() {

			System.out.println (line);
			String[] words = line.split(" ");
			
		}
	}

}
