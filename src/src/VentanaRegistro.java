package src;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistro extends JFrame {

    public VentanaRegistro() {
        setTitle("Registro - RentaCar");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 8, 8));

        JLabel lUsuario = new JLabel("Usuario:");
        JTextField tUsuario = new JTextField();

        JLabel lCorreo = new JLabel("Correo:");
        JTextField tCorreo = new JTextField();

        JLabel lContrasena = new JLabel("Contraseña:");
        JPasswordField tContrasena = new JPasswordField();

        JLabel lConfirmar = new JLabel("Confirmar contraseña:");
        JPasswordField tConfirmar = new JPasswordField();

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRegistrar.addActionListener(e -> {
            String usuario = tUsuario.getText();
            String correo = tCorreo.getText();
            String contrasena = new String(tContrasena.getPassword());
            String confirmar = new String(tConfirmar.getPassword());

            if (usuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.");
                return;
            }

            if (!contrasena.equals(confirmar)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Usuario registrado correctamente:\n" +
                    "Usuario: " + usuario + "\n" +
                    "Correo: " + correo);

            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());

        add(lUsuario); add(tUsuario);
        add(lCorreo); add(tCorreo);
        add(lContrasena); add(tContrasena);
        add(lConfirmar); add(tConfirmar);
        add(btnRegistrar); add(btnCancelar);
    }
}
