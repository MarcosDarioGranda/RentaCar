package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        JTable tabla = new JTable(new DefaultTableModel(datos, columnas));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}
