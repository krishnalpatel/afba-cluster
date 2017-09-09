package com.afd.dao;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Dao {
	
	@Autowired
	private SessionFactory sf;
	
	protected HibernateTemplate template;
	
	@PostConstruct
	public void init(){
		template = new HibernateTemplate(sf);
		template.setCheckWriteOperations(false);
	}

	public SessionFactory getSf() {
		return sf;
	}

	public void setSf(SessionFactory sf) {
		this.sf = sf;
	}

}
