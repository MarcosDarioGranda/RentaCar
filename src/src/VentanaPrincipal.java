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

        JLabel titulo = new JLabel("RentaCar", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(40, 70, 130));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);

        JLabel textoArriba = new JLabel("Bienvenido a RentaCar", SwingConstants.CENTER);
        textoArriba.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelCentro.add(textoArriba, BorderLayout.NORTH);

        ImageIcon icono = new ImageIcon(getClass().getResource("/img/portada.jpg"));
        Image imagenEscalada = icono.getImage().getScaledInstance(280, 210, Image.SCALE_SMOOTH);
        JLabel imagenLabel = new JLabel(new ImageIcon(imagenEscalada), SwingConstants.CENTER);
        panelCentro.add(imagenLabel, BorderLayout.CENTER);

        JLabel textoAbajo = new JLabel("Gestión de vehículos - Venta y Reventa", SwingConstants.CENTER);
        textoAbajo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panelCentro.add(textoAbajo, BorderLayout.SOUTH);

        add(panelCentro, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuCoches = new JMenu("Coches");
        JMenu menuSistema = new JMenu("Sistema");

        JMenuItem itemVer = new JMenuItem("Ver Inventario");
        JMenuItem itemAgregar = new JMenuItem("Agregar Coche");
        JMenuItem itemVender = new JMenuItem("Registrar Venta");
        JMenuItem itemSalir = new JMenuItem("Salir");

        VentanaInventario ventanaInventario = new VentanaInventario();

        itemVer.addActionListener(e -> ventanaInventario.setVisible(true));
        itemAgregar.addActionListener(e -> new VentanaAgregarCoche(ventanaInventario).setVisible(true));

        itemVender.addActionListener(e -> new VentanaRegistrarVenta().setVisible(true));

        itemSalir.addActionListener(e -> System.exit(0));

        menuCoches.add(itemVer);
        menuCoches.add(itemAgregar);
        menuCoches.add(itemVender);
        menuSistema.add(itemSalir);

        menuBar.add(menuCoches);
        menuBar.add(menuSistema);

        setJMenuBar(menuBar);
    }
}
