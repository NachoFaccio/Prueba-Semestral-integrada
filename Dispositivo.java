// Clase que representa cualquiera de los dispositivos que están conectados a la red
class Dispositivo {
    /* Atributo que almacena direcciones IP.
     Está declarado como private aplicando el principio de encapsulamiento (POO),
    lo que significa que solo se puede acceder desde dentro de esta clase.*/ 
    private String ip;

    /* Atributo que almacena direcciones MAC.
    También es privado por el mismo motivo de seguridad y control.*/
    private String mac;

    // Constructor de la clase. Un constructor es un método especial que se ejecuta automáticamente.
    public Dispositivo(String ip) {
        this.ip = ip;
        this.mac = "";
    }

    // Método público que permite acceder al valor de la IP desde fuera de la clase
    public String getIp() {
        return ip;
    }
   
    /* Método que devuelve el valor de la MAC.
    Operador ternario:(mac.isEmpty() ? "No encontrada" : mac;): si mac está vacía, devuelve "No encontrada", si no, devuelve su valor*/
    public String getMac() {
        return mac.isEmpty() ? "No encontrada" : mac;
    }
   /* setMac sirve para almacenar la dirección MAC detectada de un dispositivo dentro del objeto Dispositivo.*/
    public void setMac(String mac) {
        this.mac = mac;
    }

    // Muestra la información del dispositivo (IP y MAC)
    public void mostrarInfo() {
        System.out.println("IP: " + ip + "  MAC: " + getMac());
    }
}
