import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order de prioridade a cada test
public class TestUser {

    // 2.1 atributos
    static String ct = "application/json"; // content -type
    static String baseUri = "https://petstore.swagger.io/v2";
    static String userName = "LelEco";
    int petId = 38372998;
    String firstName = "Leco";

  
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
            .body("message", is("38372998"))
         
        ;    

    }

    @Test @Order(3)
    public void testPut() throws IOException{
        //carregar dadoa do arquivo json

    String jsonBody = lerArquivoJson("src/test/resources/json/UserJson/userPut.json");

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .put(baseUri + "/user/" + userName)
        .then()
            .statusCode(200)
            .body("code", is (200))
            .body("type", is ("unknown"))
            .body("message", is ("38372998"))
        ;
    }

    @Test @Order(4)
    public void testGetUser() throws IOException{
        given()
            .contentType(ct)
            .log().all()
        .when()
            .get(baseUri + "/user/Odete")
        .then()
            .log().all()
            .statusCode(200)
            .body("id",is (petId))
            .body("username", is("Odete"))
            .body("firstName", is (firstName))
            .body("lastName", is ("Silva"))
            .body("email", is ("Leco@gmail.com"))
            .body("password", is ("123456"))
            .body("phone", is ("1975858757"))
            .body("userStatus", is (1))

        ;
    }

    @Test @Order(5)
    public void testDeleteUser(){
        given()
            .contentType(ct)
            .log().all()
        .when() 
            .delete(baseUri + "/user/Odete")
        .then()
            .statusCode(200)
            .body("code", is (200))
            .body("type", is ("unknown"))
            .body("message", is ("Odete"))
        ;
    }
    @ParameterizedTest @Order(6)
    @CsvFileSource(resources = "csv/massaUser.csv", numLinesToSkip = 1, delimiter = ',')
    public void testPostUserDDT(
        int id,
        String userName,
        String firstName,
        String lastName,
        String email,
        String password,
        String phone,
        int userStatus
    )//fim de parametros
    {// inicio do test 

        UserDDT user = new UserDDT(); //instanciando a class UserDDT

        user.id = id; //relacionando atributos
        user.userName = userName;
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.password = password;
        user.phone = phone;
        user.userStatus =userStatus;

        //Criar um Json para o Body ser enviado 
        Gson gson = new Gson(); 
        String jsonBody = gson.toJson(user);

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .post(baseUri + "/user")
        .then()
            .statusCode(200)
        ;      

    }
}
