package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class VentanaInventario extends JFrame {

    public VentanaInventario() {
        setTitle("Inventario - RentaCar");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnas = {"Marca", "Modelo", "Año", "Precio (€)", "Código"};

        Object[][] datos = {
                {"Toyota", "Corolla", 2020, 15000, "C001"},
                {"BMW", "320d", 2019, 23000, "C002"},
                {"Audi", "A3", 2021, 25000, "C003"},
                {"Tesla", "Model 3", 2023, 35000, "C004"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);

        // Ocultar la columna del código
        tabla.removeColumn(tabla.getColumnModel().getColumn(4));

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Object precioObj = table.getValueAt(row, 3);
                if (precioObj instanceof Number) {
                    int precio = ((Number) precioObj).intValue();

                    if (precio < 20000)
                        c.setBackground(new Color(144, 238, 144)); // Verde claro
                    else if (precio <= 30000)
                        c.setBackground(new Color(255, 255, 153)); // Amarillo claro
                    else
                        c.setBackground(new Color(255, 160, 122)); // Rojo claro
                }

                if (isSelected)
                    c.setBackground(new Color(173, 216, 230)); // Azul claro si está seleccionada

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    int fila = tabla.getSelectedRow();

                    String marca = tabla.getValueAt(fila, 0).toString();
                    String modeloCoche = tabla.getValueAt(fila, 1).toString();
                    int anio = (int) tabla.getValueAt(fila, 2);
                    int precio = (int) tabla.getValueAt(fila, 3);

                    // ⚠️ Recuperamos el código desde el modelo original (no desde la tabla)
                    String codigo = modelo.getValueAt(fila, 4).toString();

                    // Crear ventana con los datos
                    JDialog ventanaDetalles = new JDialog();
                    ventanaDetalles.setTitle("Detalles del vehículo");
                    ventanaDetalles.setSize(450, 250);
                    ventanaDetalles.setLocationRelativeTo(null);
                    ventanaDetalles.setLayout(new BorderLayout());

                    // Panel de datos
                    JPanel panelDatos = new JPanel(new GridLayout(4, 1, 5, 5));
                    panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
                    panelDatos.add(new JLabel("Marca: " + marca));
                    panelDatos.add(new JLabel("Modelo: " + modeloCoche));
                    panelDatos.add(new JLabel("Año: " + anio));
                    panelDatos.add(new JLabel("Precio: " + precio + " €"));

                    // Imagen del coche según el código
                    JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
                    lblImagen.setPreferredSize(new Dimension(200, 200));

                    // 📷 Cargar imagen automáticamente según el código
                    String rutaImagen = "src/img/" + codigo + ".jpg";

                    File archivoImagen = new File(rutaImagen);
                    ImageIcon icon;

                    if (archivoImagen.exists()) {
                        icon = new ImageIcon(rutaImagen);
                    } else {
                        icon = new ImageIcon("src/img/default.jpg");
                    }

                    Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(img));
                    
                    //Panel inferior con botón de más detalles
                    JPanel panelBoton = new JPanel();
                    JButton btnMasDetalles = new JButton("Más detalles");
                    panelBoton.add(btnMasDetalles);

                    // Evento del botón
                    btnMasDetalles.addActionListener(ev -> {
                        // Crear segundo diálogo
                        JDialog dialogoTecnico = new JDialog(ventanaDetalles, "Ficha técnica", true);
                        dialogoTecnico.setSize(400, 250);
                        dialogoTecnico.setLocationRelativeTo(ventanaDetalles);
                        dialogoTecnico.setLayout(new BorderLayout());

                        // Panel de especificaciones técnicas
                        JPanel panelFicha = new JPanel(new GridLayout(5, 1, 5, 5));
                        panelFicha.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                        // Ejemplo de datos técnicos (se podran cargar de una BD o archivo)
                        panelFicha.add(new JLabel("Cilindrada: 1.8 L"));
                        panelFicha.add(new JLabel("Potencia: 140 CV"));
                        panelFicha.add(new JLabel("Consumo: 6.2 L/100km"));
                        panelFicha.add(new JLabel("Batalla: 2700 mm"));
                        panelFicha.add(new JLabel("Transmisión: Manual 6V"));

                        dialogoTecnico.add(panelFicha, BorderLayout.CENTER);

                        JButton btnCerrar = new JButton("Cerrar");
                        btnCerrar.addActionListener(e2 -> dialogoTecnico.dispose());
                        JPanel panelCerrar = new JPanel();
                        panelCerrar.add(btnCerrar);

                        dialogoTecnico.add(panelCerrar, BorderLayout.SOUTH);
                        dialogoTecnico.setVisible(true);
                    });


                    ventanaDetalles.add(panelDatos, BorderLayout.WEST);
                    ventanaDetalles.add(lblImagen, BorderLayout.EAST);
                    ventanaDetalles.add(panelBoton, BorderLayout.SOUTH);
                    ventanaDetalles.setVisible(true);
                }
            }
        });

        // Ajustes visuales
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}
