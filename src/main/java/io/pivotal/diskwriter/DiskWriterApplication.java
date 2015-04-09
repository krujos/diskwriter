package io.pivotal.diskwriter;

import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DiskWriterApplication {

	private static String writers = System.getenv("WRITERS");
	private static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) {
		startWriters();
		SpringApplication.run(DiskWriterApplication.class, args);
	}

	static class WriterRunnable implements Runnable {
		private int index;

		public WriterRunnable(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			System.out.println("Starting runner #" + index);
			while (true) {
				byte dataToWrite[] = new byte[1024 * 1000];
				FileOutputStream out;
				try {
					out = new FileOutputStream("/tmp/" + index + ".dat");
					out.write(dataToWrite);
					out.flush();
					out.close();
					counter.incrementAndGet();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}

	}

	public static void startWriter(int index) {
		WriterRunnable runnable = new WriterRunnable(index);
		Thread t = new Thread(runnable);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	public static void startWriters() {
		IntStream.range(0, Integer.parseInt(writers)).forEach(
				i -> startWriter(i));
	}

	@RequestMapping(value = "/")
	public String get() {
		return "Hello. You've got " + Integer.parseInt(writers)
				+ " writers who've written " + counter + "MB of data";
	}
}
