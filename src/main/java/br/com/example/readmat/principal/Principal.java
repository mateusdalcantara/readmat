package br.com.example.readmat.principal;

import br.com.example.readmat.dto.AutorDTO;
import br.com.example.readmat.dto.LivroDTO;
import br.com.example.readmat.dto.ProcuraDTO;
import br.com.example.readmat.model.Autor;
import br.com.example.readmat.model.Livro;
import br.com.example.readmat.repository.LivrariaRepositorio;
import br.com.example.readmat.services.ConsumoApi;
import br.com.example.readmat.services.ConverteDados;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private final String ENDERECO = "https://gutendex.com/books/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converter = new ConverteDados();

    private LivrariaRepositorio livrariaRepositorio;

    public Principal(LivrariaRepositorio autorRepositorio) {
        this.livrariaRepositorio = autorRepositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """                                      
                    1- Buscar titulo de livro
                    2- Buscar livros registrados
                    3- Autores registrados
                    4- Listar autores vivos em um determinado ano
                    5- Listar livros em um determinado idioma
                                        
                    0- Sair        
                    """;

            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();
            switch (opcao) {
                case 1:
                    buscarTituloDeLivro();
                    break;
                case 2:
                    buscarLivrosRegistrados();
                    break;
                case 3:
                    buscarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Digite algum numero valido");
            }

        }
    }

    private void buscarTituloDeLivro() {
        System.out.println("Digite o nome do autor ou o titulo: ");
        var titulo = sc.nextLine();
        var json = consumoApi.obterDados(ENDERECO + "?search=" + titulo.replace(" ", "%20"));
        ProcuraDTO procuraDTO = new ConverteDados().obterDados(json, ProcuraDTO.class);

        if (procuraDTO.count() == 0) {
            System.out.println("Livro ou autor não encontrado");
            return;
        }

        List<LivroDTO> livros = procuraDTO.results();
        List<AutorDTO> dadosAutor = livros.stream().flatMap(livro -> livro.authors().stream()).collect(Collectors.toList());
        List<String> idiomas = livros.stream().flatMap(livro -> livro.languages().stream()).collect(Collectors.toList());

        IntStream.range(0, livros.size()).forEach(i -> {
            var livro = livros.get(i);
            System.out.printf("_-|Livro|-_ {%d}\nTitulo: %s\nIdioma: %s\nAutor(es):\n\nNome: %s\nData de nascimento: %s\nFalecido: %s\n",
                    i + 1, livro.title(), livro.languages().get(0), livro.authors().get(0).name(), livro.authors().get(0).birth_year(), livro.authors().get(0).death_year());
        });

        System.out.println("\nQual livro deseja adicionar ao seu banco de dados?\nDigite o número do livro");
        var escolha = sc.nextInt();
        sc.nextLine();
        Autor autor = new Autor(dadosAutor.get(escolha - 1));
        Livro livro = new Livro(livros.get(escolha - 1));

        Optional<Autor> encontrarAutor = livrariaRepositorio.findByTitulo(autor.getName());
        if (encontrarAutor.isPresent()) {
            var encontradoAutor = encontrarAutor.get();
            encontradoAutor.adicionarLivro(livro);
            System.out.println("\no Autor ja esta presente no banco de dados, iremos adicionar apenas o livro.");
            livrariaRepositorio.save(encontradoAutor);
        } else {
            autor.adicionarLivro(livro);
            System.out.println("\nautor e livro salvo com sucesso!");
            livrariaRepositorio.save(autor);
        }
    }

    private void buscarLivrosRegistrados() {
        List<Autor> autores = livrariaRepositorio.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getName))
                .forEach(a -> {
                    System.out.printf("\nAutor: %s\nTitulos:\n", a.getName());
                    a.getLivros().forEach(l -> {
                        System.out.printf("id: %d\nNome: %s\nIdioma: %s\n\n", l.getId(), l.getTitulo(), l.getIdioma());
                    });
                });
    }

    private void buscarAutoresRegistrados() {
        List<Autor> autor = livrariaRepositorio.findAll();
        System.out.println("Autores cadastrados: ");
        autor.stream().sorted(Comparator.comparing(Autor::getId)).forEach(a -> System.out.println("\nid: " +
                a.getId() + "\nAutor: " + a.getName()));
    }

    private void listarAutoresVivos() {

        System.out.println("digite até que ano os autores viveram deseja consultar:");
        int ano = sc.nextInt();
        List<Autor> autores = livrariaRepositorio.findByAuthorsAlive(ano);
        System.out.printf("\nAutores vivos até %d\n", ano);

        autores.stream()
                .sorted(Comparator.comparing(Autor::getBirthYear))
                .forEach(a -> System.out.printf("id: %d\nNome: %s\nNascido: %d\nFalecido: %d\n\n",
                        a.getId(),
                        a.getName(),
                        a.getBirthYear(),
                        a.getDeathYear()));

    }

    private void listarLivrosPorIdioma() {
        System.out.println("Digite a sigla do idioma que deseja consultar: \ninglês: en\nportuguês: pt");
        var idioma = "[" + sc.nextLine() + "]";
        List<Autor> autor = livrariaRepositorio.findAll();
        autor.stream().forEach(a -> a.getLivros().forEach(l -> {
            if (l.getIdioma().equals(idioma)) {
                System.out.printf("Autor: %s\n id: %d\n Titulo: %s\n Idioma: %s\n".formatted(a.getName(),
                        l.getId(), l.getTitulo(), l.getIdioma()));}
        }));
    }
}


