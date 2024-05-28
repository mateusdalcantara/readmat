package br.com.example.readmat.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record IdiomaDados(@JsonAlias("languages") List<String> languages) {
}
