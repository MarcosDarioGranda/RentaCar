package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

        // 🔹 Método auxiliar para ejecutar el login
        Runnable ejecutarLogin = () -> {
            String usuario = tUsuario.getText();
            String contrasena = new String(tContrasena.getPassword());

            if (usuario.equals("admin") && contrasena.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión correcto. ¡Bienvenido a RentaCar!");
                dispose();
                new VentanaPrincipal().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        };

        // Acción del botón de login
        btnLogin.addActionListener(e -> ejecutarLogin.run());

        // Acción del botón de registro
        btnRegistro.addActionListener(e -> new VentanaRegistro().setVisible(true));

        // 🔹 Evento de teclado: presionar Enter ejecuta el login
        KeyAdapter keyEnterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ejecutarLogin.run();
                }
            }
        };

        // Añadimos el listener a los campos de texto
        tUsuario.addKeyListener(keyEnterListener);
        tContrasena.addKeyListener(keyEnterListener);

        add(lUsuario); add(tUsuario);
        add(lContrasena); add(tContrasena);
        add(btnLogin); add(btnRegistro);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
