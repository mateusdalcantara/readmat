package br.com.example.readmat;

import br.com.example.readmat.principal.Principal;
import br.com.example.readmat.repository.LivrariaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReadmatApplication implements CommandLineRunner {

	@Autowired
	private LivrariaRepositorio livrariaRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(ReadmatApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livrariaRepositorio);
		principal.exibeMenu();
	}
}
