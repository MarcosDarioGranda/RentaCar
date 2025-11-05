package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Configuración básica de la ventana
        setTitle("Tienda de Venta y Reventa de Coches");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Coloca la ventana en el centro de la pantalla
        setLocationRelativeTo(null);

        // Crear componentes principales
        JLabel titulo = new JLabel("Tienda de Coches - AutoPlus", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        // Botones principales
        JButton btnVerAutos = new JButton("Ver Inventario");
        JButton btnAgregar = new JButton("Agregar Coche");
        JButton btnVender = new JButton("Registrar Venta");
        JButton btnSalir = new JButton("Salir");

        // Acciones básicas de los botones
        btnVerAutos.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Mostrando inventario (demo)."));

        btnAgregar.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Agregando coche (demo)."));

        btnVender.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Venta registrada (demo)."));

        btnSalir.addActionListener((ActionEvent e) -> System.exit(0));

        // Panel central con los botones
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.add(btnVerAutos);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnVender);
        panelBotones.add(btnSalir);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));

        // Agregar los componentes al marco principal
        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Este método asegura que la interfaz se ejecute en el hilo correcto
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
