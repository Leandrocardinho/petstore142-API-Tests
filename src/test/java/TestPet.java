// 0 - Pacote


// 1- bibliotecas

// 2-   classe

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class TestPet {
    // 2.1 atributos

    static int petId = 38372993;
    static String ct = "application/json"; // content -type
    static String uriPet = "https://petstore.swagger.io/v2/pet";

    String petName = "Max";   
    String categoryName = "cachorro";
    String tagName = "vacinado";
    String tagId = "9";
    String[] status = {"available","sold"};

 
    // 2.2 funçoes e métodos
    // 2.2.1 funções e métodos comuns / uteis

    // 2.2.2 métodos de teste
   
    //  Função de leitura de Json
    public static String lerArquivoJson(String arquivoJson) throws IOException{
        
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
       
    }

    @Test
    public void testPostPet () throws IOException{
        // carregar os dados do arquivo json do pet
         String jsonBody = lerArquivoJson("src/test/resources/json/PetJson/pet1.json");
                

         //começo do teste
         given()                          //Dado que
            .contentType(ct)              // tipo do conteudo
            .log().all()                  // mostre tudo na ida
            .body(jsonBody)               // corpo da requisição

        //execução
        .when()                           // Quando
       
        .post(uriPet)                 // endpoint da API fazendo o post
        //validações
        .then()                                       // Então
            .log().all()                            // mostre tudo na volta
            .statusCode(200)      // Validação do status code 
            .body("name", is (petName))    // Verificação do nome do pet
            .body("id", is(petId))              // validação do id do pet
            .body("category.name", is(categoryName)) // validação da categoria do pet    
            .body("tags[0].id", is(Integer.parseInt(tagId)))        // validação id da tag    
            .body("tags[0].name", is(tagName)); // validação nome da tag                   
        
    }   

    @Test
    public void testGetPet(){
        //Configura movido para o inicio da classe


         //começo do teste
       
       given()                          //Dado que
            .contentType(ct)              // tipo do conteudo
            .log().all()                  // mostre tudo na ida
         
        //execução
        .when()                           // Quando
       
        .get(uriPet + "/" + petId)                 // endpoint da API fazendo o get
        //validações
        .then()                                       // Então
            .log().all()                            // mostre tudo na volta
            .statusCode(200)      // Validação do status code 
            .body("name", is (petName))    // Verificação do nome do pet
            .body("id", is(petId))              // validação do id do pet
            .body("category.name", is(categoryName)) // validação da categoria do pet    
            .body("tags[0].id", is(Integer.parseInt(tagId)))      // validação id da tag    
            .body("tags[0].name", is(tagName)) // validação nome da tag  
            .body("status", is(status[0]));               // validação status do pet
         
    }

   @Test
    public void testPutPet() throws IOException{
        // carregar os dados do arquivo json do pet
         String jsonBody = lerArquivoJson("src/test/resources/json/PetJson/petPut1.json");
        
         given()
            .contentType(ct)             
            .log().all() 
            .body (jsonBody)                 

        .when()                          
            .put (uriPet)                 
        .then()
            .log().all()                           
            .statusCode(200)      
            .body("name", is (petName))    
            .body("id", is(petId))              
            .body("category.name", is(categoryName))    
            .body("tags[0].id", is(Integer.parseInt(tagId)))         
            .body("tags[0].name", is(tagName))  
            .body("status", is(status[1]));
        }
    
    public void testDeletePet(){
        //Configura movido para o inicio da classe
        
        //começo do teste
         given()                          
            .contentType(ct)              
            .log().all()                  

        .when()                           
            .delete (uriPet + "/" + petId)                 
        .then()                                       
            .log().all()                            
            .statusCode(200)      
            .body("code", is(200))    
            .body("message", is(String.valueOf(petId)));    
    }
    
}
