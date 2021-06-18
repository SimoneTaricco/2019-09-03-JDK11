package it.polito.tdp.food.model;

import java.util.ArrayList;
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
	private Map<Integer,Portion> idMap; 
	private FoodDao dao;
	
	private List<Portion> camminoMigliore;
	private double pesoMigliore;
	private int numeroPassi;
	
	public Model () {
		this.dao = new FoodDao();
	}
 
	public void creaGrafo(int calorie) { 

		idMap = new HashMap<Integer,Portion>();
				for (Portion o:dao.listAllPortionsSelected(calorie)) { 
						idMap.put(o.getPortion_id(), o); 
		}
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, idMap.values()); 
			
		for(Adiacenza a:dao.getAdiacenze(idMap)) { 
					Graphs.addEdge(this.grafo, a.getP1(), a.getP2(), a.getPeso());
		}
		
	    	System.out.println("Numero di vertici: " + grafo.vertexSet().size());
	    	System.out.println("Numero di archi: " + grafo.edgeSet().size());	
			
	}
	public List<Portion> vertici() {
			
		TreeMap<Integer,Portion> map = new TreeMap<Integer,Portion>();
			
		for (Portion o:this.grafo.vertexSet()) 
			map.put(o.getPortion_id(), o);

		return new ArrayList<Portion>(map.values());
	}
		
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}

	
	public Map<Integer,Portion> correlate(Portion p) {
		
		TreeMap<Integer,Portion> map = new TreeMap<Integer,Portion>();
			
		for (Portion p2:Graphs.neighborListOf(grafo, p))
			map.put((int)grafo.getEdgeWeight(this.grafo.getEdge(p2, p)), p2);

		return map;
	}
	
	public List<Portion> trovaPercorso(Portion sorgente, int numeroPassi){
		
		this.camminoMigliore = new LinkedList<>();
		List<Portion> parziale = new LinkedList<>();
		parziale.add(sorgente);
		this.pesoMigliore = 0;
		this.cerca(parziale,0);
		this.numeroPassi = numeroPassi;
			
		return this.camminoMigliore;
	}
		
	private void cerca(List<Portion> parziale, int pesoParziale) {
			
		if(parziale.size() == this.numeroPassi){
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
