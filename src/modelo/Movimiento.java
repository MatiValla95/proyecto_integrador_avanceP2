package modelo;

public class Movimiento {

    private String id;
    private double monto;
    private String fechaHora;
    private String descripcion;

    public Movimiento(String id, double monto, String descripcion) {
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fechaHora = fechaHoraActual();
    }

    public boolean validarMonto() {
        return monto > 0;
    }

    public String getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public static int minutosActuales() {
        return (int) (System.currentTimeMillis() / 60000) - 300;
    }

    public static String fechaHoraActual() {
        return convertir(minutosActuales());
    }

    private static String convertir(int minutos) {
        int dias = minutos / 1440;
        int minutosDelDia = minutos % 1440;

        int anio = 1970;
        while (dias >= diasDelAnio(anio)) {
            dias = dias - diasDelAnio(anio);
            anio++;
        }
        int mes = 1;
        while (dias >= diasDelMes(anio, mes)) {
            dias = dias - diasDelMes(anio, mes);
            mes++;
        }
        int dia = dias + 1;

        int horas = minutosDelDia / 60;
        int min = minutosDelDia % 60;
        return anio + "-" + dosDigitos(mes) + "-" + dosDigitos(dia)
                + " " + dosDigitos(horas) + ":" + dosDigitos(min);
    }

    private static boolean esBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
    }

    private static int diasDelAnio(int anio) {
        if (esBisiesto(anio)) {
            return 366;
        }
        return 365;
    }

    private static int diasDelMes(int anio, int mes) {
        if (mes == 2) {
            if (esBisiesto(anio)) {
                return 29;
            }
            return 28;
        }
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            return 30;
        }
        return 31;
    }

    private static String dosDigitos(int numero) {
        if (numero < 10) {
            return "0" + numero;
        }
        return "" + numero;
    }
}
