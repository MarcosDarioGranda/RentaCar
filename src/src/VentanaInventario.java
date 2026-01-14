package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VentanaInventario extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ModeloTablaInventario modelo;
    private JTable tabla;
    private GestorBD gestor;

    public VentanaInventario() {
        setTitle("Inventario - RentaCar");
        setSize(900, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        gestor = new GestorBD();
        List<Coche> coches = gestor.obtenerCoches();
        
        modelo = new ModeloTablaInventario(coches);
        tabla = new JTable(modelo);

        // Ocultar la columna matrícula
        tabla.removeColumn(tabla.getColumnModel().getColumn(4));

        // Renderer de colores según precio
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Object precioObj = table.getValueAt(row, 3);
                if (precioObj instanceof Number) {
                    int precio = ((Number) precioObj).intValue();

                    if (precio < 20000) c.setBackground(new Color(144, 238, 144));
                    else if (precio <= 30000) c.setBackground(new Color(255, 255, 153));
                    else c.setBackground(new Color(255, 160, 122));
                }

                if (isSelected) c.setBackground(new Color(173, 216, 230));
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Evento doble clic en coche
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

                    mostrarDetallesVehiculo(marca, modeloCoche, anio, precio, matricula);
                }
            }
        });

        
        
        // ---------- Panel inferior con botones de modificación ----------

        JButton btnModificarTodos = new JButton("Modificar todo (menos marca)");
        JButton btnModificarModelo = new JButton("Modificar modelo");
        JButton btnModificarAnio = new JButton("Modificar año");
        JButton btnModificarPrecio = new JButton("Modificar precio");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnPrecioMarcas = new JButton("Precio de marcas");

        // ---------- Agregar tabla ----------
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ---------- Panel inferior con botones ----------
        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotonesInferior.setPreferredSize(new Dimension(600, 60)); // Fija altura para que se vea
        panelBotonesInferior.add(btnModificarTodos);
        panelBotonesInferior.add(btnModificarModelo);
        panelBotonesInferior.add(btnModificarAnio);
        panelBotonesInferior.add(btnModificarPrecio);
        panelBotonesInferior.add(btnRefrescar);
        panelBotonesInferior.add(btnPrecioMarcas);

        add(panelBotonesInferior, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);

        // ---------- Acciones de botones ----------

        btnRefrescar.addActionListener(e -> actualizarTabla());

        btnModificarTodos.addActionListener(e -> {
            int viewRow = tabla.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un coche en la tabla primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = tabla.convertRowIndexToModel(viewRow);
            String matricula = modelo.getMatricula(modelRow);
            String marca = (String) tabla.getValueAt(viewRow, 0);

            // Pedir nuevos valores: modelo, año, precio
            JTextField tModelo = new JTextField();
            JTextField tAnio = new JTextField();
            JTextField tPrecio = new JTextField();

            tModelo.setText(tabla.getValueAt(viewRow, 1).toString());
            tAnio.setText(tabla.getValueAt(viewRow, 2).toString());
            tPrecio.setText(tabla.getValueAt(viewRow, 3).toString());

            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Marca (no editable): " + marca));
            p.add(new JLabel("Modelo:"));
            p.add(tModelo);
            p.add(new JLabel("Año:"));
            p.add(tAnio);
            p.add(new JLabel("Precio (€):"));
            p.add(tPrecio);

            int res = JOptionPane.showConfirmDialog(this, p,
                    "Modificar todo (excepto marca) - " + matricula,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (res == JOptionPane.OK_OPTION) {
                String nuevoModelo = tModelo.getText().trim();
                String sAnio = tAnio.getText().trim();
                String sPrecio = tPrecio.getText().trim();

                if (nuevoModelo.isEmpty() || sAnio.isEmpty() || sPrecio.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Rellena todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int nuevoAnio;
                double nuevoPrecio;
                try {
                    nuevoAnio = Integer.parseInt(sAnio);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Año inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    nuevoPrecio = Double.parseDouble(sPrecio);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean ok = gestor.actualizarCocheExceptoMarca(matricula, nuevoModelo, nuevoAnio, nuevoPrecio);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Coche actualizado correctamente.");
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el coche.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnModificarModelo.addActionListener(e -> {
            int viewRow = tabla.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un coche primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = tabla.convertRowIndexToModel(viewRow);
            String matricula = modelo.getMatricula(modelRow);

            String current = tabla.getValueAt(viewRow, 1).toString();
            String nuevo = JOptionPane.showInputDialog(this, "Nuevo modelo:", current);
            if (nuevo != null) {
                nuevo = nuevo.trim();
                if (nuevo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Modelo vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean ok = gestor.actualizarModelo(matricula, nuevo);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Modelo actualizado.");
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el modelo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnModificarAnio.addActionListener(e -> {
            int viewRow = tabla.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un coche primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = tabla.convertRowIndexToModel(viewRow);
            String matricula = modelo.getMatricula(modelRow);

            String current = tabla.getValueAt(viewRow, 2).toString();
            String nuevoStr = JOptionPane.showInputDialog(this, "Nuevo año:", current);
            if (nuevoStr != null) {
                nuevoStr = nuevoStr.trim();
                if (nuevoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Año vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int nuevoAnio;
                try {
                    nuevoAnio = Integer.parseInt(nuevoStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Año inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean ok = gestor.actualizarAnio(matricula, nuevoAnio);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Año actualizado.");
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el año.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnModificarPrecio.addActionListener(e -> {
            int viewRow = tabla.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un coche primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = tabla.convertRowIndexToModel(viewRow);
            String matricula = modelo.getMatricula(modelRow);

            String current = tabla.getValueAt(viewRow, 3).toString();
            String nuevoStr = JOptionPane.showInputDialog(this, "Nuevo precio (€):", current);
            if (nuevoStr != null) {
                nuevoStr = nuevoStr.trim();
                if (nuevoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Precio vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double nuevoPrecio;
                try {
                    nuevoPrecio = Double.parseDouble(nuevoStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean ok = gestor.actualizarPrecio(matricula, nuevoPrecio);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Precio actualizado.");
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el precio.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnPrecioMarcas.addActionListener(e -> mostrarPrecioPorMarcas());

    }

    // ============================
    //      VENTANA DETALLES
    // ============================

    private void mostrarDetallesVehiculo(String marca, String modeloCoche, int anio, int precio, String matricula) {

        System.out.println("=== DETALLES DEL VEHÍCULO ===");
        System.out.println("Matrícula: '" + matricula + "'");
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modeloCoche);

        JDialog ventanaDetalles = new JDialog(this, "Detalles del vehículo", true);
        ventanaDetalles.setSize(500, 300);
        ventanaDetalles.setLocationRelativeTo(this);
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

        // Hilo imágenes
        Thread hiloImagenes = new Thread(() -> {
            int index = 1;
            while (ventanaDetalles.isVisible()) {
                try {
                    String ruta = "/img/" + matricula + "_" + index + ".jpg";
                    java.net.URL url = getClass().getResource(ruta);

                    ImageIcon icon = (url != null)
                            ? new ImageIcon(url)
                            : new ImageIcon(getClass().getResource("/img/default.jpg"));

                    Image img = icon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);

                    SwingUtilities.invokeLater(() -> lblImagen.setIcon(new ImageIcon(img)));

                    index++;
                    if (index > 3) index = 1;
                    Thread.sleep(2000);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // PANEL BOTONES
        JPanel panelBoton = new JPanel();

        JButton btnMasDetalles = new JButton("Más detalles");
        JButton btnCrearFicha = new JButton("Crear ficha técnica");
        JButton btnEliminarFicha = new JButton("Eliminar ficha");

        panelBoton.add(btnMasDetalles);
        panelBoton.add(btnCrearFicha);
        panelBoton.add(btnEliminarFicha);

        // Acción Más detalles → ficha técnica
        btnMasDetalles.addActionListener(ev -> mostrarFichaTecnica(matricula, ventanaDetalles));

        // Acción Crear ficha técnica → formulario para registrar
        btnCrearFicha.addActionListener(ev -> ventanaCrearFichaTecnica(matricula, ventanaDetalles));

        // Acción Eliminar ficha técnica
        btnEliminarFicha.addActionListener(ev -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                ventanaDetalles,
                "¿Estás seguro de eliminar la ficha técnica de " + matricula + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                gestor = new GestorBD();
                if (gestor.eliminarFichaTecnica(matricula)) {
                    JOptionPane.showMessageDialog(ventanaDetalles, 
                        "Ficha técnica eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(ventanaDetalles,
                        "No se pudo eliminar. Es posible que no exista ficha técnica.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        ventanaDetalles.add(panelDatos, BorderLayout.WEST);
        ventanaDetalles.add(lblImagen, BorderLayout.EAST);
        ventanaDetalles.add(panelBoton, BorderLayout.SOUTH);

        ventanaDetalles.setVisible(true);
        hiloImagenes.start();
    }

    // ============================
    //      VER FICHA TÉCNICA
    // ============================

    private void mostrarFichaTecnica(String matricula, JDialog parent) {

        System.out.println("=== MOSTRANDO FICHA TÉCNICA ===");
        System.out.println("Matrícula: '" + matricula + "'");

        GestorBD gestor = new GestorBD();
        FichaTecnica ficha = gestor.obtenerFicha(matricula);

        if (ficha == null) {
            JOptionPane.showMessageDialog(parent,
                    "Este coche no tiene ficha técnica registrada.\n" +
                    "Usa el botón 'Crear ficha técnica' para añadir una.",
                    "Sin ficha técnica",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog(parent, "Ficha técnica - " + matricula, true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setLayout(new BorderLayout());

        JTextField txtCilindrada = new JTextField(ficha.getCilindrada());
        JTextField txtPotencia = new JTextField(ficha.getPotencia());
        JTextField txtConsumo = new JTextField(ficha.getConsumo());
        JTextField txtBatalla = new JTextField(ficha.getBatalla());
        JTextField txtTransmision = new JTextField(ficha.getTransmision());

        txtCilindrada.setEditable(false);
        txtPotencia.setEditable(false);
        txtConsumo.setEditable(false);
        txtBatalla.setEditable(false);
        txtTransmision.setEditable(false);

        JPanel fichaPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fichaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fichaPanel.add(new JLabel("Cilindrada: "));
        fichaPanel.add(txtCilindrada);

        fichaPanel.add(new JLabel("Potencia: "));
        fichaPanel.add(txtPotencia);

        fichaPanel.add(new JLabel("Consumo: "));
        fichaPanel.add(txtConsumo);

        fichaPanel.add(new JLabel("Batalla: "));
        fichaPanel.add(txtBatalla);

        fichaPanel.add(new JLabel("Transmisión: "));
        fichaPanel.add(txtTransmision);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());

        JButton btnModificar = new JButton("Modificar");

        btnModificar.addActionListener(e -> {

            if (btnModificar.getText().equals("Modificar")) {
                txtCilindrada.setEditable(true);
                txtPotencia.setEditable(true);
                txtConsumo.setEditable(true);
                txtBatalla.setEditable(true);
                txtTransmision.setEditable(true);
                btnModificar.setText("Guardar");
            }
            else {
                boolean ok = gestor.actualizarFichaTecnica(
                        matricula,
                        txtCilindrada.getText(),
                        txtPotencia.getText(),
                        txtConsumo.getText(),
                        txtBatalla.getText(),
                        txtTransmision.getText()
                );

                if (ok) {
                    JOptionPane.showMessageDialog(dialogo,
                            "Ficha técnica actualizada correctamente.");
                    dialogo.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogo,
                            "Error al actualizar la ficha técnica.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnModificar);
        panelBotones.add(btnCerrar);

        dialogo.add(fichaPanel, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    
    

    // ============================
    //   CREAR FICHA TÉCNICA NUEVA
    // ============================

    private void ventanaCrearFichaTecnica(String matricula, JDialog parent) {

        System.out.println("=== VENTANA CREAR FICHA TÉCNICA ===");
        System.out.println("Matrícula recibida: '" + matricula + "'");
        System.out.println("Longitud: " + matricula.length());

        GestorBD gestor = new GestorBD();

        // Verificar si ya existe ficha técnica
        FichaTecnica existente = gestor.obtenerFicha(matricula);
        if (existente != null) {
            JOptionPane.showMessageDialog(parent,
                    "Este coche YA tiene una ficha técnica registrada.\n\n" +
                    "Cilindrada: " + existente.getCilindrada() + "\n" +
                    "Potencia: " + existente.getPotencia() + "\n\n" +
                    "Usa el botón 'Eliminar ficha' si deseas crear una nueva.",
                    "Ficha técnica existente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog(parent, "Crear ficha técnica - " + matricula, true);
        dialogo.setSize(450, 320);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField tCilindrada = new JTextField();
        JTextField tPotencia = new JTextField();
        JTextField tConsumo = new JTextField();
        JTextField tBatalla = new JTextField();
        JTextField tTransmision = new JTextField();

        panel.add(new JLabel("Cilindrada (ej: 2.0L):"));
        panel.add(tCilindrada);

        panel.add(new JLabel("Potencia (ej: 150 CV):"));
        panel.add(tPotencia);

        panel.add(new JLabel("Consumo (ej: 6.5 L/100km):"));
        panel.add(tConsumo);

        panel.add(new JLabel("Batalla (ej: 2700mm):"));
        panel.add(tBatalla);

        panel.add(new JLabel("Transmisión (ej: Manual):"));
        panel.add(tTransmision);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(72, 201, 176));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(botones, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialogo.dispose());

        btnGuardar.addActionListener(e -> {

            String cilindrada = tCilindrada.getText().trim();
            String potencia = tPotencia.getText().trim();
            String consumo = tConsumo.getText().trim();
            String batalla = tBatalla.getText().trim();
            String transmision = tTransmision.getText().trim();

            if (cilindrada.isEmpty() || potencia.isEmpty() || consumo.isEmpty() || 
                batalla.isEmpty() || transmision.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo,
                    "Por favor, completa todos los campos.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = gestor.insertarFichaTecnica(
                    matricula,
                    cilindrada,
                    potencia,
                    consumo,
                    batalla,
                    transmision
            );

            if (ok) {
                JOptionPane.showMessageDialog(dialogo, 
                    "✓ Ficha técnica guardada correctamente para " + matricula,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogo,
                        "✗ No se pudo guardar la ficha técnica.\n\n" +
                        "Posibles causas:\n" +
                        "• La matrícula no existe en el inventario\n" +
                        "• Ya existe una ficha para esta matrícula\n\n" +
                        "Revisa la consola para más detalles.",
                        "Error al guardar",
                        JOptionPane.ERROR_MESSAGE);
            }
            
        });
        
        dialogo.setVisible(true);
    }
    
    
    // ============================
    //	     RECURSIVIDAD
    // ============================
    
    private void calcularPrecioMarcasRecursivo(List<Coche> coches,int index,Map<String, Double> resultado) {
    	
        if (index == coches.size()) {
            return;
        }

        Coche coche = coches.get(index);
        String marca = coche.getMarca();
        double precio = coche.getPrecio();

        resultado.put(marca,resultado.getOrDefault(marca, 0.0) + precio);

        calcularPrecioMarcasRecursivo(coches, index + 1, resultado);
    }

    // ============================
    //		VENTANA RECURSIVIDAD
    // ============================
    
    private void mostrarPrecioPorMarcas() {

        List<Coche> coches = gestor.obtenerCoches();

        Map<String, Double> preciosPorMarca = new LinkedHashMap<>();

        calcularPrecioMarcasRecursivo(coches, 0, preciosPorMarca);

        JDialog dialogo = new JDialog(this, "Precio total por marcas", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(preciosPorMarca.size(), 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (String marca : preciosPorMarca.keySet()) {
            double total = preciosPorMarca.get(marca);
            panel.add(new JLabel(
                    "• " + marca + ": " + String.format("%.2f", total) + " €"
            ));
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelBoton, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    
    

    // ============================
    //   ACTUALIZAR TABLA
    // ============================

    public void actualizarTabla() {
        modelo.setCoches(gestor.obtenerCoches());
        tabla.repaint();
    }
}