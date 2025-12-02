package src;

public class Coche {
    private String matricula;
    private String marca;
    private String modelo;
    private int anio;
    private double precio;

    public Coche(String matricula, String marca, String modelo, int anio, double precio) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precio = precio;
    }

    public String getMatricula() { return matricula; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + anio + ") - " + precio + " € [" + matricula + "]";
    }
}

//p