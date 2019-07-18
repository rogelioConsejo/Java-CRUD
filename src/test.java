import CRUD.CRUD;

import java.util.ArrayList;
import java.util.HashMap;

public class test {


    public static void main(String[] args) {
        //Crea una conexión
        CRUD crud = new CRUD("com.mysql.jdbc.Driver",
                "localhost:3306", "CRUD",
                "CRUD",
                "E_Vk75bk%%y62aHe");

        //crea una nueva entrada
        HashMap<String, String> nuevaEntrada = new HashMap<>();
        nuevaEntrada.put("nombre", "Yater");
        nuevaEntrada.put("apellido", "Miné");
        crud.crearEntrada(nuevaEntrada);

        //lee todas las entradas
        ArrayList<HashMap<String,String>> resultado = crud.leerTodos();
        imprimirEnPantalla(resultado);

        System.out.println();

        //Prueba el UPDATE
        String[] primaryKey = {"id","14"};
        HashMap<String, String> cambios = new HashMap<>();
        cambios.put("apellido", "De Las Nieves");
        crud.actualizarEntrada(primaryKey,cambios);

        resultado = crud.leerTodos();
        imprimirEnPantalla(resultado);

        //Prueba el DELETE
        String[] elementoABorrar = {"id", "16"};
        crud.borrarEntrada(elementoABorrar);

        System.out.println();

        resultado = crud.leerTodos();
        imprimirEnPantalla(resultado);

        //Busca una entrada unsando sólo AND
        String dondeBuscar = "apellido";
        String queBuscar = "Consejo";
        HashMap<String, String> parametrosDeBusqueda = new HashMap<>();
        parametrosDeBusqueda.put(dondeBuscar, queBuscar);
        resultado = crud.buscarEntradas(parametrosDeBusqueda);
        System.out.printf("Resultado de la búsqueda por %s (\"%s\"):\n", dondeBuscar, queBuscar);

        imprimirEnPantalla(resultado);

        //Busca una entrada usando AND y OR
        String[][][] busqueda = {
                {
                        {"nombre", "Conejo"}, {"apellido", "Perez"}
                },
                {
                        {"apellido", "Conejo"}
                }
        };
        resultado = crud.buscarEntradas(busqueda);
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
