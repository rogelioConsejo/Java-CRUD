package CRUD;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

class Herramientas {
    static ArrayList<HashMap<String, String>> resultSet2ArrayDeHashMaps(@NotNull ResultSet resultSet) {
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

    static void entrada2String(HashMap<String, String> entrada, StringBuilder columnas) {
        for (String columna : entrada.keySet()) {
            columnas.append(columna).append(", ");
        }
        //Borramos el espacio y la coma extra al final de "columnas"
        columnas.setLength(columnas.length() - 2);
    }

    static void valores2String(HashMap<String, String> entrada, StringBuilder valores) {
        for (Object valor : entrada.values()) {
            valores.append('"').append(valor).append('"').append(", ");
        }
        //Borramos el espacio y la coma extra al final de "valores"
        valores.setLength(valores.length() - 2);
    }
}
