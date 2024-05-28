package br.com.example.readmat.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDados(@JsonAlias("birth_year") int anoDeNascimento,
                         @JsonAlias("death_year") int anoDaMorte,
                         @JsonAlias("name") String name){
}
