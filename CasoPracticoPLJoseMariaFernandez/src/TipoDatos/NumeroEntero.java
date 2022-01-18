package TipoDatos;

public class NumeroEntero extends ComponenteLexico{


	private int valor;

	public NumeroEntero(int valor) {
		super("int");
		this.valor = valor;
	}

	public int getValor() {
		return this.valor;
	}

	public String toString() {
		return super.toString() + "," + this.getValor();
	}

}
