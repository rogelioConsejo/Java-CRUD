package CRUD;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CRUD {

    private Driver driver = null;
    private Connection conexion;
    private Statement instruccion;
    private ResultSet resultado;

    private String JDBC_driver = "com.mysql.jdbc.Driver";
    private String table = "CRUD";
    private String url = "jdbc:mysql://localhost:3306/" + table + "?useSSL=false";
    private String user = "CRUD";
    private String pass = "E_Vk75bk%%y62aHe";

    //CONSTRUCTORES

    /**
     * Para definir los parámetros a usar para la conexión.
     *
     * @param JDBC_driver          driver de JDBC. Para MySQL suele ser "com.mysql.jdbc.Driver"
     * @param ubicacionBaseDeDatos dónde se encuentra la Base de Datos, usualmente "localhost:3306"
     * @param table                nombre de la tabla a usar
     * @param user                 usuario
     * @param pass                 contraseña
     */
    @Contract(pure = true)
    public CRUD(String JDBC_driver, String ubicacionBaseDeDatos, String table, String user, String pass) {
        this.JDBC_driver = JDBC_driver;
        this.table = table;
        this.url = "jdbc:mysql://" + ubicacionBaseDeDatos + "/" + table + "?useSSL=false";
        this.user = user;
        this.pass = pass;
    }

    /**
     * Se puede inicializar con valores por defecto.
     */
    @Contract(pure = true)
    public CRUD() {

    }

    //MÉTODOS PÚBLICOS
    public boolean crearEntrada(String nombre, String apellido) {
        String comando = "INSERT INTO " + table + " (nombre, apellido) VALUES (\"" + nombre + "\", \"" + apellido + "\");";
        return ejecutarInstruccion(comando);
    }

    /**
     * método genérico para insertar una entrada en la tabla
     *
     * @param entrada Hashmap que representa la entrada. Las llaves son los nombres de las columnas y los valores son valores.
     */
    public void crearEntrada(HashMap<String, String> entrada) {
        if (entrada.size() > 0) {
            StringBuilder columnas = new StringBuilder();
            StringBuilder valores = new StringBuilder();

            for (String columna : entrada.keySet()) {
                columnas.append(columna).append(", ");
            }
            //Borramos el espacio y la coma extra al final de "columnas"
            columnas.setLength(columnas.length() - 2);

            for (Object valor : entrada.values()) {
                valores.append('"').append(valor).append('"').append(", ");
            }
            //Borramos el espacio y la coma extra al final de "valores"
            valores.setLength(valores.length() - 2);

            String comando = "INSERT INTO " + table + " (" + columnas + ") VALUES (" + valores + ");";
            ejecutarInstruccion(comando);
        } else {
            System.out.println("No se puede crear una entrada sin valores.");
        }
    }

    /**
     * Lee todos los elementos de la tabla.
     *
     * @return "true" si se ejecutó sin errores, en caso contrario, "false"
     */
    public ArrayList<HashMap<String, String>> leerTodos() {
        String comando = "SELECT * FROM " + table + ";";

        //Nos aseguramos de que devolvemos un resultado no-nulo
        return resultSet2ArrayDeHashMaps(ejecutarInstruccion(comando, true));
    }


    /**
     * Cierra las conexiones, statements, y resultsets abiertos, si existen.
     */
    public void cerrar() {
        cerrar(resultado);
        cerrar(instruccion);
        cerrar(conexion);
    }

    //MÉTODOS PRIVADOS
    private ResultSet ejecutarInstruccion(String query, boolean retorna) {
        if (!retorna) {
            ejecutarInstruccion(query);
            return null;
        }
        try {
            conexion = conectar();
            assert conexion != null;
            instruccion = conexion.createStatement();


            return instruccion.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean ejecutarInstruccion(String query) {
        try {
            conexion = conectar();
            assert conexion != null;
            instruccion = conexion.createStatement();
            instruccion.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private synchronized Connection conectar() throws SQLException {
        try {
            if (driver == null) {
                Class jdbcDriver = Class.forName(JDBC_driver);
                driver = (Driver) jdbcDriver.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(driver);
            }
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("No se pudo registrar el Driver JDBC");
            e.printStackTrace();
            return null;
        }
    }

    private void cerrar(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrar(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrar(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrar(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<HashMap<String, String>> resultSet2ArrayDeHashMaps(@NotNull ResultSet resultSet) {
        try {
            ArrayList<HashMap<String, String>> resultado = new ArrayList<>();
            int columnas = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                HashMap<String, String> entrada = new HashMap<>();
                for (int i = 1; i <= columnas; i++) {
                    entrada.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                }
                resultado.add(entrada);
            }

            return resultado;

        } catch (SQLException e) {
            System.out.println("No se pudo convertir el ResultSet a Array de HashMaps");
            e.printStackTrace();
            return null;
        }
    }
}
