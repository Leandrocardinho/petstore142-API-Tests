import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUser {

    // Função de leitura do json
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));

    
    }
}