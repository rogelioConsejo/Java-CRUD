import CRUD.CRUD;

import java.util.ArrayList;
import java.util.HashMap;

public class test {


    public static void main(String[] args) {
        HashMap<String, String> nuevaEntrada = new HashMap<>();
        nuevaEntrada.put("nombre", "Ando");
        nuevaEntrada.put("apellido", "Chambeando");

        //Crea una conexión
        CRUD crud = new CRUD("com.mysql.jdbc.Driver",
                "localhost:3306", "CRUD",
                "CRUD",
                "E_Vk75bk%%y62aHe");

        //Crear una entrada y lee todas las entradas.
        crud.crearEntrada(nuevaEntrada);
        ArrayList<HashMap<String,String>> resultado = crud.leerTodos();

        imprimirEnPantalla(resultado);
        crud.cerrar();
    }

    private static void imprimirEnPantalla(ArrayList<HashMap<String, String>> resultado) {
        //Muestra las entradas leídas en pantalla.
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
