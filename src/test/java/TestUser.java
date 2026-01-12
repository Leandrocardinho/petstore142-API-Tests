import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order de prioridade a cada test
public class TestUser {

    // 2.1 atributos
    static String ct = "application/json"; // content -type
    static String baseUri = "https://petstore.swagger.io/v2";

  
    // Função de leitura do json
   public static String lerArquivoJson(String arquivoJson) throws IOException{
        
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
       
    }
   
@Test @Order(1)
    public static String testLogin(){     //testLogin e extração de toten
        //Configura 
        String username = "MaxDog";
        String password = "123457588";
        
        String resultadoEsperado = "logged in user session:";

        Response response = (Response)  given()                    //Dado que
            .contentType(ct) //tipo do conteudo
            .log().all()                     //mostre tudo na ida  
        
        .when()                    //Quando 
            .get(baseUri + "/user/login?username=" + username + "&password=" + password)

        .then()
            .log().all()
            .statusCode(200)    // Validação do status code
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", containsString(resultadoEsperado)) // valida se constem a string esperada
            .body("message", hasLength(36)) // valida o tamanho da string retornada
        .extract();   // extrai a resposta completa

        //Extração
        String token = response.jsonPath().getString("message").substring(23); // extrai o token da mensagem
        System.out.println("Token extraido: " + token);
        return token;
        
    }


@Test   @Order(2)
    public void testPostUser() throws IOException{
        // carregar os dados do arquivo json do user
        String jsonBody = lerArquivoJson("src/test/resources/json/UserJson/userPost.json");
    
        //comeco do teste
        given()                    //Dado que   
            .contentType(ct) //tipo do conteudo
            .log().all()                     //mostre tudo na ida
            .body(jsonBody)                  //corpo da requisição

        //Executa
        .when()                    //Quando 
            .post(baseUri + "/user")
            
        //validações
        .then()                         //Então
            .log().all()                            //mostre tudo na volta
            .statusCode(200)    // Validação do status code
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is("3837299312"))
         
        ;    

    }

}
