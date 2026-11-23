package interfaz;

import java.util.ArrayList;
import java.util.Scanner;
import modelo.Cliente;
import modelo.CuentaLocal;
import modelo.Gasto;
import negocio.GestorClientes;
import negocio.GestorConciliacion;
import negocio.GestorDepositos;
import negocio.GestorGastos;

public class Main {

    static Scanner teclado = new Scanner(System.in);
    static GestorClientes gestorClientes = new GestorClientes();
    static GestorDepositos gestorDepositos = new GestorDepositos();
    static GestorGastos gestorGastos = new GestorGastos();
    static GestorConciliacion gestorConciliacion = new GestorConciliacion();

    public static void main() {
        menuInicial();
    }

    static void menuInicial() {
        int opcion;
        do {
            titulo("EL BANQUILLO - Tu dinero...sin conexión?");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Desbloquear cuenta con PIN");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 3:
                    desbloquearCuenta();
                    break;
                case 0:
                    System.out.println("Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    static void registrarCliente() {
        titulo("REGISTRO DE CLIENTE");
        String nombre = leerTextoNoVacio("Nombre completo: ");
        String identificacion = leerTextoNoVacio("Identificación: ");
        String correo = leerTextoNoVacio("Correo electrónico: ");
        String contrasena = leerTextoNoVacio("Contraseña (mínimo 8 caracteres): ");
        String pin = leerTextoNoVacio("PIN de seguridad (4 dígitos): ");

        gestorClientes.registrarCliente(nombre, identificacion, correo, contrasena, pin);
        System.out.println(gestorClientes.getMensaje());
    }

    static void iniciarSesion() {
        titulo("INICIAR SESIÓN");
        String correo = leerTextoNoVacio("Correo electrónico: ");
        String contrasena = leerTextoNoVacio("Contraseña: ");

        Cliente cliente = gestorClientes.iniciarSesion(correo, contrasena);
        System.out.println(gestorClientes.getMensaje());
        if (cliente != null) {
            menuCuenta(cliente);
        }
    }

    static void desbloquearCuenta() {
        titulo("DESBLOQUEAR CUENTA");
        String correo = leerTextoNoVacio("Correo electrónico: ");
        String pin = leerTextoNoVacio("PIN de seguridad: ");

        gestorClientes.desbloquearCuenta(correo, pin);
        System.out.println(gestorClientes.getMensaje());
    }

    static void menuCuenta(Cliente cliente) {
        CuentaLocal cuenta = cliente.getCuenta();
        int opcion;
        do {
            titulo("CUENTA " + cuenta.getNumeroCuenta() + " - " + cliente.getNombreCompleto());
            mostrarSaldos(cuenta);
            System.out.println("1. Depósito y reserva de dinero");
            System.out.println("2. Gastos sin conexión");
            System.out.println("3. Conciliación y reportes");

            System.out.println("0. Cerrar sesión");
            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1:
                    menuDepositos(cuenta);
                    break;
                case 2:
                    menuGastos(cuenta);
                    break;
                case 3:
                    menuConciliacion(cuenta);
                    break;
                case 0:
                    System.out.println("Sesión cerrada.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    static void menuDepositos(CuentaLocal cuenta) {
        int opcion;
        do {
            titulo("DEPÓSITO Y RESERVA DE DINERO");
            mostrarSaldos(cuenta);
            System.out.println("1. Guardar dinero (depósito)");
            System.out.println("2. Reservar dinero intocable");
            System.out.println("3. Liberar dinero de la reserva");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1:
                    guardarDinero(cuenta);
                    break;
                case 2:
                    reservarDinero(cuenta);
                    break;
                case 3:
                    liberarDinero(cuenta);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    static void guardarDinero(CuentaLocal cuenta) {
        double monto = leerMonto("Monto a guardar: $");
        String origen = leerTexto("Origen del dinero: ");
        String descripcion = leerTexto("Descripción: ");

        gestorDepositos.guardarDeposito(cuenta, monto, origen, descripcion);
        System.out.println(gestorDepositos.getMensaje());
    }

    static void reservarDinero(CuentaLocal cuenta) {
        double monto = leerMonto("Monto a reservar: $");
        String pin = leerTextoNoVacio("PIN de seguridad: ");

        gestorDepositos.reservarDinero(cuenta, monto, pin);
        System.out.println(gestorDepositos.getMensaje());
    }

    static void liberarDinero(CuentaLocal cuenta) {
        double monto = leerMonto("Monto a liberar: $");
        String pin = leerTextoNoVacio("PIN de seguridad: ");

        gestorDepositos.liberarReserva(cuenta, monto, pin);
        System.out.println(gestorDepositos.getMensaje());
    }

    static void menuGastos(CuentaLocal cuenta) {
        int opcion;
        do {
            titulo("GASTOS SIN CONEXIÓN");
            System.out.println("Saldo disponible: $" + CuentaLocal.formatear(cuenta.getSaldoDisponible()));
            System.out.println("1. Registrar gasto");
            System.out.println("2. Consultar gastos");
            System.out.println("3. Anular gasto reciente");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1:
                    registrarGasto(cuenta);
                    break;
                case 2:
                    consultarGastos(cuenta);
                    break;
                case 3:
                    anularGasto(cuenta);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    static void registrarGasto(CuentaLocal cuenta) {
        double monto = leerMonto("Monto del gasto: $");
        String comercio = leerTextoNoVacio("Concepto o comercio: ");
        String categoria = elegirCategoria(false);

        gestorGastos.registrarGasto(cuenta, monto, comercio, categoria);
        System.out.println(gestorGastos.getMensaje());
    }

    static void consultarGastos(CuentaLocal cuenta) {
        String categoria = elegirCategoria(true);
        String estado = elegirEstado();

        ArrayList<Gasto> gastos = gestorGastos.consultarGastos(cuenta, categoria, estado);
        mostrarGastos(gastos);
    }

    static void anularGasto(CuentaLocal cuenta) {
        ArrayList<Gasto> gastos = gestorGastos.consultarGastos(cuenta, "", "");
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos registrados.");
            return;
        }
        mostrarGastos(gastos);
        int numero = leerEntero("Número del gasto a anular (0 para cancelar): ");
        if (numero == 0) {
            return;
        }
        if (numero < 1 || numero > gastos.size()) {
            System.out.println("Número de gasto no válido.");
            return;
        }
        gestorGastos.anularGasto(cuenta, gastos.get(numero - 1));
        System.out.println(gestorGastos.getMensaje());
    }

    static String elegirCategoria(boolean permitirTodas) {
        System.out.println("Categorías:");
        for (int i = 0; i < GestorGastos.CATEGORIAS.length; i++) {
            System.out.println((i + 1) + ". " + GestorGastos.CATEGORIAS[i]);
        }
        if (permitirTodas) {
            System.out.println("0. Todas");
        }
        while (true) {
            int opcion = leerEntero("Categoría: ");
            if (permitirTodas && opcion == 0) {
                return "";
            }
            if (opcion >= 1 && opcion <= GestorGastos.CATEGORIAS.length) {
                return GestorGastos.CATEGORIAS[opcion - 1];
            }
            System.out.println("Categoría no válida.");
        }
    }

    static String elegirEstado() {
        System.out.println("Estado: 0. Todos   1. EJECUTADO   2. ANULADO");
        while (true) {
            int opcion = leerEntero("Estado: ");
            if (opcion == 0) {
                return "";
            }
            if (opcion == 1) {
                return Gasto.EJECUTADO;
            }
            if (opcion == 2) {
                return Gasto.ANULADO;
            }
            System.out.println("Estado no válido.");
        }
    }

    static void mostrarGastos(ArrayList<Gasto> gastos) {
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos que coincidan con el filtro.");
            return;
        }
        System.out.println();
        for (int i = 0; i < gastos.size(); i++) {
            Gasto g = gastos.get(i);
            System.out.println((i + 1) + ". " + g.getFechaHora()
                    + " | " + g.getId()
                    + " | " + g.getComercio()
                    + " | " + g.getCategoria()
                    + " | $" + CuentaLocal.formatear(g.getMonto())
                    + " | " + g.getEstado());
        }
    }

    static void menuConciliacion(CuentaLocal cuenta) {
        int opcion;
        do {
            titulo("CONCILIACIÓN Y REPORTES");
            System.out.println("1. Conciliar saldos de la cuenta");
            System.out.println("2. Generar reporte por fechas");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1:
                    conciliarSaldos(cuenta);
                    break;
                case 2:
                    generarReporte(cuenta);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    static void conciliarSaldos(CuentaLocal cuenta) {
        double diferencia = gestorConciliacion.conciliarSaldos(cuenta);
        if (diferencia == 0) {
            System.out.println("Saldo conciliado correctamente.");
        } else {
            System.out.println("Alerta: se detectó una diferencia de $"
                    + CuentaLocal.formatear(Math.abs(diferencia)) + " en los saldos.");
        }
    }

    static void generarReporte(CuentaLocal cuenta) {
        String inicio = leerFecha("Fecha de inicio (aaaa-mm-dd): ");
        String fin = leerFecha("Fecha de fin (aaaa-mm-dd): ");

        System.out.println();
        System.out.println(gestorConciliacion.generarReportePorFechas(cuenta, inicio, fin));
    }


    static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    static String leerTextoNoVacio(String mensaje) {
        String texto = leerTexto(mensaje);
        while (texto.isEmpty()) {
            System.out.println("Este campo no puede estar vacío.");
            texto = leerTexto(mensaje);
        }
        return texto;
    }

    static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    static double leerMonto(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).replace(',', '.');
            try {
                double valor = Double.parseDouble(texto);
                if (!Double.isNaN(valor) && !Double.isInfinite(valor)) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // no hace nada: se muestra el mensaje de abajo y se vuelve a preguntar
            }
            System.out.println("Ingrese un monto numérico válido (ejemplo: 25.50).");
        }
    }

    static String leerFecha(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (fechaValida(texto)) {
                return texto;
            }
            System.out.println("Fecha inválida. Use el formato aaaa-mm-dd (ejemplo: 2026-11-05).");
        }
    }

    static boolean fechaValida(String texto) {
        if (texto.length() != 10 || texto.charAt(4) != '-' || texto.charAt(7) != '-') {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            if (i != 4 && i != 7 && !Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        int anio = Integer.parseInt(texto.substring(0, 4));
        int mes = Integer.parseInt(texto.substring(5, 7));
        int dia = Integer.parseInt(texto.substring(8, 10));
        return anio >= 1970 && mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31;
    }

    static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    static void mostrarSaldos(CuentaLocal cuenta) {
        System.out.println("Saldo disponible: $" + CuentaLocal.formatear(cuenta.getSaldoDisponible()));
        System.out.println("Saldo en reserva: $" + CuentaLocal.formatear(cuenta.getSaldoReserva()));
        System.out.println("Saldo total:      $" + CuentaLocal.formatear(cuenta.obtenerSaldoTotal()));
    }
}