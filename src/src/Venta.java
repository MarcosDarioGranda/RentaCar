package src;

public class Venta {
    private String matricula;
    private String cliente;
    private String vehiculo;
    private String dni;
    private double importe;

    public Venta(String matricula, String cliente, String vehiculo, String dni, double importe) {
        this.matricula = matricula;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.dni = dni;
        this.importe = importe;
    }

    // Getters
    public String getMatricula() { return matricula; }
    public String getCliente() { return cliente; }
    public String getVehiculo() { return vehiculo; }
    public String getDni() { return dni; }
    public double getImporte() { return importe; }
}
