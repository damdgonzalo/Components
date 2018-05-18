import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * <b>DESCRIPCIÓ</b></br>
 * Component per enviar i rebre notes.
 *</br></br>
 * <b>REQUERIMENTS</b></br>
 * La base de dades ha de tenir les següents taules: </br>
 *		- Taula "Notes" ( </br>
 *			&emsp;&emsp; idNota <code>INTEGER</code>,</br>
 *			&emsp;&emsp; titol  <code>VARCHAR(30)</code>,</br>
 *			&emsp;&emsp; dataCreacio  <code>DATE</code>,</br>
 *			&emsp;&emsp; ultimaModificacio  <code>DATE</code>,</br>
 *			&emsp;&emsp; cos  <code>TEXT</code>,</br>
 *			&emsp;&emsp; autor  <code>VARCHAR(20),</code></br>
 *			&emsp;&emsp; <code>PRIMARY KEY</code> (idNota)</br>
 *			)</br></br>
 *
 *		- Taula "Grups" ( </br>
 *			&emsp;&emsp; idGrup  <code>INTEGER</code>,</br>
 *			&emsp;&emsp; nom  <code>VARCHAR(30)</code>,</br>
 *			&emsp;&emsp; administrador  <code>VARCHAR(20)</code>,</br>
 *			&emsp;&emsp; dataCreacio  <code>DATE</code></br>
 *			&emsp;&emsp; <code>PRIMARY KEY</code> (idGrup)</br>
 *			)</br></br>
 *
 * 		- Taula "GrupsNota" ( </br>
 *			&emsp;&emsp; idGrup  <code>INTEGER</code>,</br>
 *			&emsp;&emsp; idNota  <code>INTEGER</code>,</br>
 *			&emsp;&emsp; <code>PRIMARY KEY</code> (idGrup, idNota),</br>
 *			&emsp;&emsp; <code>FOREIGN KEY</code> (idGrup) <code>REFERENCES</code> Grups(idGrup),</br>
 *			&emsp;&emsp; <code>FOREIGN KEY</code> (idNota) <code>REFERENCES</code> Nota(idNota)</br>
 *			)</br></br>
 *
 *		- Taula "GrupsUsuari" ( </br>
 *			&emsp;&emsp; idGrup  <code>INTEGER</code>,</br>
 *			&emsp;&emsp; idUsuari  <code>VARCHAR(20)</code>,</br>
 *			&emsp;&emsp; <code>PRIMARY KEY</code> (idGrup, idUsuari),</br>
 *			&emsp;&emsp; <code>FOREIGN KEY</code> (idGrup) <code>REFERENCES</code> Grups(idGrup),</br>
 *			)</br></br>
 *
 */
public class ConnexioNotes implements IConnexioNotes{
	private Connection conn;
	private Statement stmt;
	
	private int comptadorNotes;
	private int comptadorNotesNoves;
	
	private List<String> grups;
	private String usuariConnectat;
	
	/**
	 * Constructor
	 * @param conn Connexió amb la base de dades
	 * @param grups Llista amb els noms dels grups als que pertany l'usuari
	 * @throws Exception
	 */
	/*public ConnexioNotes(Connection conn, List<String> grups) throws Exception {
		this.conn = conn;
		this.grups = grups;
	
		comptadorNotes = comptarNotesActuals();
		comptadorNotesNoves = 0;
	}*/
	
	public ConnexioNotes(Connection conn, String usuariConnectat) throws Exception {
		this.conn = conn;
		this.usuariConnectat = usuariConnectat;
		grups = getGrupsUsuari();
		
		comptadorNotes = comptarNotesActuals();
		comptadorNotesNoves = 0;
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
	 * Retorna una llista amb totes les notes que hi ha als grups que pertany l'usuari
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
	 * Retorna una llista nom�s amb les notes noves que s'han penjat als grups que pertany l'usuari.
	 * Si no hi ha cap nota nova, retorna una llista buida.
	 * Es recomana utilitzar sempre despr�s de la funci� hiHaNotesNoves() , si aquesta retorna TRUE.
	 * @return Llista amb les notes noves que t� l'usuari
	 */
	public List<Nota> getNotesNoves() throws Exception {
		String query = "SELECT g.\"idGrup\", n.* FROM \"GrupsNota\" g";
			  query += " INNER JOIN \"Notes\" n ON g.\"idNota\" = n.\"idNota\" WHERE ";
		
		//busca notes noves a cada grup
		for (int i=0; i<grups.size(); i++) {
			
			if (i==grups.size()-1) query += "g.\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "')";
			else query += "g.\"idGrup\" = (SELECT \"idGrup\" FROM \"Grups\" WHERE \"nom\"='" + grups.get(i) + "') OR ";
		}
		
		query += " ORDER BY n.\"idNota\" DESC LIMIT " + (comptadorNotesNoves);
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		
		List<Nota> llistaNotesNoves = new LinkedList<>();
		
		comptadorNotes += comptadorNotesNoves;
		
		comptadorNotesNoves = 0;
		while (rs.next()) {
			Nota notaNova = new Nota();
			notaNova.setTitol(rs.getString("titol"));
			notaNova.setDataPublicacio(rs.getString("dataCreacio"));
			notaNova.setDataUltModificacio(rs.getString("ultimaModificacio"));
			notaNova.setText(rs.getString("cos"));
			notaNova.setAutor(rs.getString("autor"));
			
			llistaNotesNoves.add(notaNova);
		}
		
		return llistaNotesNoves;
	}
	
//----------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Comprova si hi ha notes noves en algun dels grups als que pertany l'usuari
	 * @return TRUE si l'usuari t� notes noves
	 */
	public boolean hiHaNotesNoves() throws Exception {
		int comptadorAux = comptarNotesActuals();
		if (comptadorAux != 0) {
			if (comptadorAux > comptadorNotes) {
				comptadorNotesNoves = comptadorAux - comptadorNotes;
				return true;
			}
		}
		
		return false;
	}
	
	
//----------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Compta les notes que hi ha a la base de dades als grups als que pertany l'usuari
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
		
		int notesActuals = rs.getInt("comptNotes");
		
		return notesActuals;
	}
	
//----------------------------------------------------------------------------------------------------------------------
	

	/**
	 * Retorna una llista amb els noms dels grups als quals pertany l'usuari connectat
	 * @return Llista amb noms de grups existents als que pertany l'usuari
	 */
	public List<String> getGrupsUsuari() throws Exception {
		String query = "SELECT gr.\"nom\" FROM \"Grups\" gr"
					 + " INNER JOIN \"GrupsUsuaris\" gu ON gr.\"idGrup\"=gu.\"idGrup\""
					 + " WHERE gu.\"idUsuari\"='" + usuariConnectat + "'";
		
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		List<String> grups = new LinkedList<>();
		while (rs.next()) grups.add(rs.getString("nom"));
		
		
		return grups;
	}

}