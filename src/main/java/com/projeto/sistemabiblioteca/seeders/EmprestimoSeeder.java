package com.projeto.sistemabiblioteca.seeders;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.repositories.EmprestimoRepository;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;

@Component
@Order(12)
@Profile("dev")
public class EmprestimoSeeder implements CommandLineRunner {

    private final EmprestimoRepository emprestimoRepository;
    private final PessoaRepository pessoaRepository;
    private final ExemplarRepository exemplarRepository;
    private final MultaRepository multaRepository;
    private final Random random = new Random();

    public EmprestimoSeeder(EmprestimoRepository emprestimoRepository,
                            PessoaRepository pessoaRepository,
                            ExemplarRepository exemplarRepository,
                            MultaRepository multaRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.pessoaRepository = pessoaRepository;
        this.exemplarRepository = exemplarRepository;
        this.multaRepository = multaRepository;
    }
    
    private Emprestimo criarEmprestimoEmAndamento(LocalDate hoje, Pessoa pessoa, Exemplar exemplar, Multa multa) {
    	 Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
    	 emprestimo.separarExemplar(hoje);    
    	 emprestimo.retirarExemplar(hoje.plusDays(random.nextInt(2)));
    	 return emprestimo;
    }
    
    private Emprestimo criarEmprestimoComStatusDevolvido(LocalDate hoje, Pessoa pessoa, Exemplar exemplar, Multa multa) {
   	 Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
   	 emprestimo.separarExemplar(hoje);    
   	 emprestimo.retirarExemplar(hoje.plusDays(random.nextInt(2)));
   	 emprestimo.devolverExemplar(emprestimo.getDtRetiradaExemplar().plusDays(random.nextInt(5)));
   	 return emprestimo;
   }
    
    private Emprestimo criarEmprestimoCancelado(LocalDate hoje, Pessoa pessoa, Exemplar exemplar, Multa multa) {
   	 Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
   	 emprestimo.separarExemplar(hoje);    
   	 emprestimo.cancelarReserva();
   	 return emprestimo;
   }
    
    private Emprestimo criarEmprestimoAtrasado(LocalDate hoje, Pessoa pessoa, Exemplar exemplar, Multa multa) {
      	 Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
      	 emprestimo.separarExemplar(hoje);    
      	 emprestimo.retirarExemplar(hoje.plusDays(random.nextInt(2)));
      	 emprestimo.registrarAtraso(emprestimo.getDtDevolucaoPrevista().plusDays(random.nextInt(10) + 1));
      	 return emprestimo;
      }
    
    private Emprestimo criarEmprestimoComStatusExemplarPerdido(LocalDate hoje, Pessoa pessoa, Exemplar exemplar, Multa multa) {
     	 Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
     	 emprestimo.separarExemplar(hoje);    
     	 emprestimo.retirarExemplar(hoje.plusDays(random.nextInt(2)));
     	 emprestimo.registrarPerdaDoExemplar();
     	 return emprestimo;
     }
    
    private Emprestimo criarEmprestimoDevolvidoComAtraso(LocalDate hoje, Pessoa pessoa, Exemplar exemplar, Multa multa) {
     	 Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
     	 emprestimo.separarExemplar(hoje);    
     	 emprestimo.retirarExemplar(hoje.plusDays(random.nextInt(2)));
     	 
     	 LocalDate dtAtraso = emprestimo.getDtDevolucaoPrevista().plusDays(random.nextInt(10) + 1);
     	 
     	 emprestimo.registrarAtraso(dtAtraso);
     	 emprestimo.devolverExemplar(dtAtraso);
     	 return emprestimo;
     }
    
    @Override
    public void run(String... args) throws Exception { // Spring Boot chama isso
        List<Pessoa> pessoas = pessoaRepository.findAllByFuncaoEquals(FuncaoUsuario.CLIENTE);
        List<Exemplar> exemplares = exemplarRepository.findAll();

        //LocalDate hoje = LocalDate.now();

        for (Pessoa pessoa : pessoas) {
            for (Exemplar exemplar : exemplares) {
                if (exemplar.getStatus() == StatusExemplar.DISPONIVEL) {

                    // Cria multa vazia
                    Multa multa = Multa.criarMultaVazia();
                    multaRepository.save(multa);
                    
                    // Marcar exemplar como alugado 
                    exemplar.alugar();
                    exemplarRepository.save(exemplar);
                    
                    int tipoEmprestimo = random.nextInt(6); // 0 a 5
                    
                    LocalDate hoje = LocalDate.now();
                    
                    Emprestimo emprestimo;
                    switch(tipoEmprestimo) {
                    case 0:
                        emprestimo = criarEmprestimoEmAndamento(hoje.minusDays(random.nextInt(4) + 3), pessoa, exemplar, multa);
                        break;
                    case 1:
                        emprestimo = criarEmprestimoComStatusDevolvido(hoje.minusDays(random.nextInt(5) + 10), pessoa, exemplar, multa);
                        break;
                    case 2:
                        emprestimo = criarEmprestimoCancelado(hoje.minusDays(random.nextInt(10)), pessoa, exemplar, multa);
                        break;
                    case 3:
                        emprestimo = criarEmprestimoAtrasado(hoje.minusDays(random.nextInt(5) + 30), pessoa, exemplar, multa);
                        break;
                    case 4:
                        emprestimo = criarEmprestimoComStatusExemplarPerdido(hoje.minusDays(random.nextInt(5) + 10), pessoa, exemplar, multa);
                        break;
                    case 5:
                    	emprestimo = criarEmprestimoDevolvidoComAtraso(hoje.minusDays(random.nextInt(5) + 30), pessoa, exemplar, multa);
                    	break;
                    default:
                    	emprestimo = criarEmprestimoEmAndamento(hoje.minusDays(random.nextInt(4) + 3), pessoa, exemplar, multa);
                    	break;
                }
                    // atualizando a multa
                    multaRepository.save(emprestimo.getMulta());
                    
                    // atualizando o exemplar
                    exemplarRepository.save(emprestimo.getExemplar());
                    
                    /*
                    // Cria empréstimo com data de início hoje
                    Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
                    
                    
                    // Separar exemplar
                    emprestimo.separarExemplar(hoje);

                    // Retirar exemplar (calcula automaticamente a data de devolução prevista)
                    emprestimo.retirarExemplar(hoje);
					*/
					
                    // Salvar empréstimo
                    emprestimoRepository.save(emprestimo);

                    break; // só um exemplar por pessoa
                }
            }
        }
    }
}
