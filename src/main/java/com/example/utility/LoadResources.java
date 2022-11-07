package com.example.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadResources extends Thread {
	@Override
	public void run() {
		String projectDir = System.getProperty("user.dir");
		String resourcesDir = projectDir + "/src/main/resources/"; // Resources directory: Contains all files
		File files = new File(resourcesDir);

		/* Creating a list of the threads equal to number of files */
		List<Thread> threadsList = new ArrayList<>();

		/* Creating thread objects and adding to the list */
		for (File file : files.listFiles()) {
			threadsList.add(new FileResources(file));
		}

		/* Starting the threads */
		for (Thread t : threadsList) {
			t.start();
			try {
				t.join();
			} catch (InterruptedException ie) {
				System.out.println(ie.getMessage());
			}
		}
	}
}