class Ruta {
    private String numeroRuta;
    private String ciudadOrigen;
    private String ciudadDestino;
    private int distancia;
    private boolean esInternacional;

    // Constructores
    public Ruta(String numeroRuta, String ciudadOrigen, String ciudadDestino, int distancia, String esInternacional) {
        this.numeroRuta = numeroRuta;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.distancia = distancia;
        this.esInternacional = Ruta.verificaInternacional(esInternacional);
    }

    public Ruta(String numeroRuta) {
        this.numeroRuta = numeroRuta;
        ciudadOrigen = "X";
        ciudadDestino = "X";
        distancia = 0;
        esInternacional = false;
    }

    // Observadores

    public String getNumeroRuta() {
        return numeroRuta;
    }

    public String getCiudadOrigen() {
        return ciudadOrigen;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public int getDistancia() {
        return distancia;
    }

    public boolean getEsInternacional() {
        return esInternacional;
    }

    public String toString() {
        return "Ruta Nº " + numeroRuta +
                " | Origen: " + ciudadOrigen +
                " | Destino: " + ciudadDestino +
                " | Distancia: " + distancia + " km" +
                " | " + (esInternacional ? "Si" : "No");
    }

    public boolean equals(Ruta otraRuta) {
        return (numeroRuta == otraRuta.getNumeroRuta());
    }

    // Modificadores? (En mi parecer son todos datos fijos)

    // Propias de la clase
    public static boolean verificaInternacional(String internacional) {
        return internacional != null && internacional.equalsIgnoreCase("SI");
    }

    public static void imprimirArrRutas(Ruta[] arrRutas) {

        System.out.println("RUTA N°        ORIGEN           DESTINO     DISTANCIA   INTERNACIONAL");
        System.out.println("-----------------------------------------------------------------------");

        for (Ruta datosRuta : arrRutas) {
            if (datosRuta != null) {
                System.out.printf("%-10d %-15s %-15s %-10d %-15s\\n",
                        datosRuta.getNumeroRuta(),
                        datosRuta.getCiudadOrigen(),
                        datosRuta.getCiudadDestino(),
                        datosRuta.getDistancia(),
                        datosRuta.getEsInternacional() ? "Si" : "No");
            }
        }
    }

}
