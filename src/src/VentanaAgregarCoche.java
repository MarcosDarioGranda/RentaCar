package src;

import javax.swing.*;

import java.awt.*;

public class VentanaAgregarCoche extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaAgregarCoche(VentanaInventario ventanaInventario) {
        setTitle("Agregar Coche - RentaCar");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("Agregar nuevo coche", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel lCodigo = new JLabel("Matricula:");
        JTextField tCodigo = new JTextField();
        JLabel lMarca = new JLabel("Marca:");
        JTextField tMarca = new JTextField();
        JLabel lModelo = new JLabel("Modelo:");
        JTextField tModelo = new JTextField();
        JLabel lAnio = new JLabel("Año:");
        JTextField tAnio = new JTextField();
        JLabel lPrecio = new JLabel("Precio (€):");
        JTextField tPrecio = new JTextField();

        Font fontCampos = new Font("Segoe UI", Font.PLAIN, 14);
        for (JLabel label : new JLabel[]{lCodigo, lMarca, lModelo, lAnio, lPrecio}) {
            label.setFont(fontCampos);
        }

        panelFormulario.add(lCodigo); panelFormulario.add(tCodigo);
        panelFormulario.add(lMarca); panelFormulario.add(tMarca);
        panelFormulario.add(lModelo); panelFormulario.add(tModelo);
        panelFormulario.add(lAnio); panelFormulario.add(tAnio);
        panelFormulario.add(lPrecio); panelFormulario.add(tPrecio);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(72, 201, 176));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String codigo = tCodigo.getText().trim().toUpperCase();
            String marca = tMarca.getText().trim();
            String modelo = tModelo.getText().trim();
            String anio = tAnio.getText().trim();
            String precio = tPrecio.getText().trim();

            if (codigo.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || precio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!codigo.matches("\\d{4}[A-Z]{3}")) {
                JOptionPane.showMessageDialog(this, 
                    "Matrícula inválida.\nFormato correcto: 1234ABC (4 números + 3 letras)", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int anioInt = Integer.parseInt(anio);
                double precioDouble = Double.parseDouble(precio);

                if (anioInt < 1900 || anioInt > 2025) {
                    JOptionPane.showMessageDialog(this, "El año debe estar entre 1900 y 2025.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (precioDouble <= 0) {
                    JOptionPane.showMessageDialog(this, "El precio debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Coche nuevo = new Coche(codigo, marca, modelo, anioInt, precioDouble);
                GestorBD gestor = new GestorBD();
                
                if (gestor.insertarCoche(nuevo)) {
                    JOptionPane.showMessageDialog(this, 
                        "✓ Coche agregado correctamente:\n" +
                        marca + " " + modelo + "\n" +
                        "Matrícula: " + codigo,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    ventanaInventario.actualizarTabla();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "✗ Ya existe un coche con la matrícula " + codigo + ".\n" +
                        "Por favor, usa otra matrícula.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Año o precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        add(panelPrincipal);
    }
}