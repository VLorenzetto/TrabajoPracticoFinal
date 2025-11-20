import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

// NOTAS: Hacer modulo que muestre los dias disponibles del cronograma.
public class Sistema {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

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
                        cargarOtroAvion(arrAviones, sc);

                        break;
                    case 3:
                        cargarOtroVuelo(arrVuelos, arrAviones, arrRutas, sc);
                        cargarCronograma(cronograma, arrVuelos);

                        break;
                    case 4:
                        marcarRealizacionVuelo(arrVuelos, sc);

                        break;
                    case 5:
                        Ruta.imprimirArrRutas(arrRutas);
                        
                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

                        break;
                    case 9:

                        break;
                    case 10:

                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } while (opcion != 0);
        } // Scanner se cierra automáticamente.
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Sistema de Aereolinea ===");
        System.out.println("1) Cargar los Aviones, Rutas y Vuelos de los archios de texto.");
        System.out.println("2) Cargar nuevo Avion a la flota.");
        System.out.println("3) Cargar nuevo vuelo al cronograma.");
        System.out.println("4) Marcar la realización efectiva de un vuelo");
        System.out.println("5) Mostrar el promedio de pasajeros que efectivamente volaron.");
        System.out.println("6) Mostrar lista de vuelos para un dia específico.");
        System.out.println("7) Mostrar los datos de un avión dado.");
        System.out.println("8) Buscar vuelos en un rango limitado.");
        System.out.println("9) Buscar cantidad de horarios sin vuelos en la semana.");
        System.out.println("10) Buscar el primer vuelo internacional de cada día.");
        System.out.println("0) Salir");
    }

    private static int validarOpcion(Scanner sc, String msg) {
        System.out.print(msg);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Ingrese una referencia numérica valida: ");
        }
        int opcion = sc.nextInt();
        sc.nextLine(); // consumir el salto de línea restante
        return opcion;
    }

    // Cargar aviones desde un archivo .txt
    public static void cargarAviones(Avion[] arrAviones) {

        String rutaArchivo = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\Aviones.txt";

        try {
            // BufferedReader permite leer línea por línea.
            // FileInputStream abre el archivo como bytes, InputStreamReader dice con qué
            // codificación convertir esos bytes → caracteres.
            // "UTF-8" asegura que las tildes y ñ se lean bien.
            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"));
            String linea;
            int largoLista = 0;

            while ((linea = lector.readLine()) != null && largoLista < arrAviones.length) {

                String[] separador = linea.split(";");
                String idAvion = separador[0];
                String modeloAvion = separador[1];
                int cantidadVuelos = Integer.parseInt(separador[2]);
                int cantidadAsientos = Integer.parseInt(separador[3]);
                int kmRecorridos = Integer.parseInt(separador[4]);
                arrAviones[largoLista] = new Avion(idAvion, modeloAvion, cantidadVuelos, cantidadAsientos,
                        kmRecorridos);
                largoLista++;
            }
            lector.close(); // Cierra el archivo

        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage()); // Error al hallar la ruta
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage()); // Error de lectura o Escritura
        }
    }

    // Cargar rutas desde un archivo .txt
    public static void cargarRutas(Ruta[] arrRutas) {

        String rutaArchivo = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\Rutas.txt";

        try {
            // BufferedReader permite leer línea por línea.
            // FileInputStream abre el archivo como bytes, InputStreamReader dice con qué
            // codificación convertir esos bytes → caracteres.
            // "UTF-8" asegura que las tildes y ñ se lean bien.
            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"));
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
            System.out.println("Error: " + ex.getMessage()); // Error al hallar la ruta
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage()); // Error de lectura o Escritura
        }

    }

    // Cargar vuelos desde un archivo .txt
    public static void cargarVuelos(Vuelo[] arrVuelos, Avion[] arrAviones, Ruta[] arrRutas) {
        String rutaArchivo = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\Vuelos.txt";

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"))) {
            String linea;
            int largoLista = 0;

            while ((linea = lector.readLine()) != null && largoLista < arrVuelos.length) {
                String[] separador = linea.split(";");
                String idVuelo = separador[0];
                Avion avion = buscarAvion(arrAviones, separador[1]);
                Ruta ruta = buscarRuta(arrRutas, separador[2]);
                String dia = separador[3];
                String hora = separador[4];

                if (avion != null && ruta != null) {
                    Vuelo unVuelo = new Vuelo(idVuelo, avion, ruta, dia, hora);
                    unVuelo.setCantidadPasajeros(avion.getCantidadAsientos());
                    unVuelo.setAterrizaje(true);
                    arrVuelos[largoLista++] = unVuelo;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private static Avion buscarAvion(Avion[] arr, String idAvion) {
        for (Avion unAvion : arr)
            if (unAvion != null && unAvion.getIdAvion().equals(idAvion))
                return unAvion;
        return null;
    }

    private static Ruta buscarRuta(Ruta[] arrRutas, String id) {
        for (Ruta unaRuta : arrRutas)
            if (unaRuta != null && unaRuta.getNumeroRuta().equals(id))
                return unaRuta;
        return null;
    }

    public static void cargarCronograma(Vuelo[][] cronograma, Vuelo[] arrVuelos) {
        System.out.println();
        for (Vuelo unVuelo : arrVuelos) {
            if (unVuelo == null)
                continue;
            int dia = Vuelo.posicionDia(unVuelo.getDia());
            int hora = Vuelo.posicionHora(unVuelo.getHora());
            if (dia < 0 || dia >= cronograma.length || hora < 0 || hora >= cronograma[0].length) {
                System.out.println(
                        "Aviso: Vuelo " + unVuelo.getIdVuelo() + " con DIA/HORA inválidos (" + unVuelo.getDia() + " "
                                + unVuelo.getHora() + ")");
                continue;
            }
            if (cronograma[dia][hora] != null) {
                System.out.println(
                        "Aviso: Horario ocupado para Vuelo (" + unVuelo.getIdVuelo() + ") en " + unVuelo.getDia() + " "
                                + unVuelo.getHora());
                continue;
            }
            cronograma[dia][hora] = unVuelo;
        }
        System.out.println("\nEl CRONOGRAMA ha sido Actualizado.");
    }

    public static void cargarOtroAvion(Avion[] arrAviones, Scanner sc) {

        String continuar;
        do {
            // Carga y control de los datos del Avion.
            String idAvion = null;

            //Busca si el ID ya existe.
            while (idAvion != null && buscarAvion(arrAviones, idAvion) != null) {

                System.out.println("ID existente, intente nuevamente.");

                System.out.print("\nIngrese ID del AVIÓN: ");
                idAvion = validarString(sc);

                //Valida el formato del ID.
                while (!Avion.verificarIdAvion(idAvion)) {
                    System.out.print("Ingrese un ID válido: ");
                    idAvion = sc.nextLine();
                }
            }
            System.out.print("Ingrese MODELO del AVIÓN: ");
            String modeloAvion = validarString(sc);
            System.out.print("Ingrese CANTIDAD de VUELOS: ");
            int cantidadVuelos = validarInt(sc);
            System.out.print("Ingrese CANTIDAD de ASIENTOS: ");
            int cantidadAsientos = validarInt(sc);
            System.out.print("Ingrese los KILOMETROS RECORRIDOS: ");
            int kmRecorridos = validarInt(sc);

            // Se crea un avion de la clase Avion
            Avion nuevoAvion = new Avion(idAvion, modeloAvion, cantidadVuelos, cantidadAsientos, kmRecorridos);

            // Ingresa el nuevo Avion al arreglo existente.
            for (int i = 0; i < arrAviones.length; i++) {
                if (arrAviones[i] == null) {
                    arrAviones[i] = nuevoAvion;
                    break;
                }
            }

            System.out.println("\nAvión Cargadado Exitosamente.");
            System.out.print("¿Desea continuar cargando aviones al Sistema? s/n: ");
            continuar = validarString(sc);

        } while (continuar.equals("s") || continuar.equals("si"));

    }

    public static void cargarOtroVuelo(Vuelo[] arrVuelos, Avion[] arrAviones, Ruta[] arrRutas, Scanner sc) {
        String continuar;
        do {
            // Busca si existe el id del Vuelo ingresado.

            String idVuelo = null;
            boolean idVueloExiste = true;
            while (idVueloExiste) {

                System.out.print("\nIngrese ID del VUELO: ");
                idVuelo = validarString(sc);

                idVueloExiste = false;

                for (int i = 0; i < arrVuelos.length; i++)
                    if (arrVuelos[i] != null && idVuelo.equals((arrVuelos[i]).getIdVuelo())) {
                        System.out.println("ID del VUELO ya existe, intente nuevamente.");
                        idVueloExiste = true;

                        break;
                    }
            }

            // Busca si existe el id del Avion ingresado.
            Avion idAvionEncontrado = null;
            String idAvion = null;
            boolean idAvionExiste = false;
            while (!idAvionExiste) {

                System.out.print("\nIngrese ID del AVIÓN: ");
                idAvion = validarString(sc);

                while (!Avion.verificarIdAvion(idAvion)) {
                    System.out.print("Ingrese un ID Valido: ");
                    idAvion = sc.nextLine();
                }

                idAvionExiste = false;

                // Usar el módulo buscarAvion para obtener la referencia (si existe)
                idAvionEncontrado = buscarAvion(arrAviones, idAvion);
                idAvionExiste = (idAvionEncontrado != null);
                if (!idAvionExiste) {
                    System.out.println("ID del AVIÓN inexistente, intente nuevamente.");
                }
            }

            // Busca si existe el id de la Ruta ingresada.
            Ruta idRutaEncontrado = null;
            String idRuta = null;
            boolean idRutaExiste = true;
            while (!idRutaExiste) {

                System.out.print("\nIngrese ID de la RUTA: ");
                idRuta = validarString(sc);

                idRutaExiste = false;

                idRutaEncontrado = buscarRuta(arrRutas, idRuta);
                idRutaExiste = (idAvionEncontrado != null);
                if (!idRutaExiste) {
                    System.out.println("ID de la RUTA inexistente, intente nuevamente.");
                }
            }

            System.out.print("Ingrese el DIA para programar el vuelo (" + idVuelo + "): ");
            String dia = validarString(sc);

            System.out.print("Ingrese la HORA para programar el vuelo (" + idVuelo + "): ");
            String hora = validarString(sc);

            // Guarda el vuelo un la primer posicion vacia del arreglo.
            boolean vueloGuardado = false;
            for (int i = 0; i < arrVuelos.length; i++) {
                if (arrVuelos[i] == null) {
                    arrVuelos[i] = new Vuelo(idVuelo, idAvionEncontrado, idRutaEncontrado, dia, hora);

                    // Actualiza la cantidad de pasajeros acorde a la cantidad de asientos del avión.
                    arrVuelos[i].setCantidadPasajeros(idAvionEncontrado.getCantidadAsientos());
                    vueloGuardado = true;
                    break;
                }
            }

            if (!vueloGuardado) {
                System.out.println("No hay espacios disponibles en el arreglo de Vuelos.");
            }

            System.out.println("\nVuelo Cargadado Exitosamente.");
            System.out.print("¿Desea continuar cargando vuelos al Sistema? s/n: ");
            continuar = validarString(sc);
        } while (continuar.equals("s") || continuar.equals("si"));
    }

    public static void marcarRealizacionVuelo(Vuelo[] arrVuelos, Scanner sc) {
        String continuar;
        do {
            // Busca si existe el id del Vuelo ingresado.
            String idVuelo = null;
            boolean idVueloExiste = false;
            while (!idVueloExiste) {

                System.out.print("\nIngrese el ID del Vuelo: ");
                idVuelo = validarString(sc);

                idVueloExiste = false;
                for (int i = 0; i < arrVuelos.length; i++) {
                    if (arrVuelos[i] != null && idVuelo.equals(arrVuelos[i].getIdVuelo())) {
                        idVueloExiste = true;
                        break;
                    }
                }
                if (!idVueloExiste) {
                    System.out.println("ID del Vuelo inexistente, intente nuevamente.");
                }
            }

            // Marcar el vuelo como realizado
            for (Vuelo unVuelo : arrVuelos) {
                if (unVuelo == null)
                    continue;
                if (unVuelo.getIdVuelo().equals(idVuelo)) {
                    unVuelo.marcarComoRealizado();
                    System.out.println("\nVuelo marcado como finalizado.");
                    break;
                }
            }

            System.out.print("¿Desea marcar la realización de otro vuelo? s/n: ");
            continuar = validarString(sc);
        } while (continuar.equals("si") || continuar.equals("s"));
    }

    public static int validarInt(Scanner sc) {
        while (!sc.hasNextInt()) { // .hasNextInt() Devuelve true si lo próximo que el usuario ingresó es un entero válido.
            sc.next(); // Evita bucle en el while.
            System.out.print("Ingrese un Número Válido: ");
        }
        int entero = sc.nextInt();
        sc.nextLine();
        return entero;
    }

    public static String validarString(Scanner sc) {
        String cadena = sc.nextLine().trim(); // Quita los espacios de los extremos
        cadena = cadena.replaceAll("\\s+", " "); // Colapsa espacios internos

        while (cadena.isEmpty()) { // .isEmpty devuelve true cuando la cadena está vacía.
            System.out.print("Ingrese una Cadena Válida: ");
            cadena = sc.nextLine().trim();
            cadena = cadena.replaceAll("\\s+", " ");
        }
        return cadena;
    }

}
