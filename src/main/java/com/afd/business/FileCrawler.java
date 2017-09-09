package com.afd.business;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "fileCrawler")
@Scope("prototype")
public class FileCrawler {

	@Autowired
	private WorkQueue workQ;
	//static int i = 0;
	
	public FileCrawler() {
		System.out.println("in filecrawler constructor");
		setWorkQ(getWorkQ());
	}

	/*
	public Worker createWorker(int n) {
		//System.out.println("in FileCrawler: createWorker()");
		Worker worker = getWorker();
		worker.setN(n);
		return worker;
	}
	*/
	
	// need try ... catch below in case the directory is not legal

	public void processDirectory(String dir) {
		//System.out.println("in FileCrawler: processDirectory");
		try {
			File file = new File(dir);
			if (file.isDirectory()) {
				String entries[] = file.list();
				if (entries != null)
					getWorkQ().add(dir);

				for (String entry : entries) {
					String subdir;
					if (entry.compareTo(".") == 0)
						continue;
					if (entry.compareTo("..") == 0)
						continue;
					if (dir.endsWith("\\"))
						subdir = dir + entry;
					else
						subdir = dir + "\\" + entry;
					processDirectory(subdir);
					//System.out.println(subdir);
				}
			}
		} catch (Exception e) {
			System.out.println("Illegal Directory");
			e.printStackTrace();
		}
	}

	public WorkQueue getWorkQ() {
		return workQ;
	}

	public void setWorkQ(WorkQueue workQ) {
		this.workQ = workQ;
	}

}
