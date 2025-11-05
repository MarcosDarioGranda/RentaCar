package src;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistrarVenta extends JFrame {

    public VentanaRegistrarVenta() {
        setTitle("Registrar Venta - RentaCar");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        JLabel lCliente = new JLabel("Cliente:");
        JTextField tCliente = new JTextField();

        JLabel lVehiculo = new JLabel("Vehículo:");
        JTextField tVehiculo = new JTextField();

        JLabel lDNI = new JLabel("DNI:");
        JTextField tDNI = new JTextField();

        JLabel lImporte = new JLabel("Importe (€):");
        JTextField tImporte = new JTextField();

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRegistrar.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Venta registrada:\n" +
                                "Cliente: " + tCliente.getText() + "\n" +
                                "Vehículo: " + tVehiculo.getText() + "\n" +
                                "DNI: " + tDNI.getText() + "\n" +
                                "Importe: " + tImporte.getText() + " €"));

        btnCancelar.addActionListener(e -> dispose());

        add(lCliente); add(tCliente);
        add(lVehiculo); add(tVehiculo);
        add(lDNI); add(tDNI);
        add(lImporte); add(tImporte);
        add(btnRegistrar); add(btnCancelar);
    }
}
