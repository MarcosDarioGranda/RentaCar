package src;

import javax.swing.*;
import java.awt.*;

public class VentanaAgregarCoche extends JFrame {

    public VentanaAgregarCoche() {
        setTitle("Agregar Coche - RentaCar");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        JLabel lMarca = new JLabel("Marca:");
        JTextField tMarca = new JTextField();

        JLabel lModelo = new JLabel("Modelo:");
        JTextField tModelo = new JTextField();

        JLabel lAnio = new JLabel("Año:");
        JTextField tAnio = new JTextField();

        JLabel lPrecio = new JLabel("Precio (€):");
        JTextField tPrecio = new JTextField();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Coche agregado:\n" +
                                "Marca: " + tMarca.getText() + "\n" +
                                "Modelo: " + tModelo.getText() + "\n" +
                                "Año: " + tAnio.getText() + "\n" +
                                "Precio: " + tPrecio.getText() + " €"));

        btnCancelar.addActionListener(e -> dispose());

        add(lMarca); add(tMarca);
        add(lModelo); add(tModelo);
        add(lAnio); add(tAnio);
        add(lPrecio); add(tPrecio);
        add(btnGuardar); add(btnCancelar);
    }
}
