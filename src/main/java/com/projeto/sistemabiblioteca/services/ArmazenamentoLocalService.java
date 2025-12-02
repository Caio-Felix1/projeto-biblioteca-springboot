package com.projeto.sistemabiblioteca.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.sistemabiblioteca.entities.interfaces.ArmazenamentoService;

@Service
public class ArmazenamentoLocalService implements ArmazenamentoService {
	
    private final Random random = new Random();
	private final Path root = Paths.get("imagens");

	@Override
	public String salvar(MultipartFile file) {
		try {
			if (!Files.exists(root)) {
				Files.createDirectories(root);
			}
			
			String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
			Path destino = root.resolve(nomeArquivo);
			Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
			
			return "http://localhost:8080/imagens/" + nomeArquivo;
		}
		catch (IOException e) {
			throw new RuntimeException("Erro: não foi possível salvar o arquivo", e);
		}
	}
	
    public String pegarCapaAleatoria() {
        try {
            if (!Files.exists(root)) {
                return null;
            }

            List<Path> arquivos = Files.list(root)
                    .filter(Files::isRegularFile)
                    .toList();

            if (arquivos.isEmpty()) {
                return null;
            }

            Path escolhido = arquivos.get(random.nextInt(arquivos.size()));
            return "http://localhost:8080/imagens/" + escolhido.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar capas aleatórias", e);
        }
    }
}