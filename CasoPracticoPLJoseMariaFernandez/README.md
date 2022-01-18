# MEMORIA CASO PRACTICO PROCESADORES DEL LENGUAJE

## Proyecto realizado por José María Fernández Gómez

## install:
git clone   
java -jar CasoPractico.jar  
introduce index of the program to test 1 to 12  

## Se pedía :
Desarrolle un traductor predictivo, recursivo y descendente para un subconjunto de instrucciones de un lenguaje similar a C.  
El traductor debe generar código intermedio para una máquina abstracta de pila.  

## Organización del código:

Se ha dividido el proyecto en 4 paquetes distintos.

Launcher: Contiene la clase ejecutable que creará el analizador  

Analizador: Contiene la clase Analizador 

TipoDatos: Contiene varias clases para dar formato a los tipos de datos y una que se encarga de mapear e introducir en un mapa hash los lexemas  
proporcionados por archivo externo.

Léxico: Contiene la clase que implementa un analizador léxico

## Clases mas significativas: 

TestAnalizador: Se le proporciona por consola el programa a testear, crea en analizador, llama a su método programa que se encarga de inicializar el analizador e
ir llamando a las subrutinas que lo componen. Por último saca por consola el contenido de la tabla de símbolos generada. 

Analizador: Analizador completo que se encarga de reconocer la secuencia de comienzo de un programa de tipo void main{……}. Una vez hechas estas comprobaciones va 
analizando en primer lugar el tipo de variables declaradas y luego las clasifica en función de esta. Después se dispone a analizar la parte operativa o de instrucciones,
Esto se hace por medio de las reglas gramaticales proporcionadas en el enunciado.

Léxico: Analizador léxico, el analizador principal creará un objeto de este dentro de sí para analizar sintácticamente el código proporcionado. Este objeto recibe el programa
a analizar en un archivo txt y una forma de codificación standard de caracteres. A grandes rasgos se encarga de extraer los componentes léxicos del programa y clasificarlos
en función de sus etiquetas, que son strings que se le asignan a cada componente para poder identificarlos y compararlos.

ComponenteLexico: Clase que se encarga de darle formato a todos los componentes léxicos, indicando que a parte de su valor deben de tener una etiqueta para poder ser 
identificados.
hashCompLexicos: Clase que se encarga de extraer todas las cadenas de caracteres o símbolos del archivo proporcionado de programa y guardarlos en un Hash table para luego 
poder identificarlos con sus etiquetas .

Resto de clases en TipoDatos : Son clases que sirven para dar formato a otros tipos de datos en función de su sintaxis, se usan para tener todo más organizado. Todos heredan
de componente léxico para que hereden la etiqueta y las funciones básicas. Luego Primitivo y Array heredarán de la clase Abstracta TipoDato ya que no son datos simples y no
tienen una etiqueta definida.
