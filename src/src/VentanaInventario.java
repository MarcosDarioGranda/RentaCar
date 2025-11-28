package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VentanaInventario extends JFrame {

    private ModeloTablaInventario modelo;
    private JTable tabla;

    public VentanaInventario() {
        setTitle("Inventario - RentaCar");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 1️⃣ Obtener coches desde BD
        GestorBD gestor = new GestorBD();
        List<Coche> coches = gestor.obtenerCoches();

        // 2️⃣ Crear modelo y tabla
        modelo = new ModeloTablaInventario(coches);
        tabla = new JTable(modelo);

        // 3️⃣ Ocultar columna matrícula
        tabla.removeColumn(tabla.getColumnModel().getColumn(4));

        // 4️⃣ Renderer de colores según precio
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Object precioObj = table.getValueAt(row, 3);
                if (precioObj instanceof Number) {
                    int precio = ((Number) precioObj).intValue(); // ⚡ Soluciona ClassCastException
                    if (precio < 20000) c.setBackground(new Color(144, 238, 144));
                    else if (precio <= 30000) c.setBackground(new Color(255, 255, 153));
                    else c.setBackground(new Color(255, 160, 122));
                }

                if (isSelected) c.setBackground(new Color(173, 216, 230));

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // 5️⃣ Configuración tabla
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // 6️⃣ Doble clic para detalles
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    int fila = tabla.getSelectedRow();

                    String marca = tabla.getValueAt(fila, 0).toString();
                    String modeloCoche = tabla.getValueAt(fila, 1).toString();
                    int anio = ((Number) tabla.getValueAt(fila, 2)).intValue();
                    int precio = ((Number) tabla.getValueAt(fila, 3)).intValue();
                    String matricula = modelo.getMatricula(fila);

                    // Ventana detalles
                    JDialog ventanaDetalles = new JDialog();
                    ventanaDetalles.setTitle("Detalles del vehículo");
                    ventanaDetalles.setSize(450, 250);
                    ventanaDetalles.setLocationRelativeTo(null);
                    ventanaDetalles.setLayout(new BorderLayout());

                    JPanel panelDatos = new JPanel(new GridLayout(5, 1, 5, 5));
                    panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
                    panelDatos.add(new JLabel("Marca: " + marca));
                    panelDatos.add(new JLabel("Modelo: " + modeloCoche));
                    panelDatos.add(new JLabel("Año: " + anio));
                    panelDatos.add(new JLabel("Precio: " + precio + " €"));
                    panelDatos.add(new JLabel("Matrícula: " + matricula));

                    JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
                    lblImagen.setPreferredSize(new Dimension(250, 200));

                    // Hilo de imágenes
                    Thread hiloImagenes = new Thread(() -> {
                        int index = 1;
                        while (ventanaDetalles.isVisible()) {
                            try {
                                String ruta = "/img/" + matricula + "_" + index + ".jpg";
                                java.net.URL url = getClass().getResource(ruta);
                                ImageIcon icon = (url != null) ? new ImageIcon(url) :
                                        new ImageIcon(getClass().getResource("/img/default.jpg"));

                                Image img = icon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);
                                lblImagen.setIcon(new ImageIcon(img));

                                index++;
                                if (index > 3) index = 1;
                                Thread.sleep(2000);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    // Panel botón "Más detalles"
                    JPanel panelBoton = new JPanel();
                    JButton btnMasDetalles = new JButton("Más detalles");
                    panelBoton.add(btnMasDetalles);

                    btnMasDetalles.addActionListener(ev2 -> {
                        JDialog dialogoTecnico = new JDialog(ventanaDetalles, "Ficha técnica", true);
                        dialogoTecnico.setSize(400, 250);
                        dialogoTecnico.setLocationRelativeTo(ventanaDetalles);
                        dialogoTecnico.setLayout(new BorderLayout());

                        JPanel panelFicha = new JPanel(new GridLayout(5, 1, 5, 5));
                        panelFicha.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                        panelFicha.add(new JLabel("Cilindrada: 1.8 L"));
                        panelFicha.add(new JLabel("Potencia: 140 CV"));
                        panelFicha.add(new JLabel("Consumo: 6.2 L/100km"));
                        panelFicha.add(new JLabel("Batalla: 2700 mm"));
                        panelFicha.add(new JLabel("Transmisión: Manual 6V"));

                        dialogoTecnico.add(panelFicha, BorderLayout.CENTER);

                        JButton btnCerrar = new JButton("Cerrar");
                        btnCerrar.addActionListener(e3 -> dialogoTecnico.dispose());
                        JPanel panelCerrar = new JPanel();
                        panelCerrar.add(btnCerrar);

                        dialogoTecnico.add(panelCerrar, BorderLayout.SOUTH);
                        dialogoTecnico.setVisible(true);
                    });

                    ventanaDetalles.add(panelDatos, BorderLayout.WEST);
                    ventanaDetalles.add(lblImagen, BorderLayout.EAST);
                    ventanaDetalles.add(panelBoton, BorderLayout.SOUTH);

                    ventanaDetalles.setVisible(true);
                    hiloImagenes.start();
                }
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    // Actualizar coches en la tabla
    public void actualizarTabla() {
        GestorBD gestor = new GestorBD();
        modelo.setCoches(gestor.obtenerCoches());
        tabla.repaint();
    }
}
