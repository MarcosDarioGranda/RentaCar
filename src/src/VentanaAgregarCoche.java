package src;

import javax.swing.*;
import java.awt.*;

public class VentanaAgregarCoche extends JFrame {

    public VentanaAgregarCoche() {
        setTitle("Agregar Coche - RentaCar");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 🔹 Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // Márgenes

        // 🔹 Título superior
        JLabel titulo = new JLabel("Agregar nuevo coche", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // 🔹 Panel del formulario
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

        // 🔹 Personalizamos etiquetas
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

        // 🔹 Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        // Estilo de botones
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

        // 🔹 Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            String codigo = tCodigo.getText().trim();
            String marca = tMarca.getText().trim();
            String modelo = tModelo.getText().trim();
            String anio = tAnio.getText().trim();
            String precio = tPrecio.getText().trim();

            if (codigo.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || precio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ✅ Comprobación simple del formato de matrícula (4 números + 3 letras)
            if (!codigo.matches("\\d{4}[A-Z]{3}")) {
                JOptionPane.showMessageDialog(this, "Formato de matrícula incorrecta (ejemplo: 1234ABC).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Coche agregado correctamente:\n\n" +
                            "Matrícula: " + codigo + "\n" +
                            "Marca: " + marca + "\n" +
                            "Modelo: " + modelo + "\n" +
                            "Año: " + anio + "\n" +
                            "Precio: " + precio + " €",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        });


        btnCancelar.addActionListener(e -> dispose());

        add(panelPrincipal);
    }

}
