package com.afd.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.afd.pojo.Document;

@Repository(value = "documentDao")
@EnableTransactionManagement()
public class DocumentDao extends Dao {

	public boolean containsDoc(String docName) {
		for (Document document : getDocumentList()) {
			if (document.getDocName().equalsIgnoreCase(docName))
				return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Document> getDocumentList() {
		try {
			return (List<Document>) template.find("select d from Document d");
		} catch (NullPointerException npe) {
			return new ArrayList<Document>();
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void insertDocument(String docName, int totalFrequency) {
		Document document = new Document();
		document.setDocName(docName);
		document.setTotalFrequency(totalFrequency);
		template.save(document);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void insertDocument(Document document){
		template.saveOrUpdate(document);
		//if it doesn't update, try adding @immutable along with @entity
	}

	public Long getDocumentId(String docName) {
		for (Document document : getDocumentList()) {
			if (document.getDocName().equalsIgnoreCase(docName))
				return document.getId();
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int getNoOfDocuments() {
		return ((List<Document>) template.find("select d from Document d")).size();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public double getSimilarity(String attribute) {
		Session session = getSf().openSession();
		session.beginTransaction();
		Query query = session.createQuery("select "+attribute+"(similarity) from Document d");
		double result = 0.0;
		for(Iterator it = query.iterate(); it.hasNext();){
			result = (double) it.next();
 		}
		session.getTransaction().commit();
		session.close();
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Document> getDocumentListOrderBySimilarity() {
		return (List<Document>) template.find("select d from Document d order by d.similarity desc");
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Document> getDocListByClusterId(Long clusterId) {
		return (List<Document>) template.find("select d from Document d where d.clusterId=" + clusterId);
	}

}
