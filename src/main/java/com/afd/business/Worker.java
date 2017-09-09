package com.afd.business;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "worker")
@Scope(value="prototype")
public class Worker implements Runnable {

	static int i = 0;

	@Autowired
	private WorkQueue queue;
	private int n;
	@Autowired
	private CustomReader customReader;

	public Worker() {
		n = i++;
		System.out.println("worker:"+n+" constructor");
	}

	// public Worker(int n) {
	// System.out.println("in worker"+n+" constructor!");
	// this.queue = queue;
	// this.setN(n);
	// }

	// since main thread has placed all directories into the workQ, we
	// know that all of them are legal directories; therefore, do not need
	// to try ... catch in the while loop below
	@Override
	public void run() {
		System.out.println("in worker" + n + ": run()");
		System.out.println("CustomReader for Worker"+n+": "+customReader.toString());
		System.out.println("Queue for Worker" + n + ": \n" + queue.toString());
		String name;
		while ((name = queue.remove()) != null) {
			System.out.println("first of queue: " + name);
			File file = new File(name);
			String entries[] = file.list();
			if (entries == null)
				continue;
			for (String entry : entries) {
				if (entry.compareTo(".") == 0)
					continue;
				if (entry.compareTo("..") == 0)
					continue;
				String fn = name + "\\" + entry;
				System.out.println(fn);

				// call out another class or function to read the file
				// call yet another fn in the called fn to measure the frequency
				// and preapre a map for the same
				File newFile = new File(fn);
				if (!newFile.isDirectory())
					try {
						customReader.readFile(newFile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				
			}
		}
	}

	public WorkQueue getQueue() {
		return queue;
	}

	public void setQueue(WorkQueue queue) {
		this.queue = queue;
	}

	public CustomReader getCustomReader() {
		return customReader;
	}

	public void setCustomReader(CustomReader customReader) {
		this.customReader = customReader;
	}
}
