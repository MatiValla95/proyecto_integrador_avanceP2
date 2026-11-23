package negocio;

import modelo.CuentaLocal;
import modelo.Deposito;

public class GestorDepositos {

    private int contador;
    private String mensaje;

    public GestorDepositos() {
        contador = 0;
        mensaje = "";
    }

    public boolean guardarDeposito(CuentaLocal cuenta, double monto,
                                   String origen, String descripcion) {
        monto = CuentaLocal.redondear(monto);

        if (monto <= 0) {
            mensaje = "Error: el monto debe ser mayor a cero.";
            return false;
        }
        if (cuenta.obtenerSaldoTotal() + monto > CuentaLocal.SALDO_MAXIMO) {
            mensaje = "Error: el depósito supera el saldo máximo permitido en la cuenta ($10.000).";
            return false;
        }
        if (origen.trim().isEmpty()) {
            origen = "No especificado";
        }
        if (descripcion.trim().isEmpty()) {
            descripcion = "Sin descripción";
        }

        contador++;
        Deposito deposito = new Deposito("DEP-" + contador, monto, origen, descripcion);
        if (!deposito.registrarDeposito()) {
            mensaje = "Error: no se pudo registrar el depósito.";
            return false;
        }
        cuenta.acreditar(monto);
        cuenta.agregarMovimiento(deposito);

        mensaje = "Dinero guardado correctamente. Saldo disponible: $"
                + CuentaLocal.formatear(cuenta.getSaldoDisponible()) + ".";
        return true;
    }

    public boolean reservarDinero(CuentaLocal cuenta, double monto, String pin) {
        monto = CuentaLocal.redondear(monto);

        if (!cuenta.getTitular().desbloquearConPin(pin)) {
            mensaje = "Error: PIN incorrecto.";
            return false;
        }
        if (monto <= 0) {
            mensaje = "Error: el monto debe ser mayor a cero.";
            return false;
        }
        if (!cuenta.moverAReserva(monto)) {
            mensaje = "Error: saldo disponible insuficiente.";
            return false;
        }
        mensaje = "Reserva actualizada. Saldo en reserva: $"
                + CuentaLocal.formatear(cuenta.getSaldoReserva()) + ".";
        return true;
    }

    public boolean liberarReserva(CuentaLocal cuenta, double monto, String pin) {
        monto = CuentaLocal.redondear(monto);

        if (!cuenta.getTitular().desbloquearConPin(pin)) {
            mensaje = "Error: PIN incorrecto.";
            return false;
        }
        if (monto <= 0) {
            mensaje = "Error: el monto debe ser mayor a cero.";
            return false;
        }
        if (!cuenta.moverADisponible(monto)) {
            mensaje = "Error: saldo en reserva insuficiente.";
            return false;
        }
        mensaje = "Reserva actualizada. Saldo en reserva: $"
                + CuentaLocal.formatear(cuenta.getSaldoReserva()) + ".";
        return true;
    }

    public String getMensaje() {
        return mensaje;
    }
}
