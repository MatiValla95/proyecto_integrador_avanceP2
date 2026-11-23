package negocio;

import modelo.CuentaLocal;
import modelo.Deposito;
import modelo.Gasto;
import modelo.Movimiento;

public class GestorConciliacion {

    public double conciliarSaldos(CuentaLocal cuenta) {
        double totalDepositos = 0;
        double totalGastos = 0;

        for (Movimiento m : cuenta.getMovimientos()) {
            if (m instanceof Deposito) {
                totalDepositos = totalDepositos + m.getMonto();
            } else if (m instanceof Gasto) {
                Gasto g = (Gasto) m;
                if (g.getEstado().equals(Gasto.EJECUTADO)) {
                    totalGastos = totalGastos + g.getMonto();
                }
            }
        }
        double esperado = totalDepositos - totalGastos;
        return CuentaLocal.redondear(esperado - cuenta.obtenerSaldoTotal());
    }

    public String generarReportePorFechas(CuentaLocal cuenta, String fechaInicio, String fechaFin) {
        if (fechaInicio.compareTo(fechaFin) > 0) {
            return "Error: rango de fechas inválido.";
        }

        double totalGuardado = 0;
        double totalGastosEjecutados = 0;
        double totalAnulados = 0;
        int cantidadAnulados = 0;
        int cantidadMovimientos = 0;
        String detalle = "";

        double[] totalPorCategoria = new double[GestorGastos.CATEGORIAS.length];

        for (Movimiento m : cuenta.getMovimientos()) {
            String fecha = m.getFechaHora().substring(0, 10);   // solo aaaa-mm-dd
            if (fecha.compareTo(fechaInicio) < 0 || fecha.compareTo(fechaFin) > 0) {
                continue;
            }
            cantidadMovimientos++;

            if (m instanceof Deposito) {
                Deposito d = (Deposito) m;
                totalGuardado = totalGuardado + d.getMonto();
                detalle = detalle + d.getFechaHora() + "  DEPÓSITO  +$"
                        + CuentaLocal.formatear(d.getMonto()) + "  Origen: " + d.getOrigen() + "\n";
            } else if (m instanceof Gasto) {
                Gasto g = (Gasto) m;
                if (g.getEstado().equals(Gasto.ANULADO)) {
                    cantidadAnulados++;
                    totalAnulados = totalAnulados + g.getMonto();
                } else {
                    totalGastosEjecutados = totalGastosEjecutados + g.getMonto();
                    for (int i = 0; i < GestorGastos.CATEGORIAS.length; i++) {
                        if (GestorGastos.CATEGORIAS[i].equals(g.getCategoria())) {
                            totalPorCategoria[i] = totalPorCategoria[i] + g.getMonto();
                        }
                    }
                }
                detalle = detalle + g.getFechaHora() + "  GASTO      -$"
                        + CuentaLocal.formatear(g.getMonto()) + "  " + g.getComercio()
                        + " (" + g.getCategoria() + ") [" + g.getEstado() + "]\n";
            }
        }

        if (cantidadMovimientos == 0) {
            return "No hay movimientos en las fechas seleccionadas.";
        }

        String reporte = "========== REPORTE DE MOVIMIENTOS ==========\n";
        reporte = reporte + "Cuenta: " + cuenta.getNumeroCuenta() + "\n";
        reporte = reporte + "Periodo: " + fechaInicio + " al " + fechaFin + "\n";
        reporte = reporte + "--------------------------------------------\n";
        reporte = reporte + "Movimientos (ordenados por fecha):\n" + detalle;
        reporte = reporte + "--------------------------------------------\n";
        reporte = reporte + "Total de dinero guardado: $" + CuentaLocal.formatear(totalGuardado) + "\n";
        reporte = reporte + "Gastos ejecutados por categoría:\n";
        for (int i = 0; i < GestorGastos.CATEGORIAS.length; i++) {
            if (totalPorCategoria[i] > 0) {
                reporte = reporte + "   " + GestorGastos.CATEGORIAS[i] + ": $"
                        + CuentaLocal.formatear(totalPorCategoria[i]) + "\n";
            }
        }
        reporte = reporte + "Total de gastos ejecutados: $" + CuentaLocal.formatear(totalGastosEjecutados) + "\n";
        reporte = reporte + "Gastos anulados: " + cantidadAnulados
                + " (total $" + CuentaLocal.formatear(totalAnulados) + ")\n";
        reporte = reporte + "Saldo disponible: $" + CuentaLocal.formatear(cuenta.getSaldoDisponible()) + "\n";
        reporte = reporte + "Saldo en reserva: $" + CuentaLocal.formatear(cuenta.getSaldoReserva()) + "\n";
        reporte = reporte + "============================================\n";
        reporte = reporte + "Reporte generado correctamente.";
        return reporte;
    }
}