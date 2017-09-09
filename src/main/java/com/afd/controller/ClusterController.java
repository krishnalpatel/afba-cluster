package com.afd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.afd.business.Clusterizer;

@Controller
@RequestMapping(value = "/")
public class ClusterController {

	@Autowired
	private Clusterizer clusterizer;
	
	@RequestMapping(value = "initclusters", method = RequestMethod.GET)
	public ModelAndView initClusters(){
		clusterizer.initClusters();
		System.out.println("Clusters initialised!");
		return new ModelAndView("/index.jsp");
	}

	@RequestMapping(value = "clusterize", method = RequestMethod.GET)
	public ModelAndView createClusters() {
		clusterizer.createClusters();
		System.out.println("Clusters Created");
		return new ModelAndView("/index.jsp");
	}

	@RequestMapping(value = "comparewithin", method = RequestMethod.GET)
	public ModelAndView compareWithinClusters() {
		clusterizer.compareWithinClusters();
		System.out.println("Clusters Compared");
		return new ModelAndView("/index.jsp");
	}

	public Clusterizer getClusterizer() {
		return clusterizer;
	}

	public void setClusterizer(Clusterizer clusterizer) {
		this.clusterizer = clusterizer;
	}
}
