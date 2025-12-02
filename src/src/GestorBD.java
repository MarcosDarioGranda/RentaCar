package src;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GestorBD {

    private static String NOMBRE_DRIVER;
    private static String ARCHIVO_BD;
    private static String CADENA_CONEXION;

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (FileInputStream entrada = new FileInputStream("parametros.properties")) {
            propiedades.load(entrada);
            NOMBRE_DRIVER = propiedades.getProperty("DRIVER_NAME", "org.sqlite.JDBC");
            ARCHIVO_BD = propiedades.getProperty("DATABASE_FILE", "rentacar.db");
            CADENA_CONEXION = propiedades.getProperty("CONNECTION_STRING", "jdbc:sqlite:") + ARCHIVO_BD;
            
            System.out.println("Configuración cargada correctamente");
            System.out.println("  - Driver: " + NOMBRE_DRIVER);
            System.out.println("  - Base de datos: " + ARCHIVO_BD);
            System.out.println("  - Conexión: " + CADENA_CONEXION);
        } catch (IOException e) {
            System.err.println("No se pudo cargar parametros.properties. Usando valores por defecto.");
            NOMBRE_DRIVER = "org.sqlite.JDBC";
            ARCHIVO_BD = "rentacar.db";
            CADENA_CONEXION = "jdbc:sqlite:" + ARCHIVO_BD;
        }
    }

    public GestorBD() {
        try {
            System.out.println("Cargando driver SQLite...");
            Class.forName(NOMBRE_DRIVER);
            System.out.println("Driver SQLite cargado OK");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se pudo cargar el driver SQLite");
            e.printStackTrace();
        }

        crearTablas();
        System.out.println("Base de datos lista para usar");
    }

    private void crearTablas() {
        crearTablaUsuarios();
        crearTablaCoches();
        crearTablaVentas();
    }

    // ----------------- CREAR TABLAS -----------------
    
    private void crearTablaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS USUARIOS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USUARIO TEXT UNIQUE NOT NULL," +
                "CONTRASENA TEXT NOT NULL," +
                "FECHA_REGISTRO TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        
        ejecutarSQL(sql, "USUARIOS");
    }

    private void crearTablaCoches() {
        String sql = "CREATE TABLE IF NOT EXISTS COCHES (" +
                "MATRICULA TEXT PRIMARY KEY," +
                "MARCA TEXT NOT NULL," +
                "MODELO TEXT NOT NULL," +
                "ANIO INTEGER NOT NULL," +
                "PRECIO REAL NOT NULL," +
                "FECHA_REGISTRO TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        
        ejecutarSQL(sql, "COCHES");
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
        
        ejecutarSQL(sql, "VENTAS");
    }

    private void ejecutarSQL(String sql, String nombreTabla) {
        try (Connection conexion = obtenerConexion();
             Statement sentencia = conexion.createStatement()) {
            sentencia.execute(sql);
            System.out.println("  -> Tabla " + nombreTabla + " OK");
        } catch (SQLException e) {
            System.err.println("  -> Error al crear tabla " + nombreTabla);
            e.printStackTrace();
        }
    }


    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(CADENA_CONEXION);
    }

    // ----------------- MÉTODOS PARA USUARIOS -----------------
    
    public boolean insertarUsuario(String usuario, String contrasena) {
        String sql = "INSERT INTO USUARIOS(USUARIO, CONTRASENA) VALUES (?, ?);";
        
        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, usuario);
            sentencia.setString(2, contrasena);
            sentencia.executeUpdate();
            
            System.out.println("Usuario '" + usuario + "' registrado correctamente");
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("El usuario '" + usuario + "' ya existe");
            } else {
                System.err.println("Error al insertar usuario: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean validarUsuario(String usuario, String contrasena) {
        String sql = "SELECT * FROM USUARIOS WHERE USUARIO = ? AND CONTRASENA = ?;";
        
        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, usuario);
            sentencia.setString(2, contrasena);

            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ----------------- MÉTODOS PARA COCHES -----------------
    
    public boolean insertarCoche(Coche coche) {
        String sql = "INSERT INTO COCHES(MATRICULA, MARCA, MODELO, ANIO, PRECIO) VALUES (?, ?, ?, ?, ?);";
        
        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, coche.getMatricula());
            sentencia.setString(2, coche.getMarca());
            sentencia.setString(3, coche.getModelo());
            sentencia.setInt(4, coche.getAnio());
            sentencia.setDouble(5, coche.getPrecio());
            sentencia.executeUpdate();
            
            System.out.println("Coche '" + coche.getMatricula() + "' agregado correctamente");
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Ya existe un coche con matrícula '" + coche.getMatricula() + "'");
            } else {
                System.err.println("Error al insertar coche: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    public List<Coche> obtenerCoches() {
        List<Coche> listaCoches = new ArrayList<>();
        String sql = "SELECT * FROM COCHES ORDER BY FECHA_REGISTRO DESC;";
        
        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                listaCoches.add(new Coche(
                        resultado.getString("MATRICULA"),
                        resultado.getString("MARCA"),
                        resultado.getString("MODELO"),
                        resultado.getInt("ANIO"),
                        resultado.getDouble("PRECIO")
                ));
            }
            
            System.out.println("Se obtuvieron " + listaCoches.size() + " coches de la BD");

        } catch (SQLException e) {
            System.err.println("Error al obtener coches: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaCoches;
    }

    public boolean eliminarCoche(String matricula) {
        String sql = "DELETE FROM COCHES WHERE MATRICULA = ?;";
        
        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, matricula);
            int filasAfectadas = sentencia.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Coche '" + matricula + "' eliminado correctamente");
                return true;
            } else {
                System.err.println("No se encontró el coche con matrícula '" + matricula + "'");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar coche: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Coche buscarCochePorMatricula(String matricula) {
        String sql = "SELECT * FROM COCHES WHERE MATRICULA = ?;";
        
        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, matricula);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new Coche(
                            resultado.getString("MATRICULA"),
                            resultado.getString("MARCA"),
                            resultado.getString("MODELO"),
                            resultado.getInt("ANIO"),
                            resultado.getDouble("PRECIO")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar coche: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    // ----------------- MÉTODOS PARA VENTAS -----------------
    
    public boolean registrarVenta(Venta venta) {
        String sql = "INSERT INTO VENTAS(MATRICULA, CLIENTE, VEHICULO, DNI, IMPORTE) VALUES(?,?,?,?,?);";
        
        Connection conexion = null;
        try {
            conexion = obtenerConexion();
            conexion.setAutoCommit(false); 

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setString(1, venta.getMatricula());
                sentencia.setString(2, venta.getCliente());
                sentencia.setString(3, venta.getVehiculo());
                sentencia.setString(4, venta.getDni());
                sentencia.setDouble(5, venta.getImporte());
                sentencia.executeUpdate();
            }
            String sqlEliminar = "DELETE FROM COCHES WHERE MATRICULA = ?;";
            try (PreparedStatement sentenciaEliminar = conexion.prepareStatement(sqlEliminar)) {
                sentenciaEliminar.setString(1, venta.getMatricula());
                sentenciaEliminar.executeUpdate();
            }

            System.out.println("Venta registrada y coche eliminado del inventario");
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); 
                    System.err.println("Operación cancelada, no se hicieron cambios");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error al registrar venta: " + e.getMessage());
            e.printStackTrace();
            return false;
            
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Venta> obtenerVentas() {
        List<Venta> listaVentas = new ArrayList<>();
        String sql = "SELECT * FROM VENTAS ORDER BY FECHA DESC;";
        
        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                listaVentas.add(new Venta(
                        resultado.getString("MATRICULA"),
                        resultado.getString("CLIENTE"),
                        resultado.getString("VEHICULO"),
                        resultado.getString("DNI"),
                        resultado.getDouble("IMPORTE")
                ));
            }
            
            System.out.println("Se obtuvieron " + listaVentas.size() + " ventas de la BD");

        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaVentas;
    }

    // ----------------- MÉTODOS AUXILIARES -----------------

    public boolean verificarConexion() {
        try (Connection conexion = obtenerConexion()) {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
            return false;
        }
    }

    public void reiniciarBaseDatos() {
        System.out.println("ADVERTENCIA: Reiniciando base de datos...");
        
        try (Connection conexion = obtenerConexion();
             Statement sentencia = conexion.createStatement()) {
            
            sentencia.execute("DROP TABLE IF EXISTS VENTAS;");
            sentencia.execute("DROP TABLE IF EXISTS COCHES;");
            sentencia.execute("DROP TABLE IF EXISTS USUARIOS;");
            
            System.out.println("Tablas antiguas eliminadas");
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar tablas: " + e.getMessage());
            e.printStackTrace();
        }
        
        crearTablas();
        System.out.println("Base de datos reiniciada correctamente");
    }

    public void insertarDatosPrueba() {
        System.out.println("Insertando datos de prueba...");

        insertarUsuario("admin", "1234");
        insertarUsuario("juan", "password");
        insertarCoche(new Coche("1234ABC", "Toyota", "Corolla", 2020, 18000));
        insertarCoche(new Coche("5678DEF", "BMW", "X5", 2021, 45000));
        insertarCoche(new Coche("9012GHI", "Ford", "Focus", 2019, 15000));
        insertarCoche(new Coche("3456JKL", "Seat", "Ibiza", 2022, 16500));
        insertarCoche(new Coche("7890MNO", "Audi", "A4", 2023, 38000));
        
        System.out.println("Datos de prueba insertados correctamente");
    }
}