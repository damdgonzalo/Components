import java.util.List;

/**
 * Interfície de la classe ConnexioNotes
 */
public interface IConnexioNotes {
	
	public Nota getNota(String idNota) throws Exception; 
	public List<Nota> getNotesNoves() throws Exception;
	public List<Nota> getNotes() throws Exception;
	public boolean hiHaNotesNoves() throws Exception;
	public int comptarNotesActuals() throws Exception;
}
