package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Configuración básica
        setTitle("RentaCar - Tienda de Venta y Reventa de Coches");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas salir de RentaCar?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0);
        });
        menuArchivo.add(itemSalir);

        JMenu menuVehiculos = new JMenu("Vehículos");
        JMenuItem itemVer = new JMenuItem("Ver inventario");
        itemVer.addActionListener(e -> {
            String[] columnas = {"ID", "Marca", "Modelo", "Año", "Precio (€)"};
            Object[][] datos = {
                    {1, "Toyota", "Corolla", 2020, 15900},
                    {2, "BMW", "320d", 2019, 23900},
                    {3, "Audi", "A3", 2021, 25900},
                    {4, "Tesla", "Model 3", 2023, 35900}
            };
            JTable tabla = new JTable(datos, columnas);
            tabla.setRowHeight(25);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JScrollPane scroll = new JScrollPane(tabla);
            JDialog dialogo = new JDialog(this, "Inventario de Vehículos - RentaCar", true);
            dialogo.add(scroll);
            dialogo.setSize(500, 300);
            dialogo.setLocationRelativeTo(this);
            dialogo.setVisible(true);
        });

        JMenuItem itemAgregar = new JMenuItem("Agregar coche");
        itemAgregar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Formulario para agregar coche (demo)."));
        menuVehiculos.add(itemVer);
        menuVehiculos.add(itemAgregar);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de RentaCar");
        itemAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "RentaCar © 2025\nGestión de venta y reventa de vehículos.\nVersión 1.0 (demo)",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuVehiculos);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        JLabel titulo = new JLabel("🚗 RentaCar - Venta y Reventa de Coches", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 200));
        panelBotones.setBackground(Color.WHITE);

        JButton btnVer = new JButton("🚘 Ver Inventario");
        btnVer.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnVer.addActionListener(itemVer.getActionListeners()[0]);

        JButton btnAgregar = new JButton("➕ Agregar Coche");
        btnAgregar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAgregar.addActionListener(itemAgregar.getActionListeners()[0]);

        JButton btnVender = new JButton("💰 Registrar Venta");
        btnVender.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnVender.addActionListener(e -> JOptionPane.showMessageDialog(this, "Venta registrada correctamente (demo)."));

        JButton btnSalir = new JButton("❌ Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnSalir.addActionListener(itemSalir.getActionListeners()[0]);

        panelBotones.add(btnVer);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnVender);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
