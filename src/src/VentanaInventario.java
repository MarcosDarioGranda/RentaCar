package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaInventario extends JFrame {

    public VentanaInventario() {
        setTitle("Inventario - RentaCar");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnas = {"Marca", "Modelo", "Año", "Precio (€)"};
        Object[][] datos = {
                {"Toyota", "Corolla", 2020, 15000},
                {"BMW", "320d", 2019, 23000},
                {"Audi", "A3", 2021, 25000},
                {"Tesla", "Model 3", 2023, 35000}
        };

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            // Evitamos que se editen las celdas
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Object precioObj = table.getValueAt(row, 3);
                if (precioObj instanceof Number) {
                    int precio = ((Number) precioObj).intValue();

                    int rango; // 0: barato, 1: medio, 2: caro
                    if (precio < 20000) rango = 0;
                    else if (precio <= 30000) rango = 1;
                    else rango = 2;

                    switch (rango) {
                        case 0:
                            c.setBackground(new Color(144, 238, 144)); // Verde claro
                            break;
                        case 1:
                            c.setBackground(new Color(255, 255, 153)); // Amarillo claro
                            break;
                        case 2:
                            c.setBackground(new Color(255, 160, 122)); // Rojo claro
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            break;
                    }
                }

                if (isSelected) {
                    c.setBackground(new Color(173, 216, 230)); // Azul claro si está seleccionada
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // 🖱️ Evento de ratón: doble clic para ver detalles del coche
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    int fila = tabla.getSelectedRow();
                    String marca = tabla.getValueAt(fila, 0).toString();
                    String modelo = tabla.getValueAt(fila, 1).toString();
                    int anio = (int) tabla.getValueAt(fila, 2);
                    int precio = (int) tabla.getValueAt(fila, 3);

                    JOptionPane.showMessageDialog(null,
                            "Detalles del coche:\n" +
                                    "Marca: " + marca + "\n" +
                                    "Modelo: " + modelo + "\n" +
                                    "Año: " + anio + "\n" +
                                    "Precio: " + precio + " €",
                            "Información del vehículo",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Ajustes visuales extra
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}

