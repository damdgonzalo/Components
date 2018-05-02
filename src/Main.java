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
		
		
		System.out.println(conn.hiHaNotesNoves());
		
		Thread.sleep(10000); //afegir nota nova
		
		System.out.println(conn.hiHaNotesNoves());
		
	}
}