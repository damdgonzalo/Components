import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class Main {
	private Connection conn;
	private Statement stmt;
	
	private int comptadorNotes;
	
	public Main(Connection conn, Statement stmt) throws Exception {
		this.conn = conn;
		this.stmt = stmt;
	
		comptarNotesInicials();
	}
	
//----------------------------------------------------------------------------------------------------------------------
	
	public boolean hiHaNotesNoves() throws Exception {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS \"comptNotes\"FROM \"Notes\"");
		rs.next();
		
		int comptNotesNou = rs.getInt("comptNotes");
		
		if (comptNotesNou > comptadorNotes) {
			comptadorNotes = comptNotesNou;
			return true;
		}
		
		return false;
	}
	
	
	
	private void comptarNotesInicials() throws Exception {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS \"comptNotes\"FROM \"Notes\"");
		rs.next();
		
		comptadorNotes = rs.getInt("comptNotes");
	}
}