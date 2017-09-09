package com.afd.pojo;

public class WordStat {

	private String word;
	private double idf;
	private double tf_weighted;
	private double normalized;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public double getTf_weighted() {
		return tf_weighted;
	}

	public void setTf_weighted(double tf_weighted) {
		this.tf_weighted = tf_weighted;
	}

	public double getNormalized() {
		return normalized;
	}

	public void setNormalized(double normalized) {
		this.normalized = normalized;
	}

	@Override
	public String toString() {
		return "WordStat [word=" + word + ", idf=" + idf + ", tf_weighted=" + tf_weighted + ", normalized=" + normalized
				+ "]";
	}

}
