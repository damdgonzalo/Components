import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		
		String usuariConnectat = "dgonzalo";
		Class.forName("org.postgresql.Driver");
		
		//Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Component", "postgres", "dam");
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/penpals", "postgres", "root");
		connection.setAutoCommit(true);
		if (connection!=null) System.out.println("-> Connexió establerta amb la base de dades.");
		
		
		//List<String> grups = Arrays.asList("DAM1");
		//ConnexioNotes conn = new ConnexioNotes(connection, grups);
		ConnexioNotes conn = new ConnexioNotes(connection, usuariConnectat);
				
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
		
		Nota n = conn.getNota("\n4");
		System.out.println(n.getTitol() + " - " + n.getText());
		
		Nota nota = new Nota();
		nota.setTitol("Titol");
		nota.setDataPublicacio("2018-3-3");
		nota.setDataUltModificacio("2018-3-3");
		nota.setAutor("Eduardo");
		nota.setText("Prova Xavier");
		
		System.out.println(conn.inserirNovaNota(nota));
		
		
		
	}
}