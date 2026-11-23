package modelo;

public class Deposito extends Movimiento {

    private String origen;

    public Deposito(String id, double monto, String origen, String descripcion) {
        super(id, monto, descripcion);
        this.origen = origen;
    }

    public boolean registrarDeposito() {
        return validarMonto() && !origen.trim().isEmpty();
    }

    public String getOrigen() {
        return origen;
    }
}
