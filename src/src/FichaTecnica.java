package src;

public class FichaTecnica {

    private String cilindrada;
    private String potencia;
    private String consumo;
    private String batalla;
    private String transmision;

    public FichaTecnica(String cilindrada, String potencia, String consumo,
                        String batalla, String transmision) {
        this.cilindrada = cilindrada;
        this.potencia = potencia;
        this.consumo = consumo;
        this.batalla = batalla;
        this.transmision = transmision;
    }

    public String getCilindrada() { return cilindrada; }
    public String getPotencia() { return potencia; }
    public String getConsumo() { return consumo; }
    public String getBatalla() { return batalla; }
    public String getTransmision() { return transmision; }
}
