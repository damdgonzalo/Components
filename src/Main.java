import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		Class.forName("org.postgresql.Driver");
		
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/penpals", "postgres", "root");
		connection.setAutoCommit(true);
		if (connection!=null) System.out.println("-> Connexió establerta amb la base de dades.");
		
		
		List<String> grups = Arrays.asList("DAM1");
		ConnexioNotes conn = new ConnexioNotes(connection, grups);
		
				
		Thread.sleep(3000); //Afegir nota nova
		
		boolean hiHaNoves = conn.hiHaNotesNoves();
		
		if (hiHaNoves) {
			System.out.println("\nNotes noves:");
		
			List<Nota> llistaNotesNoves = conn.getNotesNoves();
			for (int i=0; i<llistaNotesNoves.size(); i++) {
				System.out.println("   - " + llistaNotesNoves.get(i).getTitol() + " - " + llistaNotesNoves.get(i).getText());
			}
		}
		
		else System.out.println ("\nNo hi ha notes noves");
		
	}
}