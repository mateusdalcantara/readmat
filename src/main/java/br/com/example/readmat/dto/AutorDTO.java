package br.com.example.readmat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDTO(int birth_year,
                       int death_year,
                       String name) {
}
