package modelo;

public class Gasto extends Movimiento {

    public static final String EJECUTADO = "EJECUTADO";
    public static final String ANULADO = "ANULADO";

    public static final int PLAZO_ANULACION_MINUTOS = 5;

    private String comercio;
    private String categoria;
    private String estado;
    private int minutoRegistro;

    public Gasto(String id, double monto, String comercio, String categoria) {
        super(id, monto, "Gasto en " + comercio);
        this.comercio = comercio;
        this.categoria = categoria;
        this.estado = "";
        this.minutoRegistro = Movimiento.minutosActuales();
    }

    public boolean registrarGasto() {
        if (!validarMonto() || comercio.trim().isEmpty() || categoria.trim().isEmpty()) {
            return false;
        }
        estado = EJECUTADO;
        return true;
    }

    public boolean anularGasto() {
        if (estado.equals(EJECUTADO) && estaEnPlazoAnulacion()) {
            estado = ANULADO;
            return true;
        }
        return false;
    }

    public boolean estaEnPlazoAnulacion() {
        int minutosTranscurridos = Movimiento.minutosActuales() - minutoRegistro;
        return minutosTranscurridos <= PLAZO_ANULACION_MINUTOS;
    }

    public String getComercio() {
        return comercio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getEstado() {
        return estado;
    }
}
