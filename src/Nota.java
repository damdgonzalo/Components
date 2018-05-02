import java.util.LinkedList;
import java.util.List;

public class Nota {

	private String titol;
	private String text;
	private String dataPublicacio;
	private String dataUltModificacio;
	private String autor;
	private List<String> etiquetes;
	
	public Nota() {
		etiquetes = new LinkedList<>();
		titol = "";
		text = "";
		dataPublicacio = "";
		dataUltModificacio = "";
		autor = "";
	}

	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDataPublicacio() {
		return dataPublicacio;
	}

	public void setDataPublicacio(String dataPublicacio) {
		this.dataPublicacio = dataPublicacio;
	}

	public String getDataUltModificacio() {
		return dataUltModificacio;
	}

	public void setDataUltModificacio(String dataUltModificacio) {
		this.dataUltModificacio = dataUltModificacio;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public List<String> getEtiquetes() {
		return etiquetes;
	}

	public void setEtiquetes(List<String> etiquetes) {
		this.etiquetes = etiquetes;
	}
	
	
}
