package com.afd.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.afd.pojo.BagOfWords;

@Repository(value = "bagOfWordsDao")
@EnableTransactionManagement()
public class BagOfWordsDao extends Dao {
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<BagOfWords> getBagOfWordsList(){
		return (List<BagOfWords>) template.find("select b from BagOfWords b");
	}
}
