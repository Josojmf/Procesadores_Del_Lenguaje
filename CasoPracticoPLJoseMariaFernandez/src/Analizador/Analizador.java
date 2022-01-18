package Analizador;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import Lexico.Lexico;
import TipoDatos.*;

public class Analizador {

	private ComponenteLexico componenteLexico;
	private Lexico lexico; 
	private Hashtable<String, TipoDato> simbolos; 
	private Vector<String> errores = new Vector<String>(); 
	private String comparaciontipo=null; 
	
/*
 * Constructor
 */
	public Analizador(Lexico lexico) {
		this.simbolos = new Hashtable<String, TipoDato>();
		this.lexico = lexico;
		this.componenteLexico = this.lexico.getComponenteLexico(); //coge las palabras reservadas
	}
	
	/*
	 * Devuelve un string con los tipos de datos y las etiquetas de los simbolos
	 */
	public String tablaSimbolos() {
		String simbolos = "";
		Set<Map.Entry<String, TipoDato>> s = this.simbolos.entrySet();
		for(Map.Entry<String, TipoDato> k: s)
			simbolos = simbolos + "<'" + k.getKey() + "'," + k.getValue().toString() + "> \n";
		return simbolos;
	}

	/*
	 * Comprueba que la etiqueta es la esperada
	 */
	private void cmp(String etiqueta) {
		
		if(this.componenteLexico.getEtiqueta().equals(etiqueta)) {
			this.componenteLexico = this.lexico.getComponenteLexico();
		}else 
			System.out.println("Error, se esperaba " + etiqueta);
	}
	/*
	 *COMPRUEBA EL COMIENZO DE UN PROGRAMA
	 */
	public void programa() {
		cmp("void");
		cmp("main");
		cmp("open_bracket");

		declaraciones(); 
		instrucciones();

		cmp("closed_bracket");

	} 

	/*
	 * Comprueb si la etiqueta es la cadena vacia
	 */
	private void declaraciones() {
		String etiqueta = this.componenteLexico.getEtiqueta();

		if(etiqueta.equals("int") || etiqueta.equals("float") || etiqueta.equals("boolean")) {
			declaracionVariable();
			declaraciones();
		}else {
		}

	}

