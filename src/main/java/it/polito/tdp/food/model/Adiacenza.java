package it.polito.tdp.food.model;

public class Adiacenza {
	
	private Portion p1;
	private Portion p2;
	private double peso;
	
	public Adiacenza(Portion p1, Portion p2, double peso) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.peso = peso;
	}

	public Portion getP1() {
		return p1;
	}

	public void setP1(Portion p1) {
		this.p1 = p1;
	}

	public Portion getP2() {
		return p2;
	}

	public void setP2(Portion p2) {
		this.p2 = p2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	

}
