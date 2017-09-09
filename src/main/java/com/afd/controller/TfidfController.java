package com.afd.controller;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.afd.business.TFIDFCalculator;

@Controller
@RequestMapping(value = "/")
public class TfidfController {
	
	@Autowired
	private TFIDFCalculator calculator;
	
	@RequestMapping(value = "calculatetfidf", method = RequestMethod.GET)
	public ModelAndView calculateTfidf() throws Exception {
		Instant start = Instant.now();
		calculator.calculate();
		Instant stop = Instant.now();
		System.out.println("Tf-Idf Calculated and Time Taken: " + Duration.between(start, stop));
		return new ModelAndView("/index.jsp");
	}

	public TFIDFCalculator getCalculator() {
		return calculator;
	}

	public void setCalculator(TFIDFCalculator calculator) {
		this.calculator = calculator;
	}
}
