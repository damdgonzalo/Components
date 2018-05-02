import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class ConnexioNotes implements IConnexioNotes{
	private Connection conn;
	private Statement stmt;
	
	private int comptadorNotes;
	private int comptadorNotesVell;
	private int notesNoves;
	
	private List<String> grups;
	
	/**
	 * Constructor
	 * @param conn Connexió
	 * @param grups Llista amb els noms dels grups als que pertany l'usuari
	 * @throws Exception
	 */
	public ConnexioNotes(Connection conn, List<String> grups) throws Exception {
		this.conn = conn;
		this.grups = grups;
	
		comptadorNotes = comptarNotesActuals();
		comptadorNotesVell = comptadorNotes;
		
		notesNoves = 0;
	}
	
//----------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Retorna tota la informació d'una nota donada la seva ID
	 * @param idNota ID de la nota que es vol
	 * @return Dades de la nota
	 */
	public Nota getNota(String idNota) throws Exception {
		String query = "SELECT * FROM \"Notes\" WHERE \"idNota\"='" + idNota + "'";
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		
		Nota nota = new Nota();
		nota.setTitol(rs.getString("titol"));
		nota.setDataPublicacio(rs.getString("dataCreacio"));
		nota.setDataUltModificacio(rs.getString("ultimaModificacio"));
		nota.setText(rs.getString("cos"));
		nota.setAutor(rs.getString("autor"));
		
		return nota;
	}
	
//----------------------------------------------------------------------------------------------------------------------

	/**
	 * Retorna una llista amb totes les notes que hi ha als grups als que pertany l'usuari
 	 * @return Llista de notes
	 * @throws Exception
	 */
	public List<Nota> getNotes() throws Exception {
				
		String query = "SELECT g.\"idGrup\", n.* FROM \"GrupsNota\" g";
		  query += " INNER JOIN \"Notes\" n ON g.\"idNota\" = n.\"idNota\" WHERE ";
	
		for (int i=0; i<grups.size(); i++) {
			
			if (i==grups.size()-1) query += "g.\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "')";
			else query += "g.\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "') OR ";
		}
	
		//System.out.println(query);
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		List<Nota> llistaNotes = new LinkedList<>();
		
		while (rs.next()) {
			Nota nota = new Nota();
			nota.setTitol(rs.getString("titol"));
			nota.setDataPublicacio(rs.getString("dataCreacio"));
			nota.setDataUltModificacio(rs.getString("ultimaModificacio"));
			nota.setText(rs.getString("cos"));
			nota.setAutor(rs.getString("autor"));
			
			
			llistaNotes.add(nota);
		}
		
		return llistaNotes;
	}
	
//----------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Comprova si s'ha penjat alguna nota nova a algun dels grups als que pertany l'usuari
	 * @return TRUE si hi ha notes noves
	 * @throws Exception
	 */
	public List<Nota> getNotesNoves() throws Exception {
		//String query = "SELECT COUNT(*) AS \"comptNotes\" FROM \"GrupsNota\" WHERE ";
		String query = "SELECT g.\"idGrup\", n.* FROM \"GrupsNota\" g";
			  query += " INNER JOIN \"Notes\" n ON g.\"idNota\" = n.\"idNota\" WHERE ";
		
		for (int i=0; i<grups.size(); i++) {
			
			if (i==grups.size()-1) query += "g.\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "')";
			else query += "g.\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "') OR ";
		}
		
		query += " ORDER BY n.\"idNota\" DESC LIMIT " + (comptarNotesActuals() - comptadorNotesVell);
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		
		List<Nota> llistaNotesNoves = new LinkedList<>();
		
		comptadorNotesVell = comptadorNotes;
		
		comptadorNotes = 0;
		while (rs.next()) {
			Nota notaNova = new Nota();
			notaNova.setTitol(rs.getString("titol"));
			notaNova.setDataPublicacio(rs.getString("dataCreacio"));
			notaNova.setDataUltModificacio(rs.getString("ultimaModificacio"));
			notaNova.setText(rs.getString("cos"));
			notaNova.setAutor(rs.getString("autor"));
			
			llistaNotesNoves.add(notaNova);
			comptadorNotes++;
		}
		
		return llistaNotesNoves;
	}
	
//----------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Comprova si hi ha notes noves
	 * @return TRUE si hi ha notes noves en algun dels grups als que pertany l'usuari
	 */
	public boolean hiHaNotesNoves() throws Exception {		
		int notesActuals = comptarNotesActuals();
		if (notesActuals != 0) {
			if (notesActuals > comptadorNotes) return true;
		}
		
		return false;
	}
	
	
//----------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Compta les notes que hi ha a la base de dades
	 * @return Número de notes que hi ha en un moment determinat
	 */
	public int comptarNotesActuals() throws Exception {
		String query = "SELECT COUNT(*) AS \"comptNotes\" FROM \"GrupsNota\" WHERE ";
		
		for (int i=0; i<grups.size(); i++) {
			
			if (i==grups.size()-1) query += "\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "')";
			else query += "\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "') OR ";
		}
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		
		int notesInicials = rs.getInt("comptNotes");
		
		//System.out.println("Notes actuals: " + notesInicials);
		return notesInicials;
	}
}