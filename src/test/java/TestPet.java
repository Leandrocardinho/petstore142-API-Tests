// 0 - Pacote


// 1- bibliotecas

// 2-   classe

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale.Category;

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

    @Test @Order(1)
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

   @Test @Order(2)
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

        @Test @Order(3)
    public void testGetPet(){
        //Configura movido para o inicio da classe

         //começo do teste
       
       given()                          //Dado que
            .contentType(ct)              // tipo do conteudo
            .log().all()                  // mostre tudo na ida
            .header("","api_key: " + TestUser.testLogin()) // autenticação ele pega o testUser e tras o token que é extraido 
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
        ;            
    }

    @Test @Order(4)
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
            .body("message", is(String.valueOf(petId)))
        ;    
    }

    //Data Driver Testing - DDT - Teste com Massa
    // Teste com Json parametrizado
    
    @ParameterizedTest @Order(5)
    @CsvFileSource(resources = "/csv/massaPet.csv", numLinesToSkip = 1, delimiter = ',')
    public <User> void testPostDDT(
        int petId,
        String petName,
        int catId,
        String catName,
        String tagId,
        String status1
    
    ) //fim dos parametros
    { // inicio do código do metodo testPetDDT

        // Criar a classe pet para receber o dado
        Pet pet = new Pet();
        Pet.Category category = pet.new Category(); //instanciando a sub class Category
        Pet.Tag[] tags = new Pet.Tag[2]; //intanciando a sub class Tag, criando um array para dois elementos 
        tags[0] = pet.new Tag();
        tags[1] = pet.new Tag();

        pet.id = petId;       /// construtor está relacionado aos atributos da classe User ao dados do csv
        pet.category = category; // Associar o pet.category com a subclasse category
        pet.category.id = catId;
        pet.category.name = catName;
        pet.name = petName;
        //pet.photoUrls;  Não precisa ser incluido porque esta vazio
        pet.tags = tags; // associar a pet.tags com a subclasse tags
        pet.tags[0].id = 9;
        pet.tags[0].name = "vacinado";
        pet.status = status1; // status inicial usado no Post "available"
        

        //Criar um json para o Body ser enviado a partir da classe Pet e do Csv
        Gson gson = new Gson(); //intancia a clase Gson como Objeto gson
        String jsonBody = gson.toJson(pet);

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .post(uriPet)
        .then()
            .log().all()
            .statusCode(200)
            .body("id",is (petId))
            .body("name", is (petName))
            .body("category.id", is(catId))
            .body("category.name", is(catName))
            .body("status", is(status1)) //Inicial do Post
        ;

    }
}




