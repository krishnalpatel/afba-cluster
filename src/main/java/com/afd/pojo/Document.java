package com.afd.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Document {

	@Id
	@GeneratedValue
	private Long id;

	private String docName;

	private int totalFrequency;

	private String text;

	private double similarity;

	private Long clusterId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public int getTotalFrequency() {
		return totalFrequency;
	}

	public void setTotalFrequency(int totalFrequency) {
		this.totalFrequency = totalFrequency;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public Long getClusterId() {
		return clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	@Override
	public String toString() {
		return "Document [id=" + id + ", docName=" + docName + ", totalFrequency=" + totalFrequency + ", text=" + text
				+ ", similarity=" + similarity + ", clusterId=" + clusterId + "]";
	}

}
