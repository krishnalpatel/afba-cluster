package com.afd.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Cluster {

	@Id
	@GeneratedValue
	private Long id;

	private double high;

	private double low;

	private int docCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public int getDocCount() {
		return docCount;
	}

	public void setDocCount(int count) {
		this.docCount = count;
	}

	@Override
	public String toString() {
		return "Cluster [id=" + id + ", high=" + high + ", low=" + low + ", count=" + docCount + "]";
	}

}
