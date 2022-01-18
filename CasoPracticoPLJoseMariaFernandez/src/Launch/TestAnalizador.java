package Launch;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import Analizador.Analizador;
import Lexico.Lexico;
import TipoDatos.ComponenteLexico;

public class TestAnalizador {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduzca el numero de programa a probar (1-12)");
		 int num = sc.nextInt();
		 String fichero="programas/programa"+num+".txt";
		 System.out.println("Programa a analizar: "+ fichero + "\n");
		 Analizador analiz= new Analizador(new Lexico(fichero,StandardCharsets.UTF_8));
		 analiz.programa();
		 System.out.println("Tabla de simbolos \n\n"+analiz.tablaSimbolos());
	}
}