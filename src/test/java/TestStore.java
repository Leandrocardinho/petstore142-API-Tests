// 0 - Pacote


// 1- bibliotecas

// 2-   classe
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class TestStore {
    static String ct  = "application/json"; 
    static String uriStore = "https://petstore.swagger.io/v2/store/";
    int orderId = 1199742131;

     // Função de leitura de Json

    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    @Test
    public void testPostOrder() throws IOException {
        String jsonBody = lerArquivoJson("src/test/resources/json/StoreJson/StorePost1.json");

        given()
            .contentType(ct)       // tipo correto
            .log().all()
            .body(jsonBody)

        .when()
            .post(uriStore + "order")        // endpoint correto

        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1199742131)) // valida campo da resposta
            .body("petId", is(999))
            .body("quantity", is(1))
            .body("status", is("placed")); // valida campo da resposta

    }

    @Test
    public void testGetOrderById() throws IOException{
        String jsonBody = lerArquivoJson("src/test/resources/json/StoreJson/StoreGet.json");
        
        given()                     //Dado
            .contentType(ct)       // tipo correto
            .log().all()    
            .body(jsonBody)

        .when()           //Quando
            .get(uriStore + "order/" + orderId)        // endpoint correto

        .then()        //Então
            .log().all()
            .statusCode(200)
            .body("id", is(orderId)) // valida campo da resposta
            .body("petId", is(999))
            .body("quantity", is(1))
            .body("status", is("placed"))
            .body("complete", is(true));

        }
    @Test
    public void testDeleteOrderById(){
        given()                     //Dado
            .contentType(ct)      // tipo correto
            .log().all()
        .when()           //Quando
            .delete(uriStore + "order/" + orderId)        // endpoint correto  
        .then()        //Então
            .log().all()
            .statusCode(200) 
            .body("code", is(200)) // valida campo da resposta
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(orderId))); // valida campo da resposta
                                                                                                 
    }
}