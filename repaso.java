import java.util.Random;
import java.util.Scanner;
public class repaso{
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        // //ejercicio 1 Patrón en pirámide Usa dos bucles for para imprimir:
        // int num = 4;

        // for (int i = 1; i <= num; i++) {
        //     for (int j = i; j < num; j++) {
        //         System.err.print(" ");
        //     }
        //     for (int j = 1; j <= (2*i-1); j++) {
        //         System.out.print("*");
        //     }
        //     System.out.println(); 
        // }

        // Ejercicio 2 Inverso de un número Pide un número entero y usando while obtén el número invertido (ejemplo: 12345 → 54321). Luego, indica si es capicúa.

        // System.out.println("Ingrese su numero entero : ");
        // int num1 = input.nextInt();
        // int original = num1;
        // int invertido = 0;

        // while (num1 > 0 ) {
        //     int digito = num1 % 10;
        //     invertido = invertido * 10 + digito;
        //     num1 = num1 / 10;
        // }
        // System.out.println("Numero al reves es: "+invertido);
        // if (original == invertido) {
        //     System.out.println("el numero es capicua");
        // } else {
        //     System.out.println("el numero no es capicua");
        // }
        //Ejercicio 3 Usa do-while para repetir hasta que elija 0. Número adivinanza con pistas Genera un número aleatorio entre 1 y 100. Usa do-while para que el usuario intente adivinar. Si el número está a ±5 del secreto, muestra "¡Muy cerca!".
        

        // Random numeroAleatorio = new Random();
        // int adivinarnumero = numeroAleatorio.nextInt(100) + 1;
        // int contador = 10;
        // do {
        // System.out.println("Ingrese su numero tiene "+contador+" intentos restantes");
        // int numeroIngresado = input.nextInt();
        // if ((numeroIngresado > adivinarnumero)) {
            
        // }
        // if (numeroIngresado > adivinarnumero) {
        // System.out.println("el numero a adivinar es menor");
        // }else if (numeroIngresado < adivinarnumero){
        // System.out.println("el numero a adivinar es mayor");
        // }else if (numeroIngresado == adivinarnumero){
        // System.out.println("¡ADIVINASTE EL NUMERO!");
        // break;
        // }

        // contador -= 1;
        // }
        // while(contador != 0);

        //Ejercicio 4 Frecuencia de palabras Dado un arreglo de String, muestra cuántas veces aparece cada palabra. Ejemplo: ["hola", "mundo", "hola"] → hola=2, mundo=1. Reto extra: ignora mayúsculas/minúsculas.
    }

}