package CRUD;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
     * Busca las entradas que cumplan con las condiciones establecidas.
     * @param condiciones       Columnas y sus valores correspondientes a usar como condiciones. Cada entrada representa una condición, unidas por AND.S
     * @return                  entradas encontradas
     */
    public ArrayList<HashMap<String, String>> buscarEntradas(@NotNull HashMap<String, String> condiciones) {
        if (condiciones.size() > 0) {
            ArrayList<HashMap<String, String>> entradasEncontradas = new ArrayList<>();
            StringBuilder strCondiciones = new StringBuilder();

            for (Map.Entry<String, String> condicion: condiciones.entrySet()) {
                strCondiciones.append(condicion.getKey()).append(" = \"").append(condicion.getValue()).append("\"").append(" AND ");
            }

            strCondiciones.setLength(strCondiciones.length() - 5);

            System.out.println(condiciones);
            String comando = "SELECT * FROM " + table + " WHERE " + strCondiciones + ";";
            return Herramientas.resultSet2ArrayDeHashMaps(Objects.requireNonNull(ejecutarInstruccion(comando, true)));
        } else {
            System.out.println("No se puede buscar una entrada sin valores de referencia");
            return null;
        }
    }

    /**
     * Busca las entradas que cumplan con las condiciones establecidas.
     * @param condiciones   {{{condicion} AND {condicion}} OR {{condicion}}}. AND tiene precedencia sobre OR.
     * @return  entradas encontradas
     */
    public ArrayList<HashMap<String, String>> buscarEntradas(@NotNull String[][][] condiciones) {
        if (condiciones.length > 0 && condiciones[0].length >0) {
            ArrayList<HashMap<String, String>> entradasEncontradas = new ArrayList<>();
            StringBuilder strCondiciones = new StringBuilder();

            for (String[][] condicion: condiciones) {
                for (String[] termino : condicion) {
                    strCondiciones.append(termino[0]).append(" = \"").append(termino[1]).append("\"").append(" AND ");
                }
                strCondiciones.setLength(strCondiciones.length() - 5);

                strCondiciones.append(" OR ");
            }

            strCondiciones.setLength(strCondiciones.length() - 4);

            System.out.println(strCondiciones);
            String comando = "SELECT * FROM " + table + " WHERE " + strCondiciones + ";";
            return Herramientas.resultSet2ArrayDeHashMaps(Objects.requireNonNull(ejecutarInstruccion(comando, true)));
        } else {
            System.out.println("No se puede buscar una entrada sin valores de referencia");
            return null;
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
     * Realiza un update a una entrada.
     * @param llavePrimaria la llave primaria de la entrada a modificar [columna, valor]
     * @param cambios       los cambios a realizar {{"columna1"=>"valor1"},{"columna2"=>"valor2"}}
     */
    public void actualizarEntrada(String[] llavePrimaria, HashMap<String, String> cambios) {
        StringBuilder strCambios = new StringBuilder();

        for (Map.Entry<String,String> cambio: cambios.entrySet()) {
            strCambios.append(cambio.getKey()).append("=\"").append(cambio.getValue()).append("\", ");
        }

        strCambios.setLength(strCambios.length()-2);

        String comando = "UPDATE " + table + " SET " + strCambios + " WHERE " + llavePrimaria[0] + "=" + llavePrimaria[1];

        ejecutarInstruccion(comando);
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
