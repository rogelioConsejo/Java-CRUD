public class test {


    public static void main(String[] args) {
        CRUD crud = new CRUD("com.mysql.jdbc.Driver",
                "localhost:3306", "CRUD",
                "CRUD",
                "E_Vk75bk%%y62aHe");

        crud.leerTodos();
    }
}
