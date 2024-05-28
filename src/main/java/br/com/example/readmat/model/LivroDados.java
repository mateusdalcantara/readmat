package br.com.example.readmat.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record LivroDados(@JsonAlias("id") int id,
                         @JsonAlias("title") String titulo,
                         @JsonAlias("authors") List<String> autor,
                         @JsonAlias("languages") List<String> idioma,
                         @JsonAlias("download_count") int contadorDeDownload) {
}
