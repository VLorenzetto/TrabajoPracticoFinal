import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {  // try-with-resources
            // Declaracion de Arreglos y Matrices
            Avion[] arrAviones = new Avion[100];
            Ruta[] arrRutas = new Ruta[100];
            Vuelo[] arrVuelos = new Vuelo[120]; 
            Vuelo[][] cronograma = new Vuelo[7][15]; // 7 dias de la semana x 15 horas habiles del aereopuerto.

            int opcion;
            do {
                mostrarMenu();
                opcion = validarOpcion(sc, "Opcion: ");
                switch (opcion) {
                    case 1:
                        cargarAviones(arrAviones);
                        cargarRutas(arrRutas);
                        cargarVuelos(arrVuelos, arrAviones, arrRutas);
                        System.out.println("\nDatos EXTRAIDOS y ALMACENADOS Correctamente.");
                        cargarCronograma(cronograma, arrVuelos);

                        
                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        case 4:

                            break;
                        case 5:

                            break;
                        case 6:

                            break;
                        case 0:
                            System.out.println("Saliendo...");
                            break;
                        default:
                            System.out.println("Opción inválida");
                }
            } while (opcion != 0);
        }  // Scanner se cierra automáticamente aquí
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Sistema de Aereolinea ===");
        System.out.println("1) Cargar los Aviones, Rutas y Vuelos de los archios de texto.");
        System.out.println("2) Agregar nuevo Avion a la flota.");
        System.out.println("3) Cargar nuevo vuelo al cronograma.");
        System.out.println("4) Marcar la realización efectiva de un vuelo");
        System.out.println("5) Mostrar el promedio de pasajeros que efectivamente volaron.");
        System.out.println("6) Mostrar para un día dado, la lista de vuelos");
        System.out.println("0) Salir");
    }

    private static int validarOpcion(Scanner sc, String msg) {
        System.out.print(msg);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Ingrese una referencia numérica valida: ");
        }
        return sc.nextInt();
    }

    // Cargar aviones desde un archivo .txt
    public static void cargarAviones(Avion[] arrAviones) {

        String rutaArchivo = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\Aviones.txt";
        
        try {
            //BufferedReader permite leer línea por línea.
            //FileInputStream abre el archivo como bytes, InputStreamReader dice con qué codificación convertir esos bytes → caracteres.
            //"UTF-8" asegura que las tildes y ñ se lean bien.
            BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"));
            String linea;
            int largoLista = 0;

            while ((linea = lector.readLine()) != null && largoLista < arrAviones.length) {

                String[] separador = linea.split(";");
                String idAvion = separador[0];
                String modeloAvion = separador[1];
                int cantidadVuelos = Integer.parseInt(separador[2]);
                int cantidadAsientos = Integer.parseInt(separador[3]);
                int kmRecorridos = Integer.parseInt(separador[4]);
                arrAviones[largoLista] = new Avion(idAvion, modeloAvion, cantidadVuelos, cantidadAsientos, kmRecorridos);
                largoLista++;
            }
            lector.close();  // Cierra el archivo
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage()); //Error al hallar la ruta
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage()); //Error de lectura o Escritura
        }
    }

    // Cargar rutas desde un archivo .txt
    public static void cargarRutas(Ruta[] arrRutas) {

        String rutaArchivo = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\Rutas.txt";
        
        try {
            //BufferedReader permite leer línea por línea.
            //FileInputStream abre el archivo como bytes, InputStreamReader dice con qué codificación convertir esos bytes → caracteres.
            //"UTF-8" asegura que las tildes y ñ se lean bien.
            BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"));
            String linea;
            int largoLista = 0;

            while ((linea = lector.readLine()) != null && largoLista < arrRutas.length) {

                String[] separador = linea.split(";");
                String idRuta = separador[0];
                String ciudadOrigen = separador[1];
                String ciudadDestino = separador[2];
                int distancia = Integer.parseInt(separador[3]);
                String esInternacional = separador[4];
                arrRutas[largoLista] = new Ruta(idRuta, ciudadOrigen, ciudadDestino, distancia, esInternacional);
                largoLista++;
            }
            lector.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage()); //Error al hallar la ruta
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage()); //Error de lectura o Escritura
        }

    }
    
    // Cargar vuelos desde un archivo .txt
    public static void cargarVuelos(Vuelo[] arrVuelos, Avion[] arrAviones, Ruta[] arrRutas){
        String rutaArchivo = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\Vuelos.txt";
        
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"))) {  // try-with-resources
            String linea;
            int largoLista = 0;

            while ((linea = lector.readLine()) != null && largoLista < arrVuelos.length) {

                String[] separador = linea.split(";");
                String idVuelo = separador[0];
                String idAvion = separador[1];
                String idRuta = separador[2];
                String dia = separador[3];
                String hora = separador[4];

                // Buscar Avion por id
                Avion avionEncontrado = null;
                for (Avion a : arrAviones) {
                    if (a == null) continue; // Continue salta todo lo que queda dentro del for y pasa a la siguiente posición.
                    if (a.getIdAvion().equals(idAvion)) {
                        avionEncontrado = a;
                        break;
                    }
                }
                // Buscar Ruta por id
                Ruta rutaEncontrada = null;
                for (Ruta r : arrRutas) {
                    if (r == null) continue;// Continue salta todo lo que queda dentro del for y pasa a la siguiente posición.
                    if (r.getNumeroRuta().equals(idRuta)) {
                        rutaEncontrada = r;
                        break;
                    }
                }

                // Solo crear vuelo si existen avion y ruta
                if (avionEncontrado != null && rutaEncontrada != null) {
                    arrVuelos[largoLista++] = new Vuelo(idVuelo, avionEncontrado, rutaEncontrada, dia, hora);
                } else {
                    
                    System.out.println("Aviso: vuelo " + idVuelo + " ignorado (avión o ruta no encontrado).");
                }
            }
            // lector se cierra automáticamente aquí
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage()); //Error al hallar la ruta
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage()); //Error de lectura o Escritura
        }

    }

    public static void cargarCronograma(Vuelo[][] cronograma, Vuelo[] arrVuelos){
        System.out.println();
        for (Vuelo v : arrVuelos) {
            if (v == null) continue;
            int dia = Vuelo.posicionDia(v.getDia());
            int hora = Vuelo.posicionHora(v.getHora());
            if (dia < 0 || dia >= cronograma.length || hora < 0 || hora >= cronograma[0].length) {
                System.out.println("Aviso: vuelo " + v.getIdVuelo() + " con día/hora inválidos (" + v.getDia() + " " + v.getHora() + ")");
                continue;
            }
            if (cronograma[dia][hora] != null) {
                System.out.println("Aviso: Horario ocupado para Vuelo: " + v.getIdVuelo() + " en " + v.getDia() + " " + v.getHora());
                continue;
            }
            cronograma[dia][hora] = v;
        }
        System.out.println("\nEl CRONOGRAMA ha sido Actualizado.");
    }

}
