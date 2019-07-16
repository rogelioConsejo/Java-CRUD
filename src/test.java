import CRUD.CRUD;

import java.util.ArrayList;
import java.util.HashMap;

public class test {


    public static void main(String[] args) {
        HashMap<String, String> nuevaEntrada = new HashMap<>();
        nuevaEntrada.put("nombre", "Elmeper");
        nuevaEntrada.put("apellido", "Donas");

        CRUD crud = new CRUD("com.mysql.jdbc.Driver",
                "localhost:3306", "CRUD",
                "CRUD",
                "E_Vk75bk%%y62aHe");


        crud.crearEntrada(nuevaEntrada);

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
