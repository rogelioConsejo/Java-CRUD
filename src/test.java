import CRUD.CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class test {


    public static void main(String[] args) {
        CRUD crud = new CRUD("com.mysql.jdbc.Driver",
                "localhost:3306", "CRUD",
                "CRUD",
                "E_Vk75bk%%y62aHe");

        crud.crearEntrada("Manuel", "Conejo");
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
