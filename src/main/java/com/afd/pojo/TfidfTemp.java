package com.afd.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TfidfTemp {

	@Id
	@GeneratedValue
	private Long id;

	private String word;

	private Float tfidf;

	private Long docId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Float getTfidf() {
		return tfidf;
	}

	public void setTfidf(Float tfidf) {
		this.tfidf = tfidf;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	@Override
	public String toString() {
		return "TfidfTemp [id=" + id + ", word=" + word + ", tfidf=" + tfidf + ", docId=" + docId + "]";
	}

}
