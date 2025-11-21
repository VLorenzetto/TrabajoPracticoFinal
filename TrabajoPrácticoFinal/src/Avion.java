class Avion {
    private String idAvion;
    private String modeloAvion;
    private int cantidadVuelos;
    private int cantidadAsientos;
    private int kmRecorridos;

    // Constructures
    public Avion(String idAvion, String modeloAvion, int cantidadVuelos, int cantidadAsientos,
            int kmRecorridos) {
        this.idAvion = idAvion;
        this.modeloAvion = modeloAvion;
        this.cantidadVuelos = cantidadVuelos;
        this.cantidadAsientos = cantidadAsientos;
        this.kmRecorridos = kmRecorridos;

    }

    public Avion(String idAvion) {
        this.idAvion = idAvion;
        modeloAvion = "X";
        cantidadVuelos = 0;
        cantidadAsientos = 0;
        kmRecorridos = 0;
    }

    // Observadores
    public String getIdAvion() {
        return idAvion;
    }

    public String getModeloAvion() {
        return modeloAvion;
    }

    public int getCantidadVuelos() {
        return cantidadVuelos;
    }

    public int getCantidadAsientos() {
        return cantidadAsientos;
    }

    public int getKmRecorridos() {
        return kmRecorridos;
    }

    public String toString() {
        return ("Avión: [" + idAvion + "], Modelo: " + modeloAvion + ", Cantidad de vuelos: "
                + cantidadVuelos + ", Cantidad de asientos: " + cantidadAsientos 
                + " Distancia recorrida: " + kmRecorridos+ " Km.");
    }

    public boolean equals(Avion otroAvion) {
        return idAvion.equals(otroAvion.getIdAvion());
    }

    // Modificadores
    public void setCantidadVuelos(int cantidadVuelos) {
        this.cantidadVuelos = cantidadVuelos;
    }

    public void setKmRecorridos(int kmRecorridos) {  
        this.kmRecorridos = kmRecorridos;
    }

    // Propias de la clase

    public static boolean verificarIdAvion(String idAvion) {

        boolean valido = false;

        switch (idAvion.substring(0, 3)) {

            case "LV-":
                valido = idAvion.matches("LV-[A-Z]{3}") || // uso general
                        idAvion.matches("LV-X[0-9]{3}") || // experimental
                        idAvion.matches("LV-S[0-9]{3}") || // deportiva liviana
                        idAvion.matches("LV-SX[0-9]{3}"); // deportiva liviana experimental
                break;

            case "LQ-":
                valido = idAvion.matches("LQ-[A-Z]{3}"); // gubernamental
                break;

            default:
                valido = false;
        }

        return valido;
    }

    public static void imprimirArrAviones(Avion[] arrAviones) {

        System.out.println("ID         MODELO                         VUELOS     ASIENTOS   KM RECORRIDOS");
        System.out.println("--------------------------------------------------------------------------------");

        for (Avion datosAvion : arrAviones) {
            if (datosAvion != null) {
                System.out.printf("%-10s %-30s %-10d %-10d %-15d\n",
                        datosAvion.getIdAvion(),
                        datosAvion.getModeloAvion(),
                        datosAvion.getCantidadVuelos(),
                        datosAvion.getCantidadAsientos(),
                        datosAvion.getKmRecorridos());
            }
        }
    }


}