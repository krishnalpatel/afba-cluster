package com.afd.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.afd.pojo.TfidfTemp;

@Repository(value = "tfidfTempDao")
@EnableTransactionManagement()
public class TfidfTempDao extends Dao {

	public TfidfTempDao() {
		System.out.println("tfidfTempDao Constructor");
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void insert(TfidfTemp tfidfTemp) {
		template.save(tfidfTemp);
	}

	public List<TfidfTemp> getAll() {
		return (List<TfidfTemp>) template.find("select t from TfidfTemp t");
	}

}
