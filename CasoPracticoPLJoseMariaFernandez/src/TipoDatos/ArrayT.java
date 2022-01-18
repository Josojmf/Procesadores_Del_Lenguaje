package TipoDatos;


public class ArrayT extends TipoDato {

	private int tam;
	
	public ArrayT(String tipo, int tam) {
		super(tipo);
		this.tam = tam;
	}
	
	public int getTam() {
		return this.tam;
	}
	
	public String toString() {
		return "array (" + super.toString() + ", " + this.tam + ")";
	}
}
