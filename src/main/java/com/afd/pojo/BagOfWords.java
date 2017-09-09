package com.afd.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BagOfWords {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String word;

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

	@Override
	public String toString() {
		return "BagOfWords [id=" + id + ", word=" + word + "]";
	}

}
