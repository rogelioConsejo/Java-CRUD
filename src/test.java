import CRUD.CRUD;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class test {


    public static void main(String[] args) {
        String nombre = "Conejo";
        String apellido = "Depascua";

        CRUD crud = new CRUD("com.mysql.jdbc.Driver",
                "localhost:3306", "CRUD",
                "CRUD",
                "E_Vk75bk%%y62aHe");

//        if (crud.crearEntrada(nombre, apellido)) {
//            System.out.printf("Se creó una nueva entrada para %s %s\n", nombre, apellido);
//        } else {
//            System.out.printf("Error al crear entrada para %s %s\n", nombre, apellido);
//        }

        ArrayList<HashMap<String,String>> resultado = crud.leerTodos();

        for (HashMap<String,String> entradas : resultado) {
            for (HashMap.Entry<String,String> entrada : entradas.entrySet() ) {
                String llave = entrada.getKey();
                String valor = entrada.getValue();
                System.out.printf("| %s: %s \t\t\t |", llave, valor);
            }
            System.out.println();
        }
    }
}
