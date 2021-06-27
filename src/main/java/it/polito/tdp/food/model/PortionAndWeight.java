package it.polito.tdp.food.model;

public class PortionAndWeight {
	
	private Portion portion;
	private int weight;
	
	public PortionAndWeight(Portion portion, int weight) {
		super();
		this.portion = portion;
		this.weight = weight;
	}

	public Portion getPortion() {
		return portion;
	}

	public void setPortion(Portion portion) {
		this.portion = portion;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	

}
