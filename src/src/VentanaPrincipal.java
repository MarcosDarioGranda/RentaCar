package src;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("RentaCar - Venta y Reventa de Coches");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("🚗 RentaCar", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        JButton btnVer = new JButton("Ver Inventario");
        JButton btnAgregar = new JButton("Agregar Coche");
        JButton btnVender = new JButton("Registrar Venta");
        JButton btnSalir = new JButton("Salir");

        btnVer.addActionListener(e -> new VentanaInventario().setVisible(true));
        btnAgregar.addActionListener(e -> new VentanaAgregarCoche().setVisible(true));
        btnVender.addActionListener(e -> new VentanaRegistrarVenta().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnVer);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnVender);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
