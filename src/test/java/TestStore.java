// 0 - Pacote


// 1- bibliotecas

// 2-   classe
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order de prioridade a cada test
public class TestStore {
    static String ct  = "application/json"; 
    static String uriStore = "https://petstore.swagger.io/v2/store/";
    int orderId = 1199742131;

     // Função de leitura de Json

    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    @Test @Order(1)
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

    @Test @Order(2)
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
    @Test @Order(3)
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

    @ParameterizedTest @Order(4)
    @CsvFileSource(resources = "csv/massaStore.csv", numLinesToSkip = 1, delimiter =',')
    public void testPostDDT(
         int id,
         int petId,
         int quantity,
         String shipDate,
         String status,
         boolean complete

    ) //fim de parametros 
    { // inicio do codigo testPostDDT

        StoreDDT store = new StoreDDT(); //instanciando a classe Store

        store.id = id; // relacionanodo atributos da clase ao store
        store.petId = petId;
        store.quantity = quantity;
        store.shipDate = shipDate;
        store.status = status;
        store.complete = complete;

        //Criar um json para o Body ser enviado 
        Gson gson = new Gson(); 
        String jsonBody = gson.toJson(store);

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .post(uriStore + "order")
        .then() 
            .log().all()
            .statusCode(200)
            .body("id", is(id))
            .body("petId", is (petId))
            .body("quantity", is (quantity))
            .body("shipDate", containsString(shipDate))
            .body("status", is (status))
            .body("complete", is (complete))
        ;

    }
}