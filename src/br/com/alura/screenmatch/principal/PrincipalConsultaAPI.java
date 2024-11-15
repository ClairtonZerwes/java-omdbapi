package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.exceptions.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalConsultaAPI {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String consulta = "";
        List<Titulo> titulos = new ArrayList<>();
        final String codigoChaveAPI = "2d011eff";
        Gson gson = new Gson();

        do {
            System.out.println("Informe o nome do filme para consultar (digite \"sair\" para encerrar): ");
            var linkTituloParaConsultar = leitura.nextLine();
            consulta = linkTituloParaConsultar;
            if (consulta.equalsIgnoreCase("sair")) {
                break;
            }

            // String endereco = "https://www.omdbapi.com/?t=" + busca.replace(" ", "+") + "&apikey=coloque_sua_apikey";
            // na descrição de consulta substituir espaços (" " = %20) em branco por codificação URL = URL ENCODER
            linkTituloParaConsultar = URLEncoder.encode(linkTituloParaConsultar, StandardCharsets.UTF_8);
            linkTituloParaConsultar = "https://www.omdbapi.com/?t=" + linkTituloParaConsultar + "&apikey=" + codigoChaveAPI;
            System.out.println("Link API consulta: " + linkTituloParaConsultar);
            // exemplo filme e.t. = "https://www.omdbapi.com/?t=e.t.&apikey=2d011eff"

            try {
                HttpClient client = HttpClient.newHttpClient();
                // HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.omdbapi.com/?t=matrix&apikey=2d011eff")).build();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(linkTituloParaConsultar)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
                String json = response.body();

                // Titulo meuTitulo = gson.fromJson(json, Titulo.class);
                gson = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
                TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
                System.out.println("Meu titulo formato dados api omdb = " + meuTituloOmdb);

                // Tem que tratar alguns valores pq na minha classe titulo os tipos de dados podem mudar e no objeto
                // json da omdb todos são strings por exemplo Runtime="40 min", vem com letra para identificar o tipo
                // de tempo/periodo ou o Year = "2018-" com algum valor que não é somente números.
                Titulo meuTitulo = new Titulo(meuTituloOmdb);
                System.out.println("Titulo com dados convertido");
                System.out.println(meuTitulo);

                FileWriter escrever;
                escrever = new FileWriter("filmes.txt"); // Aqui é salvo na raiz do projeto
                escrever.write(meuTitulo.toString());
                escrever.close();
                titulos.add(meuTitulo); /* adiciona titulos pesquisados depois pode ser criado um JSON com todas consultas */
                /* OBS -> A classe File representa um arquivo ou diretório no sistema de arquivos do computador,
                    permitindo que você crie, delete, liste e manipule arquivos e diretórios. Para criar
                    um objeto File, você precisa passar o caminho do arquivo ou diretório como argumento
                    para o construtor. Por exemplo:
                    File file = new File("C:\\meuArquivo.txt");
                */
            } catch (NumberFormatException e) {
                System.out.println("Ocorreu erro ao converter os dados");
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Ocorreu um ou mais erros na consulta API odmb, Verifique o endereço informado!");
            } catch (ErroDeConversaoDeAnoException e) {
                System.out.println(e.getMessage());
            }
        } while(!consulta.equalsIgnoreCase("sair"));

        /* Criando Arquivo comformato JSON */
        FileWriter escreverJson = new FileWriter("titulos.json");
        escreverJson.write(gson.toJson(titulos));
        escreverJson.close();

        /* Imprimindo a lista de titulos */
        System.out.println("\nLista de Titulos Consultados \n" + titulos);
        System.out.println("O programa finalizou corretamente!");
    }
}
