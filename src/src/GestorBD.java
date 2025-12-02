package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBD {

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB = "RentaCar.db";
    private static final String URL = "jdbc:sqlite:" + DB;

    public GestorBD() {
        try {
            Class.forName(DRIVER);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el driver SQLite");
        }

        crearTablaUsuarios();
        crearTablaCoches();
        crearTablaVentas();
    }

    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // ---------------- TABLAS ----------------

    private void crearTablaUsuarios() {
        String sql = """
            CREATE TABLE IF NOT EXISTS USUARIOS(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                USUARIO TEXT UNIQUE NOT NULL,
                CONTRASENA TEXT NOT NULL
            );
        """;

        ejecutar(sql);
    }

    private void crearTablaCoches() {
        String sql = """
            CREATE TABLE IF NOT EXISTS COCHES(
                MATRICULA TEXT PRIMARY KEY,
                MARCA TEXT NOT NULL,
                MODELO TEXT NOT NULL,
                ANIO INTEGER NOT NULL,
                PRECIO REAL NOT NULL
            );
        """;

        ejecutar(sql);
    }

    private void crearTablaVentas() {
        String sql = """
            CREATE TABLE IF NOT EXISTS VENTAS(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                MATRICULA TEXT NOT NULL,
                CLIENTE TEXT NOT NULL,
                VEHICULO TEXT NOT NULL,
                DNI TEXT NOT NULL,
                IMPORTE REAL NOT NULL,
                FECHA TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (MATRICULA) REFERENCES COCHES(MATRICULA)
            );
        """;

        ejecutar(sql);
    }

    private void ejecutar(String sql) {
        try (Connection c = obtenerConexion(); Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (Exception ignored) {}
    }

    // ---------------- USUARIOS ----------------

    public boolean insertarUsuario(String usuario, String contrasena) {
        String sql = "INSERT INTO USUARIOS(USUARIO, CONTRASENA) VALUES(?,?)";

        try (Connection c = obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean validarUsuario(String usuario, String contrasena) {
        String sql = "SELECT 1 FROM USUARIOS WHERE USUARIO=? AND CONTRASENA=?";

        try (Connection c = obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }

    // ---------------- COCHES ----------------

    public boolean insertarCoche(Coche coche) {
        String sql = """
            INSERT INTO COCHES(MATRICULA, MARCA, MODELO, ANIO, PRECIO)
            VALUES(?,?,?,?,?)
        """;

        try (Connection c = obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, coche.getMatricula());
            ps.setString(2, coche.getMarca());
            ps.setString(3, coche.getModelo());
            ps.setInt(4, coche.getAnio());
            ps.setDouble(5, coche.getPrecio());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    public List<Coche> obtenerCoches() {
        List<Coche> lista = new ArrayList<>();
        String sql = "SELECT * FROM COCHES";

        try (Connection c = obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Coche(
                    rs.getString("MATRICULA"),
                    rs.getString("MARCA"),
                    rs.getString("MODELO"),
                    rs.getInt("ANIO"),
                    rs.getDouble("PRECIO")
                ));
            }

        } catch (SQLException ignored) {}

        return lista;
    }

    public boolean eliminarCoche(String matricula) {
        String sql = "DELETE FROM COCHES WHERE MATRICULA=?";

        try (Connection c = obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    // ---------------- VENTAS ----------------

    public boolean registrarVenta(Venta v) {
        String sql = """
            INSERT INTO VENTAS(MATRICULA, CLIENTE, VEHICULO, DNI, IMPORTE)
            VALUES(?,?,?,?,?)
        """;

        try (Connection c = obtenerConexion()) {
            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, v.getMatricula());
                ps.setString(2, v.getCliente());
                ps.setString(3, v.getVehiculo());
                ps.setString(4, v.getDni());
                ps.setDouble(5, v.getImporte());
                ps.executeUpdate();
            }

            eliminarCoche(v.getMatricula());

            c.commit();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }
}
