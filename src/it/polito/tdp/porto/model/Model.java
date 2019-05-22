package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	PortoDAO pdao = new PortoDAO();
	Graph<Author,DefaultEdge> grafo;
	ArrayList<Author> autori;
	HashMap<Integer,Author> idMap ;
	
	public void creaGrafo() {
		
		grafo = new SimpleGraph<>(DefaultEdge.class);
		idMap = new HashMap<Integer,Author>();
		
		//genero idMap
		pdao.getAllAuthors(idMap);
		
		//aggiungo i vertici
		Graphs.addAllVertices(grafo,idMap.values());
		
		//aggiungo gli archi nel dao
		pdao.getEdges(grafo, idMap);
		
		System.out.print("GRAFO CREATO CON "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi");
		
	}
	
	
	
}
