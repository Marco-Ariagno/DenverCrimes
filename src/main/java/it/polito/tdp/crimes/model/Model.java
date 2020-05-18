package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
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
	List<String> best;
	
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
	
	public List<Arco> getArchi(){
		double pesoMedio=0;
		for(DefaultWeightedEdge e:grafo.edgeSet()) {
			pesoMedio+=this.grafo.getEdgeWeight(e);
		}
		pesoMedio=pesoMedio/this.grafo.edgeSet().size();
		
		List<Arco> archi=new ArrayList<>();
		for(DefaultWeightedEdge e:grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>pesoMedio) {
				archi.add(new Arco(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
			}
		}
		Collections.sort(archi);
		return archi;
	}
	
	public List<String> trovaPercorso(String sorgente, String destinazione){
		List<String> parziale=new ArrayList<>();
		best=new ArrayList<>();
		parziale.add(sorgente);
		trovaRicorsivo(destinazione,parziale,0);
		return this.best;
	}

	private void trovaRicorsivo(String destinazione, List<String> parziale, int livello) {
		
		// CASO TERMINALE -> ultimo vertice inserito in parziale = destinazione
		
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size() > best.size()) {
				this.best=parziale;
			}
			return;
		}
		
		//scorro i vicini dell'ultimo vertice inserito in parziale
		for(String vicino:Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			//cammino aciclico, controllo che vertice non sia gia' in parziale
			if(!parziale.contains(vicino)) {
				//provo ad aggiungere
				parziale.add(vicino);
				this.trovaRicorsivo(destinazione, parziale, livello+1);
				parziale.remove(vicino);
			}
		}
	}

	public List<Integer> getMesi(){
		return dao.getMesi();
	}
	
	public List<String> getCategorie(){
		return dao.getCategorie();
	}
}
