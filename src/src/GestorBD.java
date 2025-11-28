package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBD {

    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    private static final String CONNECTION_STRING = "jdbc:sqlite:RentaCar.db";


    public GestorBD() {
        try {
            System.out.println("Intentando cargar driver SQLite...");
            Class.forName(DRIVER_NAME);
            System.out.println("Driver SQLite cargado correctamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se pudo cargar el driver SQLite. Clase no encontrada.");
            e.printStackTrace();
            return;
        }

        try {
            System.out.println("Creando tabla USUARIOS...");
            crearTablaUsuarios();
            System.out.println("Tabla USUARIOS creada o ya existía.");
        } catch (Exception e) {
            System.err.println("ERROR al crear tabla USUARIOS:");
            e.printStackTrace();
        }

        try {
            System.out.println("Creando tabla COCHES...");
            crearTablaCoches();
            System.out.println("Tabla COCHES creada o ya existía.");
        } catch (Exception e) {
            System.err.println("ERROR al crear tabla COCHES:");
            e.printStackTrace();
        }

        try {
            System.out.println("Creando tabla VENTAS...");
            crearTablaVentas();
            System.out.println("Tabla VENTAS creada o ya existía.");
        } catch (Exception e) {
            System.err.println("ERROR al crear tabla VENTAS:");
            e.printStackTrace();
        }
        
       
    }

    // ----------------- TABLAS -----------------
    private void crearTablaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS USUARIOS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USUARIO TEXT UNIQUE NOT NULL," +
                "CONTRASENA TEXT NOT NULL);";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQLException en crearTablaUsuarios:");
            e.printStackTrace();
        }
    }

    private void crearTablaCoches() {
        String sql = "CREATE TABLE IF NOT EXISTS COCHES (" +
                "MATRICULA TEXT PRIMARY KEY," +
                "MARCA TEXT NOT NULL," +
                "MODELO TEXT NOT NULL," +
                "ANIO INTEGER NOT NULL," +
                "PRECIO REAL NOT NULL);";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQLException en crearTablaCoches:");
            e.printStackTrace();
        }
    }

    private void crearTablaVentas() {
        String sql = "CREATE TABLE IF NOT EXISTS VENTAS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "MATRICULA TEXT NOT NULL," +
                "CLIENTE TEXT NOT NULL," +
                "VEHICULO TEXT NOT NULL," +
                "DNI TEXT NOT NULL," +
                "IMPORTE REAL NOT NULL," +
                "FECHA TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(MATRICULA) REFERENCES COCHES(MATRICULA));";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQLException en crearTablaVentas:");
            e.printStackTrace();
        }
    }

    // ----------------- USUARIOS -----------------
    public boolean insertarUsuario(String usuario, String contrasena) {
        String sql = "INSERT INTO USUARIOS(USUARIO, CONTRASENA) VALUES (?, ?);";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, contrasena);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarUsuario(String usuario, String contrasena) {
        String sql = "SELECT * FROM USUARIOS WHERE USUARIO = ? AND CONTRASENA = ?;";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, contrasena);

            ResultSet rs = pstmt.executeQuery();
            boolean existe = rs.next();
            rs.close();
            return existe;

        } catch (SQLException e) {
            System.err.println("Error al validar usuario:");
            e.printStackTrace();
            return false;
        }
    }

    // ----------------- COCHES -----------------
    public boolean insertarCoche(Coche c) {
        String sql = "INSERT INTO COCHES(MATRICULA, MARCA, MODELO, ANIO, PRECIO) VALUES (?, ?, ?, ?, ?);";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, c.getMatricula());
            pstmt.setString(2, c.getMarca());
            pstmt.setString(3, c.getModelo());
            pstmt.setInt(4, c.getAnio());
            pstmt.setDouble(5, c.getPrecio());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar coche:");
            e.printStackTrace();
            return false;
        }
    }

    public List<Coche> obtenerCoches() {
        List<Coche> lista = new ArrayList<>();
        String sql = "SELECT * FROM COCHES;";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Coche(
                        rs.getString("MATRICULA"),
                        rs.getString("MARCA"),
                        rs.getString("MODELO"),
                        rs.getInt("ANIO"),
                        rs.getDouble("PRECIO")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener coches:");
            e.printStackTrace();
        }
        return lista;
    }

    // ----------------- VENTAS -----------------
    public boolean registrarVenta(Venta v) {
        String sql = "INSERT INTO VENTAS(MATRICULA, CLIENTE, VEHICULO, DNI, IMPORTE) VALUES(?,?,?,?,?);";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getCliente());
            ps.setString(3, v.getVehiculo());
            ps.setString(4, v.getDni());
            ps.setDouble(5, v.getImporte());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar venta:");
            e.printStackTrace();
            return false;
        }
    }
    



}