	/*
	 * Comprueba el tipo de dato, y si es un array  lo que puede haber dentro de el
	 */
	public void declaracionVariable() {
		String tipo = tipoPrimitivo();
		int tam = 1; 
			if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")){ 

				cmp("open_square_bracket");  

				if(this.componenteLexico.getEtiqueta().equals("int")) {
					NumeroEntero numero = (NumeroEntero) this.componenteLexico; 
					tam = numero.getValor();
				}
				cmp("int");
				cmp("closed_square_bracket");

				if(this.componenteLexico.getEtiqueta().equals("id")) {
					Identificador id = (Identificador) this.componenteLexico;
					this.simbolos.put(id.getLexema(), new ArrayT(tipo,tam));
				}

				cmp("id");
				cmp("semicolon"); // ;

			}else { 
				this.comparaciontipo = tipo;
				listaDeIdentificadores(tipo);
				cmp("semicolon");
				this.comparaciontipo = null;

			}
		}

	private void listaDeIdentificadores(String tipo) {
		if(this.componenteLexico.getEtiqueta().equals("id")) {

			Identificador id = (Identificador) this.componenteLexico; 
			if(this.simbolos.get(id.getLexema())!=null) {
				this.errores.add("ERROR, en la linea "+this.lexico.getLineas()+" , varibale "+
						id.getLexema() +" ya ha sido declarada");
			}else { 
				simbolos.put(id.getLexema(), new Primitivo(tipo));
			}
			cmp("id");
			asignacion();
			masIdentificadores(tipo); 

		}
	}
	/*
	 * 
	 * --------------------------REGLAS GRAMATICALES A PARTIR DE AQUI--------------------------------------------------------------
	 */
	//más-identificadores -> , id asignacion-declaración más-identificadores | epsilon
	private void masIdentificadores(String tipo) {

		if(this.componenteLexico.getEtiqueta().equals("comma")) {
			cmp("comma"); // , 

			if(this.componenteLexico.getEtiqueta().equals("id")) {

				Identificador id = (Identificador) this.componenteLexico;
				this.simbolos.put(id.getLexema(), new Primitivo(tipo)); 

				cmp("id");
				asignacion();
				masIdentificadores(tipo);
			}

		}else {
		}
	}

	//asignacion -> = expresión-logica | epsilon
	public void asignacion() {
		if(this.componenteLexico.getEtiqueta().equals("assignment")) { //=
			cmp("assignment");
			expresionLogica();
		}else {
		}

	}


	//tipo-primitivo -> int | float | boolean
	private String tipoPrimitivo() {
		String tipo = this.componenteLexico.getEtiqueta(); 

		if(tipo.equals("int")) {
			cmp("int");
		}
		else if(tipo.equals("float")) {
			cmp("float");
		}else if(tipo.equals("boolean")) {
			cmp("boolean");
		}else{
			System.out.println("Error, se esperaba un tipo de dato Int , Float , Boolean");
		}
		return tipo;
	}

	public void instrucciones() { 
		if(this.componenteLexico.getEtiqueta().equals("closed_bracket")) {
		}else {
			instruccion();
			instrucciones();
		}

	}

	private void instruccion() { 
		
		if(this.componenteLexico.getEtiqueta().equals("int")||this.componenteLexico.getEtiqueta().equals("float")
				||this.componenteLexico.getEtiqueta().equals("boolean")) {
			declaracionVariable();
		}else if(this.componenteLexico.getEtiqueta().equals("id")){
			Identificador id =(Identificador) this.componenteLexico;
			variable();
			cmp("assignment");
			
			if(this.simbolos.get(id.getLexema())==null){
				this.comparaciontipo=null;
			}else{
				this.comparaciontipo=this.simbolos.get(id.getLexema()).getTipo();
			}

			expresionLogica();
			this.comparaciontipo=null;
			cmp("semicolon");

		}else if(this.componenteLexico.getEtiqueta().equals("if")) {
			cmp("if");
			cmp("open_parenthesis");
			expresionLogica();
			cmp("closed_parenthesis");
			instruccion();
		}else if(this.componenteLexico.getEtiqueta().equals("else")) {
			cmp("else");
			instruccion();
		}else if(this.componenteLexico.getEtiqueta().equals("while")) {
			cmp("while");
			cmp("open_parenthesis");
			expresionLogica();
			cmp("closed_parenthesis");
			if(this.componenteLexico.getEtiqueta().equals("semicolon")) {
				cmp("semicolon");
			}else {
				instrucciones();
			}
		}else if(this.componenteLexico.getEtiqueta().equals("do")) {
			cmp("do");
			instruccion();
		}else if(this.componenteLexico.getEtiqueta().equals("print")) {
			cmp("print");
			cmp("open_parenthesis");
			variable();
			cmp("closed_parenthesis");
			cmp("semicolon");
		}else if(this.componenteLexico.getEtiqueta().equals("open_bracket")) {
			cmp("open_bracket");
			instrucciones();
			cmp("closed_bracket");
		}
	}

	/*expresion -> expresión + término |
	 * 			   expresión – término |
	 * 			   término
	 */
	
	private void expresion() {
		termino();
		expresionPrima();
	}
	
	private void expresionPrima() {
		if(this.componenteLexico.getEtiqueta().equals("add")) {
			cmp("add");
			termino();
			expresionPrima();
		}else if(this.componenteLexico.getEtiqueta().equals("subtract")) {
			cmp("subtract");
			termino();
			expresionPrima();
		}else {
		}
	}
	
	/*termino -> término * factor |
	 * 		   	 término / factor |
	 * 			 término % factor |
	 * 			 factor
	 */
	
	private void termino() {
		factor();
		terminoPrima();
	}

	private void terminoPrima() {
		if(	this.componenteLexico.getEtiqueta().equals("multiply")){
			cmp("multiply");
			factor();
			terminoPrima();
		}else if(this.componenteLexico.getEtiqueta().equals("divide")) {
			cmp("divide");
			factor();
			terminoPrima();
		}else if(this.componenteLexico.getEtiqueta().equals("remainder") ) {
			cmp("remainder");
			factor();
			terminoPrima();
		}else {
		}
	}

	/*factor ->  ( expresión ) |
	 * 			  variable |
	 *  		  num
	 */
	
	private void factor() {

		if (this.componenteLexico.getEtiqueta().equals("open_parenthesis")) {
			cmp("open_parenthesis");
			expresion();
			cmp("closed_parenthesis");
		}
		else if (this.componenteLexico.getEtiqueta().equals("int")) {
			if(this.comparaciontipo != null) {
				if(!this.comparaciontipo.equals("int")) {
					this.errores.add("ERROR, en la linea " + this.lexico.getLineas() + ""
							+ " se intenta asignar un " + this.comparaciontipo + " con un int");
				}
			}
			cmp("int");
		}else if(this.componenteLexico.getEtiqueta().equals("float")) { 
			if(this.comparaciontipo != null) {
				if(!this.comparaciontipo.equals("float")) {
					this.errores.add("ERROR, en la linea " + this.lexico.getLineas() + ""
							+ " se intenta asignar un " + this.comparaciontipo + " con un float");
				}
			}
			cmp("float");
		}else {
			variable();
		}	
	}

	//variable ->  id | id [ expresión ]
	private void variable() {
		if(this.componenteLexico.getEtiqueta().equals("id")) {
			Identificador id= (Identificador) this.componenteLexico;
			if(this.simbolos.get(id.getLexema())==null) {

				this.errores.add("ERROR, en la linea "+this.lexico.getLineas()+" , varibale "+id.getLexema() +" no ha sido declarada");
			}
			cmp("id");
			if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")) {
				cmp("open_square_bracket");
				expresion();
				cmp("closed_square_bracket");
			}
			if(this.comparaciontipo!=null) {
				if(this.simbolos.get(id.getLexema()).getTipo()!= this.comparaciontipo.toString()) {
					this.errores.add("ERROR, en la linea "+this.lexico.getLineas()+" ,incompatibilidad de tipos en la instruccion de asignacion");
				}
			}
		}
	}

	/*expresion-logico -> expresion-logica || termino-logico |
	 * 					  termino-logico
	 */
	private void expresionLogica() {
		terminoLogico();
		expresionLogicaPrima();

	}
	private void expresionLogicaPrima(){
		if (this.componenteLexico.getEtiqueta().equals("or")) {
			cmp("or");
			terminoLogico();
			expresionLogicaPrima();
		}else {
		}	
	}

	/*termino-logico -> termino-logico && factor-logico | 
	 * 					factor-logico
	 */
	private void terminoLogico() {
		factorLogico();
		terminoLogicoPrimo();
	}
	private void terminoLogicoPrimo() {
		if (this.componenteLexico.getEtiqueta().equals("and")) {
			cmp("and");
			factorLogico();
			terminoLogicoPrimo();
		}else {
			//epsilon
		}		
	}

	/*factor-logico -> ! factor-logico | true | false | 
	 * 				   expresion-relacional
	 */
	private void factorLogico() {
		if (this.componenteLexico.getEtiqueta().equals("not")) {
			cmp("not");
			factorLogico();
		}else if(this.componenteLexico.getEtiqueta().equals("true")) {
			cmp("true");
		}else if(this.componenteLexico.getEtiqueta().equals("false")) {
			cmp("false");
		}else {
			expresionRelacional();
		}
	}

	/*expresion-relacional -> expresión operador-relacional expresión |
	 * 						  expresion
	 */
	private void expresionRelacional() { 
		expresion();
		if( 
				this.componenteLexico.getEtiqueta().equals("greater_than") ||
				this.componenteLexico.getEtiqueta().equals("greater_equals") ||
				this.componenteLexico.getEtiqueta().equals("less_than") ||
				this.componenteLexico.getEtiqueta().equals("less_equals") ||
				this.componenteLexico.getEtiqueta().equals("equals") ||
				this.componenteLexico.getEtiqueta().equals("not_equals")
				) {
			operadorRelacional();
			expresion();
		}
	}

	private void operadorRelacional() {
		if (this.componenteLexico.getEtiqueta().equals("less_than")) {
			cmp("less_than");
			factor();	
		}
		else if (this.componenteLexico.getEtiqueta().equals("less_equals")) {
			cmp("less_equals");
			factor();
		}
		else if (this.componenteLexico.getEtiqueta().equals("greater_than")) {
			cmp("greater_than");
			factor();
		}
		else if (this.componenteLexico.getEtiqueta().equals("greater_equals")) {
			cmp("greater_equals");
		}
		else if (this.componenteLexico.getEtiqueta().equals("equals")) {
			cmp("equals");
			factor();
		}
		else if (this.componenteLexico.getEtiqueta().equals("not_equals")) {
			cmp("not_equals");
			factor();
		}
	}
	
}
