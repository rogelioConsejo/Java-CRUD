package CRUD;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Permite generar acciones CRUD para una base de datos.
 */
public class CRUD {
    private String table = "CRUD";
    private Conexion conexion;

    //CONSTRUCTORES
    //No se puede inicializar sin definir los valores para la conexión.
    private CRUD() {

    }

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
        conexion = new Conexion(JDBC_driver, ubicacionBaseDeDatos, table, user, pass);
        this.table = table;
    }

    //MÉTODOS PÚBLICOS
    /**
     * método genérico para insertar una entrada en la tabla
     *
     * @param entrada Hashmap que representa la entrada. Las llaves son los nombres de las columnas y los valores son valores.
     */
    public void crearEntrada(HashMap<String, String> entrada) {
        if (entrada.size() > 0) {
            String columnas = Herramientas.columnas2String(entrada);
            String valores = Herramientas.valores2String(entrada);

            String comando = "INSERT INTO " + table + " (" + columnas + ") VALUES (" + valores + ");";
            conexion.ejecutarInstruccion(comando);
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
            return Herramientas.resultSet2ArrayDeHashMaps(Objects.requireNonNull(conexion.ejecutarInstruccion(comando, true)));
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
            return Herramientas.resultSet2ArrayDeHashMaps(Objects.requireNonNull(conexion.ejecutarInstruccion(comando, true)));
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
        return Herramientas.resultSet2ArrayDeHashMaps(Objects.requireNonNull(conexion.ejecutarInstruccion(comando, true)));
    }

    /**
     * Realiza un update a una entrada.
     * @param llavePrimaria la llave primaria de la entrada a modificar [columna, valor]
     * @param cambios       los cambios a realizar {{"columna1"=>"valor1"},{"columna2"=>"valor2"}}
     */
    public void actualizarEntrada(String[] llavePrimaria, HashMap<String, String> cambios) {
        if (llavePrimaria.length == 2) {
            String strCambios = Herramientas.entradas2String(cambios);
            String columnaLlavePrimaria = llavePrimaria[0];
            String valorLlavePrimaria = llavePrimaria[1];

            String comando = "UPDATE " + table + " SET " + strCambios +
                    " WHERE " + columnaLlavePrimaria + "=" + valorLlavePrimaria;

            conexion.ejecutarInstruccion(comando);
        } else {
            System.out.println("La llave primaria se debe especificar por medio de un array de dos elementos.");
        }

    }

    /**
     * Elimina una entrada.
     * @param llavePrimaria llave primaria de la entrada a eliminar. [columna, valor]
     */
    public void borrarEntrada(String[] llavePrimaria) {
        if (llavePrimaria.length == 2) {
            String columnaLlavePrimaria = llavePrimaria[0];
            String valorLlavePrimaria = llavePrimaria[1];

            String comando = "DELETE FROM " + table + " WHERE " + columnaLlavePrimaria + "=" + valorLlavePrimaria;

            conexion.ejecutarInstruccion(comando);
        } else {
            System.out.println("La llave primaria se debe especificar por medio de un array de dos elementos.");
        }
    }

    /**
     * Cierra las conexiones, statements, y resultsets abiertos, si existen.
     */
    public void cerrar() {
        conexion.cerrar();
    }
}
