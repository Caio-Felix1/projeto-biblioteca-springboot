package com.projeto.sistemabiblioteca.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.sistemabiblioteca.entities.interfaces.ArmazenamentoService;

@Service
public class ArmazenamentoLocalService implements ArmazenamentoService {
	
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
}