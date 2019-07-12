import CRUD.CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class test {


    public static void main(String[] args) {
        String nombre = "Conejo";
        String apellido = "Depascua";

        CRUD crud = new CRUD("com.mysql.jdbc.Driver",
                "localhost:3306", "CRUD",
                "CRUD",
                "E_Vk75bk%%y62aHe");

        if (crud.crearEntrada(nombre, apellido)) {
            System.out.printf("Se creó una nueva entrada para %s %s\n", nombre, apellido);
        } else {
            System.out.printf("Error al crear entrada para %s %s\n", nombre, apellido);
        }

        ResultSet resultado = crud.leerTodos();


        try {
            if (resultado != null) {
                while (resultado.next()) {
                    System.out.print(resultado.getString(2) + " ");
                    System.out.println(resultado.getString(3));
                }
                crud.cerrar();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
