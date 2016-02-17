package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;

public class AsymmetricSimilarity<T> implements Serializable {
	
	private T itemA;
	private T itemB;
	private double similarity = 0f;
	private double oppositeSimilarity = 0f;
		
	public AsymmetricSimilarity() {
		
	}
	
	public AsymmetricSimilarity(T itemA, T itemB, double similarity, double oppSimilarity) {
		super();
		this.itemA = itemA;
		this.itemB = itemB;
		this.similarity = similarity;
		this.oppositeSimilarity = oppSimilarity;
	}
		
	public void setItemA(T itemA) {
		this.itemA = itemA;
	}

	public void setItemB(T itemB) {
		this.itemB = itemB;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public void setOppositeSimilarity(double oppositeSimilarity) {
		this.oppositeSimilarity = oppositeSimilarity;
	}

	public double getSimilarity() {
		return similarity;
	}

	public double getOppositeSimilarity() {
		return oppositeSimilarity;
	}
	
	public double getAverageSimilarity() {
		return Math.abs((similarity - oppositeSimilarity) / 2);
	}
	
	public T getItemA() {
		return itemA;
	}
	
	public T getItemB() {
		return itemB;
	}
}
