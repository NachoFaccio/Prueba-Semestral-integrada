import java.util.Scanner;
/* Importa la clase Scanner, que permite leer lo que el usuario escribe por teclado */

import java.net.InetAddress;
/* Importa la clase InetAddress, que se usa para obtener información de direcciones IP y nombres de host */

/* Clase principal del programa. Es donde empieza la ejecución del código */
public class Main {
    public static void main(String[] args) {
        try {
             /* Obtiene la dirección IP del dispositivo que ejecuta este programa. 
             InetAddress.getLocalHost() devuelve la IP local (por ejemplo, 192.168.1.5) */
            InetAddress localHost = InetAddress.getLocalHost();
            String ipLocal = localHost.getHostAddress(); // Se obtiene la IP como texto
            
            /* Aclara que el programa está preparado para escanear máscara de subred clase C (254 IPs), también pide al usuario que ingrese el Network ID (ID de la red) y lo guarda en una variable tipo String de nombre redLocal.*/
            Scanner scanner = new Scanner(System.in);
            System.out.println("Este programa esta disenado para escanear redes clase C (254 IPs)");
            System.out.print("Por favor, ingrese el ID de la red con un punto al final (ej. 192.168.0.):");
            String redLocal = scanner.nextLine();

            // Crear objeto escáner y ejecutar sus métodos
            EscanerRed escaner = new EscanerRed(redLocal);

             /* Llamar al método para escanear las direcciones IP activas en la red */
            escaner.escanearRed();
	
	    /* Llamar al método para obtener las direcciones MAC asociadas a las IPs detectadas */
            escaner.obtenerMACs();

            // Mostrar mensaje sólo si la IP local pertenece a la red ingresada
            if (ipLocal.startsWith(redLocal)) {
                System.out.println("\nAdemas de los dispositivos detectados, la IP local: " + ipLocal +
                        " tambien esta conectada a la red, ya que es la IP del dispositivo donde se esta ejecutando este programa.");
            }

	/* Mostrar todos los resultados obtenidos del escaneo: IPs y MACs */
            escaner.mostrarResultados();

	/* Cerrar el objeto Scanner para liberar recursos del sistema */
            scanner.close();

        } catch (Exception e) {
 /* Si ocurre cualquier error durante la ejecución del programa, se muestra este mensaje */
            System.out.println("Ocurrio un error al iniciar el programa.");
        }
    }
}
