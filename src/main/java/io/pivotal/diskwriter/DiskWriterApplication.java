package io.pivotal.diskwriter;

import java.io.FileOutputStream;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DiskWriterApplication {

	private static String writers = System.getenv("WRITERS");
	private static int counter = 0;

	public static void main(String[] args) {
		startWriters();
		SpringApplication.run(DiskWriterApplication.class, args);
	}

	public static void startWriters() {

		IntStream.range(0, Integer.parseInt(writers)).forEach(
				i -> new Thread(new Runnable() {

					@Override
					public void run() {
						System.out.println("Starting runner #" + i);
						while (true) {
							byte dataToWrite[] = new byte[1024 * 1000];
							FileOutputStream out;
							try {
								out = new FileOutputStream("/tmp/" + i + ".dat");
								out.write(dataToWrite);
								out.flush();
								out.close();
								counter++;
							} catch (Exception e) {
								e.printStackTrace();
								System.exit(1);
							}

						}
					}
				}).start());
	}

	@RequestMapping(value = "/")
	public String get() {
		return "Hello. You've got " + Integer.parseInt(writers)
				+ " writers who've written " + counter + "MB of data";
	}
}
