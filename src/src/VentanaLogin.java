package src;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    public VentanaLogin() {
        setTitle("Login - RentaCar");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lUsuario = new JLabel("Usuario:");
        JTextField tUsuario = new JTextField();

        JLabel lContrasena = new JLabel("Contraseña:");
        JPasswordField tContrasena = new JPasswordField();

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegistro = new JButton("Registrarse");

        // Acción del botón de login
        btnLogin.addActionListener(e -> {
            String usuario = tUsuario.getText();
            String contrasena = new String(tContrasena.getPassword());

            if (usuario.equals("admin") && contrasena.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión correcto. ¡Bienvenido a RentaCar!");
                dispose();
                new VentanaPrincipal().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        });

        // Acción del botón de registro
        btnRegistro.addActionListener(e -> new VentanaRegistro().setVisible(true));

        add(new JLabel("Usuario:")); add(tUsuario);
        add(new JLabel("Contraseña:")); add(tContrasena);
        add(btnLogin); add(btnRegistro);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}

