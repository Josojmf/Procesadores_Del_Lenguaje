package TipoDatos;

public class Identificador  extends ComponenteLexico{

	private String lexema;

	public Identificador(String lexema) {
		super("id");
		this.lexema = lexema;
	}

	public String getLexema() {
		return this.lexema;
	}
	
	public String toString() {
		return super.toString()+","+this.getLexema();
	} 

}
