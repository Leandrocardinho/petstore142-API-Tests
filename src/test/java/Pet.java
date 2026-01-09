public class Pet {
    //Definindo classe pet para guardar a estrutura de dados sobre animais pet

    public int id;
    public class Category{
        public int id;
        public String name;
    }
    public Category category; // Campo criado só para aparecer no TestPet o category
    public String name;
    public String[] photoUrls;
    public class Tag{
        public int id;
        public String name;
    }
    public Tag tags[];
    public String status;

    //Lembrando que esse Body acima vem lá no Post, feito da estrutura da API
    
}
