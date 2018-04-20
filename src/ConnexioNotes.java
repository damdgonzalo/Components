import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class ConnexioNotes {
	private Connection conn;
	private Statement stmt;
	
	private int comptadorNotes;
	private int comptadorNotesVell;
	
	private List<String> grups;
	
	public ConnexioNotes(Connection conn, List<String> grups) throws Exception {
		this.conn = conn;
		this.grups = grups;
	
		comptarNotesInicials();
	}
	
//----------------------------------------------------------------------------------------------------------------------
	
	public boolean hiHaNotesNoves() throws Exception {
		String query = "SELECT COUNT(*) AS \"comptNotes\" FROM \"GrupsNota\" WHERE ";
		
		for (int i=0; i<grups.size(); i++) {
			
			if (i==grups.size()-1) query += "\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "')";
			else query += "\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "') OR ";
		}
		
		System.out.println(query);
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		
		int compt = rs.getInt("comptNotes");
		System.out.println("Notes trobades:" + compt);
		
		if (compt > comptadorNotes) {
			comptadorNotesVell = comptadorNotes;
			comptadorNotes = compt;
			return true;
		}
		
		else return false;
	}
	
	
	public List<Nota> getNotesNoves() throws Exception {
		List<Nota> llistaNotes = new LinkedList<>();
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT \"idNota\" FROM \"GrupsNota\"W");
		rs.next();
		
		return llistaNotes;
	}
	
	
	private void comptarNotesInicials() throws Exception {
		String query = "SELECT COUNT(*) AS \"comptNotes\" FROM \"GrupsNota\" WHERE ";
		
		for (int i=0; i<grups.size(); i++) {
			
			if (i==grups.size()-1) query += "\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "')";
			else query += "\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "') OR ";
		}
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		
		comptadorNotes = rs.getInt("comptNotes");
		comptadorNotesVell = comptadorNotes;
		
		System.out.println("Notes inicials: " + comptadorNotes);
	}
}