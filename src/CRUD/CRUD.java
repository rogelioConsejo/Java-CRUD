package CRUD;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
     * Se puede inicializar con valores por defecto. Este desaparecerá en el futuro cercano.
     */
    @Contract(pure = true)
    public CRUD() {

    }

    //MÉTODOS PÚBLICOS
    /**
     * método genérico para insertar una entrada en la tabla
     *
     * @param entrada Hashmap que representa la entrada. Las llaves son los nombres de las columnas y los valores son valores.
     */
    public void crearEntrada(HashMap<String, String> entrada) {
        if (entrada.size() > 0) {
            StringBuilder valores = new StringBuilder();
            StringBuilder columnas = new StringBuilder();

            Herramientas.entrada2String(entrada, columnas);
            Herramientas.valores2String(entrada, valores);

            String comando = "INSERT INTO " + table + " (" + columnas + ") VALUES (" + valores + ");";
            ejecutarInstruccion(comando);
        } else {
            System.out.println("No se puede crear una entrada sin valores.");
        }
    }

    /**
     * Lee todos los elementos de la tabla y los retorna como HashMap.
     *
     * @return "true" si se ejecutó sin errores, en caso contrario, "false"
     */
    public ArrayList<HashMap<String, String>> leerTodos() {
        String comando = "SELECT * FROM " + table + ";";

        //Nos aseguramos de que devolvemos un resultado no-nulo
        return Herramientas.resultSet2ArrayDeHashMaps(Objects.requireNonNull(ejecutarInstruccion(comando, true)));
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

    private void ejecutarInstruccion(String query) {
        try {
            conexion = conectar();
            assert conexion != null;
            instruccion = conexion.createStatement();
            instruccion.executeUpdate(query);

        } catch (Exception e) {
            System.out.printf("No se pudo ejecutar el query: %s\n", query);
            e.printStackTrace();
        }
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

}
