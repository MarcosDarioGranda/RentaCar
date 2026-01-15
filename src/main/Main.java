package main;

import javax.swing.SwingUtilities;

import gui.VentanaLogin;

public class Main {

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
