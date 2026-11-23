package negocio;

import java.util.ArrayList;
import modelo.Cliente;

public class GestorClientes {

    private ArrayList<Cliente> clientes;
    private String mensaje;

    public GestorClientes() {
        clientes = new ArrayList<Cliente>();
        mensaje = "";
    }

    public boolean registrarCliente(String nombreCompleto, String identificacion,
                                    String correo, String contrasena, String pin) {
        if (existeIdentificacion(identificacion)) {
            mensaje = "Error: la identificación ya está registrada.";
            return false;
        }
        if (existeCorreo(correo)) {
            mensaje = "Error: el correo electrónico ya está registrado.";
            return false;
        }
        if (contrasena.length() < 8) {
            mensaje = "Error: la contraseña debe tener mínimo 8 caracteres.";
            return false;
        }
        if (!pinValido(pin)) {
            mensaje = "Error: el PIN debe tener exactamente 4 dígitos.";
            return false;
        }

        Cliente cliente = new Cliente(identificacion, nombreCompleto, correo, contrasena, pin);
        clientes.add(cliente);

        mensaje = "Cliente registrado correctamente.";
        return true;
    }

    public Cliente iniciarSesion(String correo, String contrasena) {
        Cliente cliente = buscarPorCorreo(correo);
        if (cliente == null) {
            mensaje = "Error: no existe un cliente registrado con ese correo.";
            return null;
        }
        if (cliente.isBloqueada()) {
            mensaje = "Cuenta bloqueada por múltiples intentos fallidos.";
            return null;
        }
        if (cliente.autenticar(correo, contrasena)) {
            mensaje = "Acceso permitido. Bienvenido/a, " + cliente.getNombreCompleto() + ".";
            return cliente;
        }
        if (cliente.isBloqueada()) {
            mensaje = "Cuenta bloqueada por múltiples intentos fallidos.";
        } else {
            int restantes = Cliente.MAX_INTENTOS - cliente.getIntentosFallidos();
            mensaje = "Contraseña incorrecta. Intentos restantes: " + restantes + ".";
        }
        return null;
    }

    public boolean desbloquearCuenta(String correo, String pin) {
        Cliente cliente = buscarPorCorreo(correo);
        if (cliente == null) {
            mensaje = "Error: no existe un cliente registrado con ese correo.";
            return false;
        }
        if (!cliente.isBloqueada()) {
            mensaje = "La cuenta no está bloqueada.";
            return false;
        }
        if (cliente.desbloquearConPin(pin)) {
            mensaje = "Cuenta desbloqueada correctamente.";
            return true;
        }
        mensaje = "Error: PIN incorrecto.";
        return false;
    }

    public boolean existeIdentificacion(String identificacion) {
        for (Cliente c : clientes) {
            if (c.getIdentificacion().equals(identificacion)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeCorreo(String correo) {
        return buscarPorCorreo(correo) != null;
    }

    private Cliente buscarPorCorreo(String correo) {
        for (Cliente c : clientes) {
            if (c.getCorreo().equalsIgnoreCase(correo)) {
                return c;
            }
        }
        return null;
    }

    private boolean pinValido(String pin) {
        if (pin.length() != 4) {
            return false;
        }
        for (int i = 0; i < pin.length(); i++) {
            if (!Character.isDigit(pin.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public String getMensaje() {
        return mensaje;
    }
}