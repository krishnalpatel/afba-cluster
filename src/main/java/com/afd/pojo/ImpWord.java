package com.afd.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ImpWord {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true)
	private String word;
	
	int frequency;

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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return "ImpWord [id=" + id + ", word=" + word + ", frequency=" + frequency + "]";
	}

}
