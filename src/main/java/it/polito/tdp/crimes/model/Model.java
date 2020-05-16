package it.polito.tdp.crimes.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<String,DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	public Model() {
		dao=new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		List<Adiacenza> adiacenze=this.dao.getAdiacenze(categoria,mese);
		for(Adiacenza a:adiacenze) {
			if(!this.grafo.containsVertex(a.getV1())) {
				this.grafo.addVertex(a.getV1());
			}
			if(!this.grafo.containsVertex(a.getV2())) {
				this.grafo.addVertex(a.getV2());
			}
			
			if(this.grafo.getEdge(a.getV1(), a.getV2())==null) {
				Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(),a.getPeso());
			}
		}
		System.out.println( this.grafo.vertexSet().size()+" "+this.grafo.edgeSet().size());
	}

	public List<Integer> getMesi(){
		return dao.getMesi();
	}
	
	public List<String> getCategorie(){
		return dao.getCategorie();
	}
}
