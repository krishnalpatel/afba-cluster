package com.afd.business;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "propertyReader")
@Scope(value = "prototype")
public class PropertyReader {
	
	@Autowired
	private ServletContext servletContext;

	public String readProperty(String property) {

		Properties properties = new Properties();
		InputStream inputStream = null;
		String reqdProperty = null;
		
		try {
			inputStream = new FileInputStream(getServletContext().getRealPath("/WEB-INF//test.properties"));
			
			//load the properties
			properties.load(inputStream);
			
			//get the required property
			reqdProperty = properties.getProperty(property);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return reqdProperty;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
