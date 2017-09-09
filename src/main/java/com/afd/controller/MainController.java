package com.afd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.afd.business.FileCrawler;
import com.afd.business.Worker;
import com.afd.dao.TfidfTempDao;
import com.afd.pojo.TfidfTemp;

@Controller
@RequestMapping(value="/")
public class MainController {
	@Autowired
	private FileCrawler fileCrawler;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private TfidfTempDao tfidfTempDao;
	
	@RequestMapping(value = "scan", method = RequestMethod.GET)
	public ModelAndView scan(HttpServletRequest req) throws Exception {
		
		String directory = req.getParameter("directory");
		System.out.println(directory);

		// using getters n setters for the same as below:
		// fileCrawler fc = new fileCrawler();

		// ApplicationContext context = new
		// ClassPathXmlApplicationContext("../WEB-INF/dispatcher-servlet.xml");
		// now start all of the worker threads
		// N: no of threads to start
		int N = 5;
		// ArrayList<Thread> threadList = new ArrayList<Thread>(N);
		//for (int i = 0; i < N; i++) {
			Worker worker = (Worker) appContext.getBean("worker");
			taskExecutor.execute(worker);
			// Thread thread = new Thread(fileCrawler.createWorker());
			// threadList.add(thread);
			// thread.start();
		//}

		// now place each directory into the workQ

		fileCrawler.processDirectory(directory);
		System.out.println("work Q created: " + fileCrawler.getWorkQ());
		// indicate that there are no more directories to add

		fileCrawler.getWorkQ().finish();
		
		taskExecutor.shutdown();
		/*
		 * for (int i = 0; i < N; i++) { try { threadList.get(i).join(); } catch
		 * (Exception e) { } }
		 */
		return new ModelAndView("/index.jsp");
	}
	
	@RequestMapping(value = "showtfidf", method = RequestMethod.GET)
	public ModelAndView showTfidf() throws Exception {
		Map<String, Object> model = new HashMap<>();
		List<TfidfTemp> tfidfTempList = tfidfTempDao.getAll();
		model.put("tfidfTempList", tfidfTempList);
		return new ModelAndView("/display.jsp", "model", model);
	}
	
	public FileCrawler getFileCrawler() {
		return fileCrawler;
	}

	public void setFileCrawler(FileCrawler fileCrawler) {
		this.fileCrawler = fileCrawler;
	}

	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}
}
