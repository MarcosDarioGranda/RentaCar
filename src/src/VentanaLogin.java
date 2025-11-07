package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin extends JFrame {

    public VentanaLogin() {
        setTitle("Login - RentaCar");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo y márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título
        JLabel titulo = new JLabel("Inicio de Sesión", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(40, 70, 130));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Panel del formulario
        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 10));
        formulario.setOpaque(false);

        JLabel lUsuario = new JLabel("Usuario:");
        JTextField tUsuario = new JTextField();

        JLabel lContrasena = new JLabel("Contraseña:");
        JPasswordField tContrasena = new JPasswordField();

        Font fuenteCampos = new Font("Segoe UI", Font.PLAIN, 14);
        lUsuario.setFont(fuenteCampos);
        lContrasena.setFont(fuenteCampos);
        tUsuario.setFont(fuenteCampos);
        tContrasena.setFont(fuenteCampos);

        formulario.add(lUsuario); formulario.add(tUsuario);
        formulario.add(lContrasena); formulario.add(tContrasena);

        //  Panel de botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        botones.setOpaque(false);

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegistro = new JButton("Registrarse");

        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnRegistro.setBackground(new Color(220, 53, 69));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setFocusPainted(false);
        btnRegistro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistro.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        botones.add(btnLogin);
        botones.add(btnRegistro);

        // 🔹 Método auxiliar para ejecutar el login
        Runnable ejecutarLogin = () -> {
            String usuario = tUsuario.getText();
            String contrasena = new String(tContrasena.getPassword());

            if (usuario.equals("admin") && contrasena.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión correcto. ¡Bienvenido a RentaCar!");
                dispose();
                new VentanaPrincipal().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        // Acción de los botones
        btnLogin.addActionListener(e -> ejecutarLogin.run());
        btnRegistro.addActionListener(e -> new VentanaRegistro().setVisible(true));

        // 🔹 Enter ejecuta el login
        KeyAdapter keyEnterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ejecutarLogin.run();
                }
            }
        };

        tUsuario.addKeyListener(keyEnterListener);
        tContrasena.addKeyListener(keyEnterListener);

        // 🧩 Estructura final
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(formulario, BorderLayout.CENTER);
        panelPrincipal.add(botones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
