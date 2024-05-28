package br.com.example.readmat.model;

import br.com.example.readmat.dto.LivroDTO;
import jakarta.persistence.*;

@Entity
@Table(name= "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idioma;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Livro(LivroDTO livroDTO) {
        this.autor = autor;
        this.titulo = livroDTO.title();
        this.idioma = String.valueOf(livroDTO.languages());
    }

    public Livro() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

}
