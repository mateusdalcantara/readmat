package br.com.example.readmat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProcuraDTO(int count,
                         String next,
                         String previous,
                         List<LivroDTO> results) {
}