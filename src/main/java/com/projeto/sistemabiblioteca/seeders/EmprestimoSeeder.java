package com.projeto.sistemabiblioteca.seeders;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.repositories.EmprestimoRepository;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class EmprestimoSeeder implements CommandLineRunner {

    private final EmprestimoRepository emprestimoRepository;
    private final PessoaRepository pessoaRepository;
    private final ExemplarRepository exemplarRepository;
    private final MultaRepository multaRepository;

    public EmprestimoSeeder(EmprestimoRepository emprestimoRepository,
                            PessoaRepository pessoaRepository,
                            ExemplarRepository exemplarRepository,
                            MultaRepository multaRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.pessoaRepository = pessoaRepository;
        this.exemplarRepository = exemplarRepository;
        this.multaRepository = multaRepository;
    }

    @Override
    public void run(String... args) throws Exception { // Spring Boot chama isso
        List<Pessoa> pessoas = pessoaRepository.findAll();
        List<Exemplar> exemplares = exemplarRepository.findAll();

        LocalDate hoje = LocalDate.now();

        for (Pessoa pessoa : pessoas) {
            for (Exemplar exemplar : exemplares) {
                if (exemplar.getStatus() == StatusExemplar.DISPONIVEL) {

                    // Cria multa vazia
                    Multa multa = Multa.criarMultaVazia();
                    multaRepository.save(multa);

                    // Cria empréstimo com data de início hoje
                    Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);

                    // Separar exemplar
                    emprestimo.separarExemplar(hoje);

                    // Marcar exemplar como alugado antes de retirar
                    exemplar.alugar();
                    exemplarRepository.save(exemplar);

                    // Retirar exemplar (calcula automaticamente a data de devolução prevista)
                    emprestimo.retirarExemplar(hoje);

                    // Salvar empréstimo
                    emprestimoRepository.save(emprestimo);

                    break; // só um exemplar por pessoa
                }
            }
        }
    }
}
