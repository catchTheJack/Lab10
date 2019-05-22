package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	PortoDAO pdao = new PortoDAO();
	Graph<Author,DefaultEdge> grafo;
	ArrayList<Author> autori;
	HashMap<Integer,Author> idMap ;
	
	
	//creaGrafo è lasciato privato perchè viene richiamato automaticamente dal set model che in
	//questo caso mentre carica le combo box genera il grafo che resta sempre lo stesso 
	private void creaGrafo() {
		
		grafo = new SimpleGraph<>(DefaultEdge.class);
		idMap = new HashMap<Integer,Author>();
		autori = new ArrayList<Author>();
		
		//genero idMap
		pdao.getAllAuthors(idMap,autori);
		
		//aggiungo i vertici
		Graphs.addAllVertices(grafo,idMap.values());
		
		//aggiungo gli archi nel dao
		pdao.getEdges(grafo, idMap);
		
		System.out.print("GRAFO CREATO CON "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi");
		
	}
	
	public List<Author> collab(Author ax){
		int id = ax.getId();
		ArrayList<Author> col = new ArrayList<Author>(pdao.getCollab(id).values());
		return col;
	}
	
	public ArrayList<Author> getAuthors(){
		creaGrafo();
		return autori;
	}
	
	public boolean thereIsCollab(Author a1,Author a2) {
		boolean  collab = false;
		int id1 = a1.getId();
		int id2 = a2.getId();
		
		if(pdao.getCollab(id1).containsKey(id2)) {
			collab = true;
		}
		return collab;
	}
	
	public List<String> trovaCamminoMinimo(Author primo, Author secondo) {
		DijkstraShortestPath<Author, DefaultEdge> dijstra = new DijkstraShortestPath<>(this.grafo) ;
		GraphPath<Author, DefaultEdge> path = dijstra.getPath(primo,secondo) ;
		List<Author> autori = new ArrayList<Author>( path.getVertexList()) ;
		List<String> pubblicazioni = new ArrayList<String>();
		for(int i=0; i<autori.size()-1;i++ ) {
			String pubblicato = pdao.getTitle(autori.get(i).getId(),autori.get(i+1).getId());
			pubblicazioni.add(pubblicato);
		}
		return pubblicazioni;
	}
	
}
