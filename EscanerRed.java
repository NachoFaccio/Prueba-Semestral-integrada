// Clase que escanea la red local (LAN) buscando dispositivos conectados a esa red.
// Guarda el ID de la red (por ejemplo en la casa donde uno de los integrantes del grupo 192.168.68.) y una lista con los dispositivos que se encuentren conectados.

import java.io.BufferedReader;
// Esta librería se usa para leer el resultado del comando arp -a, que muestra los dispositivos conectados a la red local.

import java.io.InputStreamReader;
// Se encarga de convertir bytes en caracteres. Es necesaria porque la salida del comando arp -a se obtiene como un flujo de bytes, e InputStreamReader convierte eso en texto, para que después pueda ser leído por BufferedReader.

import java.net.InetAddress;
// Esta librería permite obtener información de direcciones IP y nombres de host. 

import java.util.ArrayList;
// Permite crear listas donde se pueden agregar, quitar o modificar elementos sin necesidad de definir un tamaño fijo. Nosotros la utilizamos para almacenar los dispositivos encontrados durante el escaneo de red ya que no se sabe cuántos serán.

import java.util.List;
// Es una interfaz que define el comportamiento general de una lista. Se usa junto a ArrayLlist para que el código sea más flexible y pueda adaptarse fácilmente si en el futuro se quiere usar otro tipo de lista. 


public class EscanerRed {

    // Variable que guarda el ID de red.
    private String redBase;

    // Lista donde se guardan los dispositivos que respondieron al ping, llamada dispositivosctivos
    private List<Dispositivo> dispositivosActivos;

    // Constructor, recibe la red base y crea la lista por ahora vacia
    public EscanerRed(String redBase) {
        this.redBase = redBase;
        this.dispositivosActivos = new ArrayList<>();
    }

    // Método que escanea la red haciendo ping a todas las IPs del 1 al 254
    public void escanearRed() {
        System.out.println("Escaneando red: " + redBase + "1-254...");

        // El for recorre todas las posibles IPs conectadas a la red (del 1 al 254) haciendo ping con cada una con un tiempo de espera por la respuesta de 700 milisegundos. Si pasa ese tiempo y no obtiene respuesta pasa a la siguiente, pero si en ese tiempo el dispositivo “contestó”, la ip de este dispositivo es agregada a la lista.
        for (int i = 1; i <= 254; i++) {
            String ip = redBase + i; // Construye la IP actual

            try {
        // Crea un objeto InetAddress para esa IP
        // La clase InetAddress se usa para representar direcciones IP. Con InetAddress.getByName(ip) se obtiene un objeto InetAddress de una IP concreta, al cual se le puede hacer ping usando inet.isReachable(700). Este método devuelve true si la IP responde dentro de un rango de 700 milisegundos.
         
                InetAddress inet = InetAddress.getByName(ip);

                InetAddress localHost = InetAddress.getLocalHost(); // IP de la computadora que ejecuta.

                // Hace un ping silencioso y evita agregar la propia IP del host
                if (inet.isReachable(700) && !inet.equals(localHost)) {
                System.out.println("Dispositivo activo encontrado: " + ip);
                dispositivosActivos.add(new Dispositivo(ip));
}

            } catch (Exception e) {
                // Si hay un error al hacer ping, se muestra un mensaje
                System.out.println("Error al escanear IP: " + ip);
            }
        }
    }

    // Método que intenta obtener las direcciones MAC usando la tabla ARP.
    public void obtenerMACs() {
        try {
            // Ejecuta el comando "arp -a" del sistema para ver la tabla ARP(Protocolo de Resolución de Direcciones) con las IPs y las MAC que vió la computadora. El ARP permite asociar direcciones IP con direcciones MAC en una red local.
            Process proceso = Runtime.getRuntime().exec("arp -a");

            /* Esta línea prepara un lector para leer la salida del comando arp -a de forma ordenada, linea por linea.*/
            BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            
            // Mientras haya líneas que leer
            while ((linea = lector.readLine()) != null) {

                // Recorre cada dispositivo que está en la lista dispositivosActivos y ya fue detectado por ping.
                for (Dispositivo dispositivo : dispositivosActivos) {

                    // Si la línea contiene la IP del dispositivo deberá mostrar la dirección MAC, es decir, en una misma línea de la consola, muestra la IP y la MAC correspondiente
                    if (linea.contains(dispositivo.getIp())) {

                        // Se separa la línea por espacios (la MAC está en la segunda columna)
                        String[] partes = linea.trim().split("\\s+");

                        // Si hay al menos dos partes, se asume que la segunda es la dirección MAC
                        if (partes.length >= 2) {
                            // Se guarda la MAC en el objeto Dispositivo
                            dispositivo.setMac(partes[1]);
                        }
                    }
                }
                
            }
        } catch (Exception e) {
            // Si falla el comando arp, muestra un mensaje de error
            System.out.println("No se pudo obtener la tabla ARP.");
        }
    }
    // Método que se ejecuta en la clase Main.
    public void mostrarResultados() {
    // Muestra un mensaje inicial antes de listar los dispositivos que encontró.
    System.out.println("\nDispositivos detectados:");

    // Intenta obtener la dirección IP local del dispositivo que ejecuta el programa
    String ipLocal;
    try {
        ipLocal = InetAddress.getLocalHost().getHostAddress();
    } catch (Exception e) {
        ipLocal = ""; // por si no se puede obtener
    }
    
    // Variable para contar cuántos dispositivos fueron detectados
    int contador = 0;
    
    // Recorre la lista de dispositivos activos para obtener las ip y mac de cada dispositivo que esté conectado a la red.
    for (Dispositivo dispositivo : dispositivosActivos) {
        String ip = dispositivo.getIp();
        String mac = dispositivo.getMac();
 
        // Muestra la ip con su MAC correspondiente de todos los dispositivos detectados y de forma ordenada.
        System.out.println(ip + " - MAC: " + mac);
        contador++;  // Cuenta la cantidad de dispositivos que están conectados a la red, sumando 1 cada vez que se ejecute el for.
    }
    
    System.out.println("\nTotal de dispositivos detectados: " + contador); // Muestra la cantidad de dispositivos conectados a la red, osea muestra el nuevo valor de la variable contador luego de que termine el for.
   

   /* Si no se detecta ningún dispositivo en la red, muestra "No se detecto ningun dispositivo en esta red",
    pero si se detecta por lo menos uno muestra “Además de los dispositivos detectados, la IP local: 
    (el numero de la ip de la pc que este corriendo el programa) también está conectada a la red,
    ya que es la IP del dispositivo donde se está ejecutando este programa.*/
if (contador == 0){
        System.out.println("No se detecto ningun dispositivo en esta red");
    }else{ 
                System.out.println("\nAdemas de los dispositivos detectados, la IP local: " + ipLocal + " tambien esta conectada a la red, ya que es la IP del dispositivo donde se esta ejecutando este programa.");
            }
    }
}