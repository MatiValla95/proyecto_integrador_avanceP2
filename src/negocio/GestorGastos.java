package negocio;

import java.util.ArrayList;
import modelo.CuentaLocal;
import modelo.Gasto;
import modelo.Movimiento;

public class GestorGastos {

    public static final String[] CATEGORIAS = {
        "Alimentación", "Transporte", "Salud", "Servicios", "Entretenimiento", "Otros"
    };

    private int contador;
    private String mensaje;

    public GestorGastos() {
        contador = 0;
        mensaje = "";
    }

    public boolean registrarGasto(CuentaLocal cuenta, double monto,
                                  String comercio, String categoria) {
        monto = CuentaLocal.redondear(monto);

        if (monto <= 0) {
            mensaje = "Error: el monto debe ser mayor a cero.";
            return false;
        }
        if (monto > cuenta.getSaldoDisponible()) {
            mensaje = "Error: saldo insuficiente.";
            return false;
        }

        contador++;
        Gasto gasto = new Gasto("GAS-" + contador, monto, comercio, categoria);
        if (!gasto.registrarGasto()) {
            mensaje = "Error: los datos del gasto no son válidos.";
            return false;
        }
        cuenta.debitar(monto);
        cuenta.agregarMovimiento(gasto);

        mensaje = "Gasto registrado. Saldo disponible: $"
                + CuentaLocal.formatear(cuenta.getSaldoDisponible()) + ".";
        return true;
    }

    public ArrayList<Gasto> consultarGastos(CuentaLocal cuenta, String categoria, String estado) {
        ArrayList<Gasto> resultado = new ArrayList<Gasto>();
        for (Movimiento m : cuenta.getMovimientos()) {
            if (m instanceof Gasto) {
                Gasto g = (Gasto) m;
                boolean coincideCategoria = categoria.isEmpty() || g.getCategoria().equals(categoria);
                boolean coincideEstado = estado.isEmpty() || g.getEstado().equals(estado);
                if (coincideCategoria && coincideEstado) {
                    resultado.add(g);
                }
            }
        }
        return resultado;
    }

    public boolean anularGasto(CuentaLocal cuenta, Gasto gasto) {
        if (gasto.getEstado().equals(Gasto.ANULADO)) {
            mensaje = "Error: el gasto ya fue anulado.";
            return false;
        }
        if (!gasto.estaEnPlazoAnulacion()) {
            mensaje = "Error: ya pasó el tiempo permitido para anular este gasto.";
            return false;
        }
        gasto.anularGasto();
        cuenta.acreditar(gasto.getMonto());

        mensaje = "Gasto anulado. Saldo restituido: $" + CuentaLocal.formatear(gasto.getMonto()) + ".";
        return true;
    }

    public String getMensaje() {
        return mensaje;
    }
}
