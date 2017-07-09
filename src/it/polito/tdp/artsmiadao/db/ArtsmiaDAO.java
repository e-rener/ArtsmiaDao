package it.polito.tdp.artsmiadao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.bean.ArtObject;
import it.polito.tdp.artsmia.bean.Artist;
import it.polito.tdp.artsmia.bean.Exhibition;

public class ArtsmiaDAO {
	
	private String numExhibitionsPerArtistAndYear = "SELECT a.name, e.exhibition_title, count(DISTINCT e.exhibition_title) " + 
			"FROM artists a, authorship au, objects o, exhibition_objects eo, exhibitions e " + 
			"WHERE a.artist_id=au.artist_id AND au.object_id=o.object_id AND o.object_id=eo.object_id " + 
			"AND eo.exhibition_id=e.exhibition_id AND e.`begin`=? AND e.`end`=? " + 
			"GROUP BY a.name";
	
	private String exhibitionsPerArtist = "SELECT DISTINCT a.name, a.artist_id, e.exhibition_title, e.exhibition_id " + 
			"FROM artists a, authorship au, objects o, exhibition_objects eo, exhibitions e " + 
			"WHERE a.artist_id=au.artist_id AND au.object_id=o.object_id AND o.object_id=eo.object_id " + 
			"AND eo.exhibition_id=e.exhibition_id AND e.`begin`=2011 AND e.`end`=2011 and a.name=?";
	
	public List<Exhibition> getExhibitions(Map<Integer, Exhibition> exhibitionsMap, int year) {
		String sql = "SELECT * from exhibitions where begin >= ?";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				int exhibitionId = res.getInt("exhibition_id");
				Exhibition exhibition = new Exhibition(exhibitionId, res.getString("exhibition_department"),
						res.getString("exhibition_title"), res.getInt("begin"), res.getInt("end"));
				// Inserisco l'oggetto sia in una lista che in una mappa.
				result.add(exhibition);
				exhibitionsMap.put(exhibitionId, exhibition);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Prblema di connessione al database");
		}
	}
	
	public List<Artist> getArtists(Map<Integer, Artist> artistsMap){
		String sql = "SELECT * from artists";
		List<Artist> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Artist a = new Artist(
						res.getInt("artist_id"),
						res.getString("name")
						);
				result.add(a);
				artistsMap.put(a.getId(), a) ;
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Prblema di connessione al database");
		}
		
	}

	public List<Integer> getIdArtObjectsForExhibition(Exhibition exhibition) {
		String sql = "SELECT object_id from exhibition_objects where exhibition_id = ?";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, exhibition.getId());
			ResultSet res = st.executeQuery();
			while (res.next()) {
				int objectId = res.getInt("object_id");
				result.add(objectId);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Prblema di connessione al database");
		}
	}

	public List<ArtObject> getArtObjects(Map<Integer, ArtObject> artObjectsMap) {
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				int objectId = res.getInt("object_id");
				ArtObject artObject = new ArtObject(objectId, res.getString("classification"), 
						res.getString("continent"), res.getString("country"), res.getInt("curator_approved"), 
						res.getString("dated"), res.getString("department"), res.getString("medium"), 
						res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), 
						res.getString("style"), res.getString("title"));
				// Inserisco l'oggetto sia in una lista che in una mappa.
				result.add(artObject);
				artObjectsMap.put(objectId, artObject);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Prblema di connessione al database");
		}
	}

	
	public List<Integer> getBeginYears() {
		String sql = "SELECT distinct(begin) from exhibitions order by begin DESC";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getInt("begin"));
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Prblema di connessione al database");
		}
	}

	

}
