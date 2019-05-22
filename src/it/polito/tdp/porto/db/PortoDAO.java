package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	
	public void getAllAuthors(HashMap<Integer,Author> map, List<Author> autori){
		
		final String sql = "SELECT * FROM author";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author author = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				autori.add(author);
				map.put(rs.getInt("id"),author);
				
			}
			
			conn.close();
			

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
			
		}
		
	}
	
	public void getEdges(Graph<Author,DefaultEdge> grafo, HashMap<Integer,Author> mappa) {
		
		final String sql = "SELECT c1.authorid AS aut1, c2.authorid AS aut2 "
							+ "FROM creator c1, creator c2 "
							+ "WHERE c1.authorid != c2.authorid AND c1.eprintid=c2.eprintid AND c1.authorid>c2.authorid";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				grafo.addEdge(mappa.get(rs.getInt("aut1")),mappa.get(rs.getInt("aut2")));
			}
			
			conn.close();

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
			
		}
	}	
		
	public HashMap<Integer,Author> getCollab(int id){
		
		HashMap<Integer,Author> autori = new HashMap<Integer,Author>();
		
		final String sql = "SELECT * "
							+ "FROM author a1 "
							+ "WHERE a1.id IN (SELECT c2.authorid AS aut2 "
							+ "FROM creator c1, creator c2 "
							+ "WHERE c1.authorid != c2.authorid AND c1.eprintid=c2.eprintid AND c1.authorid = ?)";

				try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, id);
				
				ResultSet rs = st.executeQuery();
				
				while (rs.next()) {
					Author author = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
					autori.put(rs.getInt("id"),author);
				}
				
				conn.close();
				return autori;
				
				} catch (SQLException e) {
				 e.printStackTrace();
				throw new RuntimeException("Errore Db");

			}
	}
	
	public String getTitle(int id1,int id2) {
		
		String titolo ="";
		final String sql = "SELECT p.title "
				+ "FROM paper p "
				+ "WHERE p.eprintid "
				+ "IN(SELECT c1.eprintid "
				+ "FROM creator c1, creator c2 "
				+ "WHERE c1.authorid= ? AND c2.authorid= ? )";

			try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id1);
			st.setInt(2, id2);
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				 titolo = rs.getString("title");
			}
			
			conn.close();
			return titolo;
			
			} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");

}
	}
	
}