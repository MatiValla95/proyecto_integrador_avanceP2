package modelo;

public class Cliente {

    public static final int MAX_INTENTOS = 3;

    private String identificacion;
    private String nombreCompleto;
    private String correo;
    private String contrasena;
    private String pinSeguridad;
    private int intentosFallidos;
    private boolean bloqueada;

    private CuentaLocal cuenta;

    public Cliente(String identificacion, String nombreCompleto, String correo,
                   String contrasena, String pinSeguridad) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasena = contrasena;
        this.pinSeguridad = pinSeguridad;
        this.intentosFallidos = 0;
        this.bloqueada = false;
        this.cuenta = new CuentaLocal("CL-" + identificacion, this);
    }

    public boolean autenticar(String correo, String contrasena) {
        if (bloqueada) {
            return false;
        }
        if (this.correo.equalsIgnoreCase(correo) && this.contrasena.equals(contrasena)) {
            intentosFallidos = 0;
            return true;
        }
        intentosFallidos++;
        if (intentosFallidos >= MAX_INTENTOS) {
            bloqueada = true;
        }
        return false;
    }

    public boolean desbloquearConPin(String pin) {
        if (pinSeguridad.equals(pin)) {
            bloqueada = false;
            intentosFallidos = 0;
            return true;
        }
        return false;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public CuentaLocal getCuenta() {
        return cuenta;
    }
}
