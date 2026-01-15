package gui;

import javax.swing.*;

import db.GestorBD;

import java.awt.*;

public class VentanaRegistro extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GestorBD gestor;

    public VentanaRegistro() {
        setTitle("Registro - RentaCar");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        gestor = new GestorBD(); // Instancia de la BD

        // Panel principal con fondo suave y márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel titulo = new JLabel("Registro de Usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(50, 70, 120));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Panel del formulario
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        formulario.setOpaque(false);

        JLabel lUsuario = new JLabel("Usuario:");
        JTextField tUsuario = new JTextField();

        JLabel lCorreo = new JLabel("Correo:");
        JTextField tCorreo = new JTextField();

        JLabel lContrasena = new JLabel("Contraseña:");
        JPasswordField tContrasena = new JPasswordField();

        JLabel lConfirmar = new JLabel("Confirmar contraseña:");
        JPasswordField tConfirmar = new JPasswordField();

        // Ajustar tipografía
        Font fontCampos = new Font("Segoe UI", Font.PLAIN, 14);
        lUsuario.setFont(fontCampos);
        lCorreo.setFont(fontCampos);
        lContrasena.setFont(fontCampos);
        lConfirmar.setFont(fontCampos);
        tUsuario.setFont(fontCampos);
        tCorreo.setFont(fontCampos);
        tContrasena.setFont(fontCampos);
        tConfirmar.setFont(fontCampos);

        formulario.add(lUsuario); formulario.add(tUsuario);
        formulario.add(lCorreo); formulario.add(tCorreo);
        formulario.add(lContrasena); formulario.add(tContrasena);
        formulario.add(lConfirmar); formulario.add(tConfirmar);

        // Panel de botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botones.setOpaque(false);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRegistrar.setBackground(new Color(70, 130, 180));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Lógica de registro
        btnRegistrar.addActionListener(e -> {
            String usuario = tUsuario.getText().trim();
            String correo = tCorreo.getText().trim();
            String contrasena = new String(tContrasena.getPassword());
            String confirmar = new String(tConfirmar.getPassword());

            if (usuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!contrasena.equals(confirmar)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Intentar registrar en la base de datos
            if (gestor.insertarUsuario(usuario, contrasena)) {
                JOptionPane.showMessageDialog(this,
                        "Usuario registrado correctamente:\nUsuario: " + usuario + "\nCorreo: " + correo,
                        "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "El usuario ya existe. Elige otro nombre de usuario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        botones.add(btnRegistrar);
        botones.add(btnCancelar);

        // Estructura final
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(formulario, BorderLayout.CENTER);
        panelPrincipal.add(botones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }
}
