package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Graph<Portion,DefaultWeightedEdge> grafo; 
	private Map<String,Portion> idMap; 
	private FoodDao dao;
	
	private List<Portion> camminoMigliore;
	private double pesoMigliore;
	private int numeroPassi;
	
	public Model () {
		this.dao = new FoodDao();
	}
 
	public void creaGrafo(int calorie) { 

		idMap = new HashMap<String,Portion>();
			for (Portion o:dao.listAllPortionsSelected(calorie)) { 
				idMap.put(o.getPortion_display_name(), o); 
		}
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, idMap.values()); 
			
		for(Adiacenza a:dao.getAdiacenze(idMap,calorie)) { 
			if (a.getPeso() > 0)
				Graphs.addEdge(this.grafo, a.getP1(), a.getP2(), a.getPeso());
		}
			
	}
	public List<Portion> vertici() {
			
		ArrayList<Portion> res = new ArrayList<Portion>(grafo.vertexSet());
		
		Collections.sort(res,new Comparator<Portion>() {
			public int compare(Portion o1, Portion o2) {
				return (o1.getPortion_display_name().compareTo(o2.getPortion_display_name()));
				}
			}
		);

		return res;
	}
		
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}

	
	public List<PortionAndWeight> correlate(Portion o) {
		
		ArrayList<PortionAndWeight> map = new ArrayList<PortionAndWeight>();
		
		for (Portion o2:Graphs.neighborListOf(grafo, o)) {
		map.add(new PortionAndWeight(o2, (int)this.grafo.getEdgeWeight(grafo.getEdge(o, o2))));
		}
			
		Collections.sort(map, new Comparator<PortionAndWeight>() {
			public int compare(PortionAndWeight o1, PortionAndWeight o2) {
			if (o2.getWeight() - o1.getWeight() < 0) 
				return -1;
			else  			
				return 1;
			}
		}); 
		return map;
	}
	
	public List<Portion> trovaPercorso(Portion sorgente, int numeroPassi){
		
		this.camminoMigliore = new LinkedList<>();
		List<Portion> parziale = new LinkedList<>();
		parziale.add(sorgente);
		this.pesoMigliore = 0;
		this.numeroPassi = numeroPassi;
		
		this.cerca(parziale,0); 
			
		return this.camminoMigliore;
	}
		
	private void cerca(List<Portion> parziale, int pesoParziale) {
			
		if(parziale.size() == this.numeroPassi+1){
			if (pesoParziale > this.pesoMigliore) {
				this.pesoMigliore = pesoParziale;
				this.camminoMigliore = new LinkedList<>(parziale);
				return;
			}
		}
		
		//System.out.println(parziale.toString());
			
		for(Portion vicino: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			
			int pesoAggiunto = (int)grafo.getEdgeWeight(this.grafo.getEdge(vicino, parziale.get(parziale.size()-1)));
			
			if(!parziale.contains(vicino) && parziale.size() <= this.numeroPassi && parziale.size()>=1) {
				pesoParziale += pesoAggiunto;
				parziale.add(vicino);
				
				cerca(parziale, pesoParziale);
				
				pesoParziale -= pesoAggiunto;
				parziale.remove(parziale.size()-1); 
				}
			}
			
	}
	
	public int pesoMassimo() {
		return (int)this.pesoMigliore;
	}

	
}
