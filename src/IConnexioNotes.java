import java.util.List;

public interface IConnexioNotes {
	
	public List<Nota> getNotesNoves() throws Exception;
	public List<Nota> getNotes() throws Exception;
	public boolean hiHaNotesNoves() throws Exception;
	public int comptarNotesActuals() throws Exception;
}
