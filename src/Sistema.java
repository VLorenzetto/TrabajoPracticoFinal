import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

//NOTAS: Agragar controlador para cuando todas las rutas sean verdaderas de un aviso
public class Sistema {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            // Designar ubicaciones de las rutas con los archivos txt.
            String rutaTxtAviones = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\src\\Aviones.txt";
            String rutaTxtRutas = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\src\\Rutas.txt";
            String rutaTxtVuelos = "C:\\Users\\valen\\OneDrive\\Desktop\\TrabajoPrácticoFinal\\src\\Vuelos.txt";

            // Declaracion de Arreglos y Matrices
            // Calculo es tamaño del arreglo de acuerdo al largo de la lista + 20 espacios
            // para operar.
            Avion[] arrAviones = new Avion[calcularLargoLista(rutaTxtAviones) + 20];
            Ruta[] arrRutas = new Ruta[calcularLargoLista(rutaTxtRutas) + 20];
            Vuelo[] arrVuelos = new Vuelo[calcularLargoLista(rutaTxtVuelos) + 20];
            Vuelo[][] cronograma = new Vuelo[7][15]; // 7 dias de la semana x 15 horas habiles del aereopuerto.

            int opcion;
            do {
                mostrarMenu();
                opcion = validarOpcion(sc, "Opcion: ");

                switch (opcion) {
                    case 1:
                        cargarAviones(arrAviones, rutaTxtAviones);
                        cargarRutas(arrRutas, rutaTxtRutas);
                        cargarVuelos(arrVuelos, arrAviones, arrRutas, rutaTxtVuelos);
                        System.out.println("\nDatos EXTRAIDOS y ALMACENADOS Correctamente."); // Corregir segun notas
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
                        promedioPasajeros(arrVuelos, 0, 0, 0);

                        break;
                    case 6:
                        
                        Vuelo.imprimirArrVuelos(ordenarPorKm(listaVuelosPorDia(cronograma, sc)));

                        break;
                    case 7:
                        mostrarDatosAvion(arrAviones, sc);

                        break;
                    case 8:
                        Vuelo.imprimirArrVuelos(buscarVuelosEnRango(arrVuelos, sc));

                        break;
                    case 9:
                        int diasLibres = cantidadLibres(cronograma); // contar si hay libres
                        System.out.println("\nHORARIOS DISPONIBLES: (" + diasLibres + ")\n");
                        mostrarHorariosLibres(cronograma, 0, 0);

                        if (diasLibres == 0) {
                            System.out.println("No hay horarios disponibles.");
                        }

                        break;
                    case 10:
                        Vuelo.imprimirArrVuelos(primerVueloInterPorDia(cronograma));

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
        System.out.println("2) Cargar nuevo Avion al sistema.");
        System.out.println("3) Cargar nuevo vuelo al cronograma.");
        System.out.println("4) Marcar la realización efectiva de un vuelo");
        System.out.println("5) Mostrar el promedio de pasajeros que efectivamente volaron.");
        System.out.println("6) Mostrar lista de vuelos para un dia específico (orden ascendente).");
        System.out.println("7) Mostrar los datos de un avión.");
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
    public static void cargarAviones(Avion[] arrAviones, String rutaTxtAviones) {

        try {
            // BufferedReader permite leer línea por línea.
            // FileInputStream abre el archivo como bytes, InputStreamReader dice con qué
            // codificación convertir esos bytes → caracteres.
            // "UTF-8" asegura que las tildes y ñ se lean bien.
            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(new FileInputStream(rutaTxtAviones), "UTF-8"));
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
    public static void cargarRutas(Ruta[] arrRutas, String rutaTxtRutas) {

        try {
            // BufferedReader permite leer línea por línea.
            // FileInputStream abre el archivo como bytes, InputStreamReader dice con qué
            // codificación convertir esos bytes → caracteres.
            // "UTF-8" asegura que las tildes y ñ se lean bien.
            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(new FileInputStream(rutaTxtRutas), "UTF-8"));
            String linea;
            int largoLista = 0;

            while ((linea = lector.readLine()) != null && largoLista < arrRutas.length) {

                // Separa la cadena cuando lee ";" y guarda cada subcadena en una posicion del
                // arr diferente.
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
    public static void cargarVuelos(Vuelo[] arrVuelos, Avion[] arrAviones, Ruta[] arrRutas, String rutaTxtVuelos) {

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaTxtVuelos), "UTF-8"))) {
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
            String idAvion;

            // Pide y valida el ID hasta comprobar que no exista dentro del sistema.
            do {
                System.out.print("\nIngrese ID del AVIÓN: ");
                idAvion = validarString(sc);

                // Valida formato
                while (!Avion.verificarIdAvion(idAvion)) {
                    System.out.print("Ingrese un ID válido: ");
                    idAvion = validarString(sc);
                }

                if (buscarAvion(arrAviones, idAvion) != null) {
                    System.out.println("ID existente, intente nuevamente.");
                }

            } while (buscarAvion(arrAviones, idAvion) != null);

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
            continuar = validarString(sc).toLowerCase();

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
                    idAvion = validarString(sc);
                }

                idAvionExiste = false;

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
            String dia = validarString(sc).toLowerCase();

            System.out.print("Ingrese la HORA para programar el vuelo (" + idVuelo + "): ");
            String hora = validarString(sc).toLowerCase();

            // Guarda el vuelo un la primer posicion vacia del arreglo.
            boolean vueloGuardado = false;
            for (int i = 0; i < arrVuelos.length; i++) {
                if (arrVuelos[i] == null) {
                    arrVuelos[i] = new Vuelo(idVuelo, idAvionEncontrado, idRutaEncontrado, dia, hora);

                    // Actualiza la cantidad de pasajeros acorde a la cantidad de asientos del
                    // avión.
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
            continuar = validarString(sc).toLowerCase();
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
            continuar = validarString(sc).toLowerCase();
        } while (continuar.equals("si") || continuar.equals("s"));
    }

    public static double promedioPasajeros(Vuelo[] arrVuelos, int i, int suma, int cant) {
        double resultado;

        if (i == arrVuelos.length) {
            // Caso base: si no hubo ningún vuelo válido, devolvemos 0 para evitar división
            // por cero, sino hacemos el promedio de los ultimos datos guardados.
            resultado = cant == 0 ? 0 : (double) suma / cant;
            System.out.println("\nEl promedio de pasajeros que efectivamente volaron es: " + resultado);
        } else {
            // Paso recursivo: si la posicion esta vacia y el vuelo efectivamente realizó el
            // aterrizaje, suma la cantidad de pasajeros con la ultima guardada y suma 1 al
            // contador de aviones.
            if (arrVuelos[i] != null && arrVuelos[i].getAterrizaje() != false) {
                suma += arrVuelos[i].getCantidadPasajeros();
                cant++;
            }
            // Llamada recursiva.
            resultado = promedioPasajeros(arrVuelos, i + 1, suma, cant);
        }

        return resultado;
    }

   public static Vuelo[] listaVuelosPorDia(Vuelo[][] cronograma, Scanner sc) {

        String dia;
        do {
            System.out.print("\nIngrese el DIA para buscar vuelos: ");
            dia = validarString(sc).toLowerCase();

            if (!dia.equals("lunes") && !dia.equals("martes") && !dia.equals("miercoles") &&
                !dia.equals("jueves") && !dia.equals("viernes") &&
                !dia.equals("sabado") && !dia.equals("domingo")) {

                System.out.println("Ingrese un dia valido.");
                dia = null;
            }

        } while (dia == null);

        // Buscar posición dentro de cronograma
        int posDia = Vuelo.posicionDia(dia);

        // Contar vuelos no nulos.
        int cantidad = 0;
        for (int i = 0; i < cronograma[posDia].length; i++) {
            if (cronograma[posDia][i] != null) {
                cantidad++;
            }
        }

        // Si no hay vuelos para ese día devuelve arreglo vacío.
        if (cantidad == 0) {
            System.out.println("\nNo hay vuelos disponibles el dia "+ nombreDia(posDia)+".");
            return new Vuelo[0];
        }

        // Copiar solo los vuelos válidos.
        Vuelo[] vuelos = new Vuelo[cantidad];
        int k = 0;

        for (int i = 0; i < cronograma[posDia].length; i++) {
            if (cronograma[posDia][i] != null) {
                vuelos[k++] = cronograma[posDia][i];
            }
        }

        return vuelos;
    }

    public static Vuelo[] ordenarPorKm(Vuelo[] arrVuelos) {

        // Utilicé el metodo BURBUJA MEJORADO.
        int i = 0;
        boolean ordenado = false;
        Vuelo aux;

        while (i < arrVuelos.length - 1 && !ordenado) { //
            ordenado = true;

            // Limita el arreglo para verificar siempre de a pares en cada pasada.
            for (int j = 0; j <= arrVuelos.length - 2 - i; j++) {

                // Verifica que las posiciones consecutivas que se van a comparar no sean null.
                if (arrVuelos[j] == null || arrVuelos[j + 1] == null) {
                    continue; // salta si las posiciones estan vacías
                }

                // Guarda la posicion actual y su consecutiva.
                int distActual = arrVuelos[j].getDistancia();
                int distSiguiente = arrVuelos[j + 1].getDistancia();

                // Compara las posciones para ver si la consecutiva es menor a la actual.
                if (distSiguiente < distActual) {
                    ordenado = false; // Da la orden para que el bucle principal siga.

                    // Guarda la posicion actual en un arr aux y despues la intercambia con la
                    // consecutiva
                    aux = arrVuelos[j];
                    arrVuelos[j] = arrVuelos[j + 1];
                    arrVuelos[j + 1] = aux;
                }
            }

            i++;
        }

        return arrVuelos; // Retorna el arr con los vuelos ordenados de forma ascendente.
    }

    public static void mostrarDatosAvion(Avion[] arrAviones, Scanner sc) {
        // Busca si existe el id del Avion ingresado.
        Avion idAvionEncontrado = null;
        String idAvion = null;
        boolean idAvionExiste = false;
        while (!idAvionExiste) {

            System.out.print("\nIngrese ID del AVIÓN: ");
            idAvion = validarString(sc);

            while (!Avion.verificarIdAvion(idAvion)) {
                System.out.print("Ingrese un ID Valido: ");
                idAvion = validarString(sc);
            }

            idAvionExiste = false;

            idAvionEncontrado = buscarAvion(arrAviones, idAvion);
            idAvionExiste = (idAvionEncontrado != null);
            if (!idAvionExiste) {
                System.out.println("ID del AVIÓN inexistente, intente nuevamente.");
            }
        }
        System.out.println(idAvionEncontrado.toString());
    }

    public static Vuelo[] buscarVuelosEnRango(Vuelo[] arrVuelos, Scanner sc) {
        Vuelo[] vuelosOrdenados = ordenarPorKm(arrVuelos);

        System.out.print("Ingrese distancia Minima: ");
        int kmMin = validarInt(sc);

        System.out.print("Ingrese distancia Maxima: ");
        int kmMax = validarInt(sc);

        int ini = 0, fin = vuelosOrdenados.length, medio;

        // Busqueda binaria para primer índice con km >= kmMin
        while (ini < fin) {
            medio = (ini + fin) / 2;
            if (vuelosOrdenados[medio].getDistancia() >= kmMin)
                fin = medio;
            else
                ini = medio + 1;
        }
        int desde = ini;

        // Busqueda binaria para primer índice con km > kmMax
        ini = desde;
        fin = vuelosOrdenados.length;
        while (ini < fin) {
            medio = (ini + fin) / 2;
            if (vuelosOrdenados[medio].getDistancia() > kmMax)
                fin = medio;
            else
                ini = medio + 1;
        }
        int hasta = ini;

        int cantidad = hasta - desde;

        Vuelo[] vuelosEnRango;
        if (cantidad <= 0) {
            System.out.println("\nNo hay vuelos en el rango seleccionado.");
            vuelosEnRango = new Vuelo[0]; // nunca null
        } else {
            vuelosEnRango = new Vuelo[cantidad];
            for (int i = 0; i < cantidad; i++) {
                vuelosEnRango[i] = vuelosOrdenados[desde + i];
            }
        }

        return vuelosEnRango;
    }

    public static void mostrarHorariosLibres(Vuelo[][] cronograma, int dia, int hora) {
        int sigDia;
        int sigHora;

        // Caso Base
        if (dia == cronograma.length) {
            sigDia = dia;
            sigHora = hora;

            // Paso recursivo
        } else {
            // calcular siguiente casillero
            sigDia = dia;
            sigHora = hora + 1;
            if (sigHora == cronograma[dia].length) {
                sigDia = dia + 1;
                sigHora = 0;
            }

            // si este horario está libre, lo mostramos
            if (cronograma[dia][hora] == null) {

                String nombreDia = nombreDia(dia); // ← módulo separado

                int horaReal = 8 + hora; // 0 → 08:00, 1 → 09:00, etc.
                String horaTexto = String.format("%02d:00", horaReal);

                System.out.println(nombreDia + " - " + horaTexto);
            }

            // Llamada recursiva.
            mostrarHorariosLibres(cronograma, sigDia, sigHora);
        }

        return;
    }

    public static int cantidadLibres(Vuelo[][] cronograma) {
        int cont = 0;
        for (int dia = 0; dia < cronograma.length; dia++) {
            for (int hora = 0; hora < cronograma[dia].length; hora++) {
                if (cronograma[dia][hora] == null)
                    cont++;
            }
        }
        return cont;
    }

    public static Vuelo[] primerVueloInterPorDia(Vuelo[][] cronograma) {
        System.out.println();// Deja un linea de espacio despues del menú.

        Vuelo[] temp = new Vuelo[cronograma.length]; // como mucho 1 por día
        int k = 0;

        for (int i = 0; i < cronograma.length; i++) {
            boolean encontrado = false;

            for (int j = 0; j < cronograma[i].length && !encontrado; j++) {
                Vuelo v = cronograma[i][j];
                if (v != null && v.getEsInternacional()) {
                    temp[k] = v; // lo guardo compactado
                    k++;
                    encontrado = true; // ya tengo el primero de este día
                }
            }

            if (!encontrado) {
                System.out.println("No hay vuelos internacionales el día " + nombreDia(i));
            }
        }

        if (k == 0) { // ningún vuelo internacional encontrado
            return new Vuelo[0]; // arreglo vacío, nunca null
        }

        Vuelo[] resultado = new Vuelo[k]; // solo con los encontrados
        for (int i = 0; i < k; i++) {
            resultado[i] = temp[i];
        }

        return resultado;
    }

    public static String nombreDia(int dia) {
        String nombreDia;

        switch (dia) {
            case 0:
                nombreDia = "Lunes";
                break;
            case 1:
                nombreDia = "Martes";
                break;
            case 2:
                nombreDia = "Miércoles";
                break;
            case 3:
                nombreDia = "Jueves";
                break;
            case 4:
                nombreDia = "Viernes";
                break;
            case 5:
                nombreDia = "Sábado";
                break;
            case 6:
                nombreDia = "Domingo";
                break;
            default:
                nombreDia = "Día " + dia;
        }

        return nombreDia;
    }

    public static int calcularLargoLista(String rutaArchivo) {
        int largoLista = 0;

        try {
            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"));

            String linea;

            while ((linea = lector.readLine()) != null) {
                if (!linea.trim().isEmpty()) { // solo cuenta si la línea tiene texto real

                    largoLista++;
                }
            }

            lector.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }

        return largoLista;
    }

    public static int validarInt(Scanner sc) {
        // .hasNextInt() Devuelve true si lo próximo que el usuario ingresó es un entero
        // válido.
        while (!sc.hasNextInt()) {
            sc.next(); // Evita bucle en el while.
            System.out.print("Ingrese un Número Válido: ");
        }
        int entero = sc.nextInt();
        sc.nextLine();
        return entero;
    }

    public static String validarString(Scanner sc) {
        String cadena = sc.nextLine().trim().replaceAll("\\s+", " ");
        // .trim() Quita los espacios de los extremos.
        // .replaceAll("\\s+", " ") colapsa todos los espacios internos.

        while (cadena.isEmpty()) { // .isEmpty devuelve true cuando la cadena está vacía.
            System.out.print("Ingrese una Cadena Válida: ");
            cadena = sc.nextLine().trim().replaceAll("\\s+", " ");
        }

        return cadena;
    }

}
