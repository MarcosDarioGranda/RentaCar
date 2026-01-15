package gui;

import javax.swing.*;

import db.GestorBD;

import java.awt.*;

public class VentanaRegistrarVenta extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GestorBD gestor;

    public VentanaRegistrarVenta() {
        setTitle("Registrar Venta - RentaCar");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        gestor = new GestorBD();

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("Registrar nueva venta", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel lCodigo = new JLabel("Matrícula:");
        JTextField tCodigo = new JTextField();

        JLabel lCliente = new JLabel("Cliente:");
        JTextField tCliente = new JTextField();

        JLabel lVehiculo = new JLabel("Vehículo:");
        JTextField tVehiculo = new JTextField();

        JLabel lDNI = new JLabel("DNI:");
        JTextField tDNI = new JTextField();

        JLabel lImporte = new JLabel("Importe (€):");
        JTextField tImporte = new JTextField();

        panelFormulario.add(lCodigo); panelFormulario.add(tCodigo);
        panelFormulario.add(lCliente); panelFormulario.add(tCliente);
        panelFormulario.add(lVehiculo); panelFormulario.add(tVehiculo);
        panelFormulario.add(lDNI); panelFormulario.add(tDNI);
        panelFormulario.add(lImporte); panelFormulario.add(tImporte);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRegistrar.setBackground(new Color(72, 201, 176));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> {
            String matricula = tCodigo.getText().trim();
            String cliente = tCliente.getText().trim();
            String vehiculo = tVehiculo.getText().trim();
            String dni = tDNI.getText().trim();
            String importeStr = tImporte.getText().trim();

            if (matricula.isEmpty() || cliente.isEmpty() || vehiculo.isEmpty() ||
                dni.isEmpty() || importeStr.isEmpty()) {

                JOptionPane.showMessageDialog(this, "Completa todos los campos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double importe = Double.parseDouble(importeStr);

                Venta venta = new Venta(matricula, cliente, vehiculo, dni, importe);

                if (gestor.registrarVenta(venta)) {

                    gestor.eliminarCoche(matricula);

                    JOptionPane.showMessageDialog(this,
                            "Venta registrada y coche eliminado del inventario.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    dispose();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Importe inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        add(panelPrincipal);
    }
}
