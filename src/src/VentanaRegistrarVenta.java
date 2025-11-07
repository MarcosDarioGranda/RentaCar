package src;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistrarVenta extends JFrame {

    public VentanaRegistrarVenta() {
        setTitle("Registrar Venta - RentaCar");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        //Título superior
        JLabel titulo = new JLabel("Registrar nueva venta", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        //Panel del formulario con 5 campos
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel lCodigo = new JLabel("Matricula:");
        JTextField tCodigo = new JTextField();

        JLabel lCliente = new JLabel("Cliente:");
        JTextField tCliente = new JTextField();

        JLabel lVehiculo = new JLabel("Vehículo:");
        JTextField tVehiculo = new JTextField();

        JLabel lDNI = new JLabel("DNI:");
        JTextField tDNI = new JTextField();

        JLabel lImporte = new JLabel("Importe (€):");
        JTextField tImporte = new JTextField();

        // 🔹 Fuente uniforme
        Font fontCampos = new Font("Segoe UI", Font.PLAIN, 14);
        for (JLabel label : new JLabel[]{lCodigo, lCliente, lVehiculo, lDNI, lImporte}) {
            label.setFont(fontCampos);
        }

        panelFormulario.add(lCodigo); panelFormulario.add(tCodigo);
        panelFormulario.add(lCliente); panelFormulario.add(tCliente);
        panelFormulario.add(lVehiculo); panelFormulario.add(tVehiculo);
        panelFormulario.add(lDNI); panelFormulario.add(tDNI);
        panelFormulario.add(lImporte); panelFormulario.add(tImporte);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        // Estilo de botones
        btnRegistrar.setBackground(new Color(72, 201, 176));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Acción del botón Registrar
        btnRegistrar.addActionListener(e -> {
            String codigo = tCodigo.getText().trim();
            String cliente = tCliente.getText().trim();
            String vehiculo = tVehiculo.getText().trim();
            String dni = tDNI.getText().trim();
            String importe = tImporte.getText().trim();

            if (codigo.isEmpty() || cliente.isEmpty() || vehiculo.isEmpty() || dni.isEmpty() || importe.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "✅ Venta registrada correctamente:\n\n" +
                            "Código coche: " + codigo + "\n" +
                            "Cliente: " + cliente + "\n" +
                            "Vehículo: " + vehiculo + "\n" +
                            "DNI: " + dni + "\n" +
                            "Importe: " + importe + " €",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        });

        //  Acción botón Cancelar
        btnCancelar.addActionListener(e -> dispose());

        add(panelPrincipal);
    }

}
