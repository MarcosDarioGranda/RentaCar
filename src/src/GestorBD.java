package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBD {

    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    private static final String CONNECTION_STRING = "jdbc:sqlite:RentaCar.db";

    public GestorBD() {
        System.out.println("Ruta real de la base de datos: " +
                new java.io.File("RentaCar.db").getAbsolutePath());
        try {
            System.out.println("Intentando cargar driver SQLite...");
            Class.forName(DRIVER_NAME);
            System.out.println("Driver SQLite cargado correctamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se pudo cargar el driver SQLite.");
            e.printStackTrace();
        }

        crearTablaUsuarios();
        crearTablaCoches();
        crearTablaVentas();
        crearTablaFichaTecnica();
    }

    // ================= NORMALIZACIÓN =================
    
    /**
     * Normaliza una matrícula: elimina espacios y convierte a mayúsculas
     */
    private String normalizarMatricula(String matricula) {
        if (matricula == null) return null;
        String normalizada = matricula.trim().toUpperCase().replaceAll("\\s+", "");
        System.out.println("Matrícula normalizada: '" + matricula + "' -> '" + normalizada + "'");
        return normalizada;
    }

    // ----------------- TABLAS -----------------

    private void crearTablaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS USUARIOS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USUARIO TEXT UNIQUE NOT NULL," +
                "CONTRASENA TEXT NOT NULL);";
        ejecutarSQL(sql);
    }

    private void crearTablaCoches() {
        String sql = "CREATE TABLE IF NOT EXISTS COCHES (" +
                "MATRICULA TEXT PRIMARY KEY," +
                "MARCA TEXT NOT NULL," +
                "MODELO TEXT NOT NULL," +
                "ANIO INTEGER NOT NULL," +
                "PRECIO REAL NOT NULL);";
        ejecutarSQL(sql);
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
        ejecutarSQL(sql);
    }

    private void crearTablaFichaTecnica() {
        String sql = "CREATE TABLE IF NOT EXISTS FICHA_TECNICA (" +
                "MATRICULA TEXT PRIMARY KEY," +
                "CILINDRADA TEXT," +
                "POTENCIA TEXT," +
                "CONSUMO TEXT," +
                "BATALLA TEXT," +
                "TRANSMISION TEXT," +
                "FOREIGN KEY (MATRICULA) REFERENCES COCHES(MATRICULA));";
        ejecutarSQL(sql);
    }

    private void ejecutarSQL(String sql) {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) { e.printStackTrace(); }
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
            e.printStackTrace();
            return false;
        }
    }

    // ----------------- COCHES -----------------

    public boolean insertarCoche(Coche c) {
        String sql = "INSERT INTO COCHES VALUES (?, ?, ?, ?, ?);";
        String matriculaNormalizada = normalizarMatricula(c.getMatricula());
        
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, matriculaNormalizada);
            pstmt.setString(2, c.getMarca());
            pstmt.setString(3, c.getModelo());
            pstmt.setInt(4, c.getAnio());
            pstmt.setDouble(5, c.getPrecio());
            pstmt.executeUpdate();
            
            System.out.println("✓ Coche insertado con matrícula: " + matriculaNormalizada);
            return true;

        } catch (SQLException e) {
            System.err.println("✗ ERROR al insertar coche: " + e.getMessage());
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

        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean eliminarCoche(String matricula) {
        String sql = "DELETE FROM COCHES WHERE MATRICULA = ?;";
        String matriculaNormalizada = normalizarMatricula(matricula);
        
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matriculaNormalizada);
            boolean eliminado = ps.executeUpdate() > 0;
            
            if (eliminado) {
                System.out.println("✓ Coche eliminado: " + matriculaNormalizada);
            } else {
                System.out.println("✗ No se encontró el coche: " + matriculaNormalizada);
            }
            
            return eliminado;

        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // ----------------- FICHA TÉCNICA -----------------

    public FichaTecnica obtenerFicha(String matricula) {
        String sql = "SELECT * FROM FICHA_TECNICA WHERE MATRICULA = ?";
        String matriculaNormalizada = normalizarMatricula(matricula);

        System.out.println("=== BUSCANDO FICHA TÉCNICA ===");
        System.out.println("Matrícula a buscar: '" + matriculaNormalizada + "'");

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matriculaNormalizada);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("✓ Ficha técnica ENCONTRADA para: " + matriculaNormalizada);
                return new FichaTecnica(
                        rs.getString("CILINDRADA"),
                        rs.getString("POTENCIA"),
                        rs.getString("CONSUMO"),
                        rs.getString("BATALLA"),
                        rs.getString("TRANSMISION")
                );
            } else {
                System.out.println("✗ NO existe ficha técnica para: " + matriculaNormalizada);
            }

        } catch (SQLException e) { 
            System.err.println("✗ Error SQL al buscar ficha: " + e.getMessage());
            e.printStackTrace(); 
        }

        return null;
    }

    public boolean insertarFichaTecnica(String matricula, String cilindrada, String potencia,
                                        String consumo, String batalla, String transmision) {
        
        String matriculaNormalizada = normalizarMatricula(matricula);
        
        System.out.println("=== INSERTANDO FICHA TÉCNICA ===");
        System.out.println("Matrícula normalizada: '" + matriculaNormalizada + "'");
        
        // PRIMERO: Verificar que el coche existe en COCHES
        String sqlVerificar = "SELECT MATRICULA FROM COCHES WHERE MATRICULA = ?";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement psVerif = con.prepareStatement(sqlVerificar)) {
            
            psVerif.setString(1, matriculaNormalizada);
            ResultSet rs = psVerif.executeQuery();
            
            if (!rs.next()) {
                System.err.println("✗ ERROR: La matrícula '" + matriculaNormalizada + "' NO existe en COCHES");
                
                // Mostrar todas las matrículas disponibles
                System.out.println("Matrículas en la base de datos:");
                String sqlTodas = "SELECT MATRICULA FROM COCHES";
                PreparedStatement psTodas = con.prepareStatement(sqlTodas);
                ResultSet rsTodas = psTodas.executeQuery();
                while (rsTodas.next()) {
                    System.out.println("  - '" + rsTodas.getString("MATRICULA") + "'");
                }
                rsTodas.close();
                psTodas.close();
                
                return false;
            } else {
                System.out.println("✓ El coche existe en COCHES");
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("✗ Error al verificar coche:");
            e.printStackTrace();
            return false;
        }

        // SEGUNDO: Verificar que NO existe ya una ficha
        String sqlVerificarFicha = "SELECT MATRICULA FROM FICHA_TECNICA WHERE MATRICULA = ?";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement psVerif = con.prepareStatement(sqlVerificarFicha)) {
            
            psVerif.setString(1, matriculaNormalizada);
            ResultSet rs = psVerif.executeQuery();
            
            if (rs.next()) {
                System.err.println("✗ ERROR: Ya existe una ficha técnica para '" + matriculaNormalizada + "'");
                return false;
            } else {
                System.out.println("✓ No existe ficha previa, procediendo a insertar...");
            }
            rs.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // TERCERO: Insertar la ficha técnica
        String sql = "INSERT INTO FICHA_TECNICA (MATRICULA, CILINDRADA, POTENCIA, CONSUMO, BATALLA, TRANSMISION) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matriculaNormalizada);
            ps.setString(2, cilindrada);
            ps.setString(3, potencia);
            ps.setString(4, consumo);
            ps.setString(5, batalla);
            ps.setString(6, transmision);

            ps.executeUpdate();
            System.out.println("✓✓✓ Ficha técnica insertada correctamente para: " + matriculaNormalizada);
            return true;

        } catch (SQLException e) {
            System.err.println("✗✗✗ ERROR al insertar ficha técnica:");
            System.err.println("Matrícula: " + matriculaNormalizada);
            System.err.println("Mensaje SQL: " + e.getMessage());
            System.err.println("Código error: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarFichaTecnica(String matricula) {
        String sql = "DELETE FROM FICHA_TECNICA WHERE MATRICULA = ?";
        String matriculaNormalizada = normalizarMatricula(matricula);
        
        System.out.println("=== ELIMINANDO FICHA TÉCNICA ===");
        System.out.println("Matrícula normalizada: '" + matriculaNormalizada + "'");
        
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, matriculaNormalizada);
            int deleted = ps.executeUpdate();
            
            if (deleted > 0) {
                System.out.println("✓ Ficha técnica eliminada correctamente");
                return true;
            } else {
                System.out.println("✗ No se encontró ficha técnica para eliminar");
                
                // Mostrar fichas existentes
                String sqlTodas = "SELECT MATRICULA FROM FICHA_TECNICA";
                PreparedStatement psTodas = con.prepareStatement(sqlTodas);
                ResultSet rs = psTodas.executeQuery();
                System.out.println("Fichas técnicas en la base de datos:");
                while (rs.next()) {
                    System.out.println("  - '" + rs.getString("MATRICULA") + "'");
                }
                rs.close();
                psTodas.close();
                
                return false;
            }
            
        } catch (SQLException e) { 
            System.err.println("✗ Error al eliminar ficha: " + e.getMessage());
            e.printStackTrace(); 
            return false; 
        }
    }

    // ----------------- VENTAS -----------------

    public boolean registrarVenta(Venta v) {
        String sql = "INSERT INTO VENTAS VALUES(NULL,?,?,?,?,?);";
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, normalizarMatricula(v.getMatricula()));
            ps.setString(2, v.getCliente());
            ps.setString(3, v.getVehiculo());
            ps.setString(4, v.getDni());
            ps.setDouble(5, v.getImporte());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }


    // ----------------- MODIFICAR DATOS INVENTARIO -----------------

	public boolean actualizarCocheExceptoMarca(String matricula, String nuevoModelo, int nuevoAnio, double nuevoPrecio) {
	    String sql = "UPDATE COCHES SET MODELO = ?, ANIO = ?, PRECIO = ? WHERE MATRICULA = ?";
	    String matriculaNormalizada = normalizarMatricula(matricula);
	
	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement ps = con.prepareStatement(sql)) {
	
	        ps.setString(1, nuevoModelo);
	        ps.setInt(2, nuevoAnio);
	        ps.setDouble(3, nuevoPrecio);
	        ps.setString(4, matriculaNormalizada);
	
	        int updated = ps.executeUpdate();
	        if (updated > 0) {
	            System.out.println("✓ Coche actualizado (excepto marca): " + matriculaNormalizada);
	            return true;
	        } else {
	            System.out.println("✗ No se encontró coche para actualizar: " + matriculaNormalizada);
	            return false;
	        }
	
	    } catch (SQLException e) {
	        System.err.println("✗ Error al actualizar coche: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean actualizarModelo(String matricula, String nuevoModelo) {
	    String sql = "UPDATE COCHES SET MODELO = ? WHERE MATRICULA = ?";
	    String matriculaNormalizada = normalizarMatricula(matricula);
	
	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement ps = con.prepareStatement(sql)) {
	
	        ps.setString(1, nuevoModelo);
	        ps.setString(2, matriculaNormalizada);
	
	        int updated = ps.executeUpdate();
	        return updated > 0;
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean actualizarAnio(String matricula, int nuevoAnio) {
	    String sql = "UPDATE COCHES SET ANIO = ? WHERE MATRICULA = ?";
	    String matriculaNormalizada = normalizarMatricula(matricula);
	
	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement ps = con.prepareStatement(sql)) {
	
	        ps.setInt(1, nuevoAnio);
	        ps.setString(2, matriculaNormalizada);
	
	        int updated = ps.executeUpdate();
	        return updated > 0;
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean actualizarPrecio(String matricula, double nuevoPrecio) {
	    String sql = "UPDATE COCHES SET PRECIO = ? WHERE MATRICULA = ?";
	    String matriculaNormalizada = normalizarMatricula(matricula);
	
	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement ps = con.prepareStatement(sql)) {
	
	        ps.setDouble(1, nuevoPrecio);
	        ps.setString(2, matriculaNormalizada);
	
	        int updated = ps.executeUpdate();
	        return updated > 0;
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
