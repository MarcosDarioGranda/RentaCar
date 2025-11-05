package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {
	//H
    public VentanaPrincipal() {
        // Configuración básica de la ventana
        setTitle("Tienda de Venta y Reventa de Coches");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

        // Crear componentes principales
        JLabel titulo = new JLabel("Tienda de Coches - AutoPlus", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnVerAutos = new JButton("Ver Inventario");
        JButton btnAgregar = new JButton("Agregar Coche");
        JButton btnVender = new JButton("Registrar Venta");
        JButton btnSalir = new JButton("Salir");

        // Acciones básicas de los botones
        btnVerAutos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mostrando inventario (demo)."));
        btnAgregar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Agregando coche (demo)."));
        btnVender.addActionListener(e -> JOptionPane.showMessageDialog(this, "Venta registrada (demo)."));
        btnSalir.addActionListener((ActionEvent e) -> System.exit(0));

        // Panel central con botones
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.add(btnVerAutos);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnVender);
        panelBotones.add(btnSalir);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // Agregar componentes al marco
        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
