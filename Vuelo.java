class Vuelo {
    private String idVuelo;
    private Avion avion;
    private Ruta ruta;
    private String dia;
    private String hora;
    private int cantidadPasajeros;
    private boolean aterrizo;

    // Constructores

    public Vuelo(String idVuelo, Avion avion, Ruta ruta, String dia, String hora) {
        this.idVuelo = idVuelo;
        this.avion = avion;
        this.ruta = ruta;
        this.dia = dia;
        this.hora = hora;
        aterrizo = false;
    }

    public Vuelo(String idVuelo, Avion avion, Ruta ruta) {
        this.idVuelo = idVuelo;
        this.avion = avion;
        this.ruta = ruta;
        dia = "X";
        hora = "X";
        cantidadPasajeros = 0;
        aterrizo = false;
    }

    // Observadores

    public String getIdVuelo() {
        return idVuelo;
    }

    public Avion getAvion() {
        return avion;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public String getDia() {
        return dia;
    }

    public String getHora() {
        return hora;
    }

    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public boolean getAterrizaje() {
        return aterrizo;
    }

    public String toString() {
        return "Vuelo Nº " + idVuelo +
                " | Avión: " + avion.getIdAvion() +
                " | Ruta: " + ruta.getNumeroRuta() +
                " | Día: " + dia +
                " | Hora: " + hora +
                " | Pasajeros: " + cantidadPasajeros +
                " | Realizado: " + (aterrizo ? "Sí" : "No");
    }

    public boolean equals(Vuelo otroVuelo) {
        return idVuelo.equals(otroVuelo.getIdVuelo());
    }

    // Modificadores

    public void setAterrizaje(boolean aterrizajeConfirmado) {
        aterrizo = aterrizajeConfirmado;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setCantidadPasajeros(int cantidadPasajeros) {
        this.cantidadPasajeros = cantidadPasajeros;
    }

    // Marca el vuelo como realizado y actualiza los datos del avión
    public void marcarComoRealizado() {
        if (!aterrizo) {
            // Actualiza cantidad de vuelos del avión
            avion.setCantidadVuelos(avion.getCantidadVuelos() + 1);

            // Suma la distancia de la ruta a los km recorridos del avión
            int nuevaDistancia = avion.getKmRecorridos() + ruta.getDistancia();
            avion.setKmRecorridos(nuevaDistancia);

            aterrizo = true;
        }
    }

    // Propios de la clase
    public static String normalizarDia(String texto) { // Quita las tildes de los dias para evitar errores
        texto = texto.toLowerCase();
        texto = texto.replace("á", "a");
        texto = texto.replace("é", "e");
        texto = texto.replace("í", "i");
        texto = texto.replace("ó", "o");
        texto = texto.replace("ú", "u");
        return texto;
    }

    public static int posicionDia(String dia) {
        return switch (normalizarDia(dia)) {
            case "lunes" -> 0;
            case "martes" -> 1;
            case "miercoles" -> 2;
            case "jueves" -> 3;
            case "viernes" -> 4;
            case "sabado" -> 5;
            case "domingo" -> 6;
            default -> -1;
        };

    }

    public static int posicionHora(String hora) {
        return switch (hora.substring(0, 2)) {
            case "08" -> 0;
            case "09" -> 1;
            case "10" -> 2;
            case "11" -> 3;
            case "12" -> 4;
            case "13" -> 5;
            case "14" -> 6;
            case "15" -> 7;
            case "16" -> 8;
            case "17" -> 9;
            case "18" -> 10;
            case "19" -> 11;
            case "20" -> 12;
            case "21" -> 13;
            case "22" -> 14;
            default -> -1;
        };
    }

}
