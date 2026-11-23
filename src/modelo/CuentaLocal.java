package modelo;

import java.util.ArrayList;

public class CuentaLocal {

    public static final double SALDO_MAXIMO = 10000.00;

    private String numeroCuenta;
    private double saldoDisponible;
    private double saldoReserva;
    private String fechaApertura;

    private Cliente titular;
    private ArrayList<Movimiento> movimientos;

    public CuentaLocal(String numeroCuenta, Cliente titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldoDisponible = 0;
        this.saldoReserva = 0;
        this.fechaApertura = Movimiento.fechaHoraActual().substring(0, 10);
        this.movimientos = new ArrayList<Movimiento>();
    }

    public void acreditar(double monto) {
        saldoDisponible = redondear(saldoDisponible + monto);
    }

    public boolean debitar(double monto) {
        if (monto <= 0 || monto > saldoDisponible) {
            return false;
        }
        saldoDisponible = redondear(saldoDisponible - monto);
        return true;
    }

    public boolean moverAReserva(double monto) {
        if (monto <= 0 || monto > saldoDisponible) {
            return false;
        }
        saldoDisponible = redondear(saldoDisponible - monto);
        saldoReserva = redondear(saldoReserva + monto);
        return true;
    }

    public boolean moverADisponible(double monto) {
        if (monto <= 0 || monto > saldoReserva) {
            return false;
        }
        saldoReserva = redondear(saldoReserva - monto);
        saldoDisponible = redondear(saldoDisponible + monto);
        return true;
    }

    public double obtenerSaldoTotal() {
        return redondear(saldoDisponible + saldoReserva);
    }

    public void agregarMovimiento(Movimiento movimiento) {
        movimientos.add(movimiento);
    }

    public static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public static String formatear(double valor) {
        int centavos = (int) Math.round(valor * 100);
        int enteros = centavos / 100;
        int decimales = centavos % 100;
        if (decimales < 10) {
            return enteros + ".0" + decimales;
        }
        return enteros + "." + decimales;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    public double getSaldoReserva() {
        return saldoReserva;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public Cliente getTitular() {
        return titular;
    }
}
