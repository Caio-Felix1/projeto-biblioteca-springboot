package com.projeto.sistemabiblioteca.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.EnderecoDTO;
import com.projeto.sistemabiblioteca.DTOs.PessoaDTO;
import com.projeto.sistemabiblioteca.DTOs.RegistroDTO;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.exceptions.AcessoNegadoException;
import com.projeto.sistemabiblioteca.exceptions.CpfJaCadastradoException;
import com.projeto.sistemabiblioteca.exceptions.EmailJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PessoaService {
	
	private PessoaRepository pessoaRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private EstadoService estadoService;
	
	private EnderecoService enderecoService;
	
	public PessoaService(PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder, EstadoService estadoService, EnderecoService enderecoService) {
		this.pessoaRepository = pessoaRepository;
		this.passwordEncoder = passwordEncoder;
		this.estadoService = estadoService;
		this.enderecoService = enderecoService;
	}
	
	public List<Pessoa> buscarTodos() {
		return pessoaRepository.findAll();
	}
	
	public List<Pessoa> buscarTodosComFuncaoIgualA(FuncaoUsuario funcao) {
		return pessoaRepository.findAllByFuncaoEquals(funcao);
	}
	
	public List<Pessoa> buscarTodosComStatusContaIgualA(StatusConta statusConta) {
		return pessoaRepository.findAllByStatusContaEquals(statusConta);
	}
	
	public Pessoa buscarPorId(Long id) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(id);
		if (pessoa.isEmpty()) {
			throw new EntityNotFoundException("Erro: usuário com id correspondente não foi encontrado.");
		}
		return pessoa.get();
	}
	
	public void verificarEmailDisponivel(String email) {
		if (pessoaRepository.existsByEmailEndereco(email)) {
			throw new EmailJaCadastradoException("Erro: email já foi cadastrado.");
		}
	}
	
	public void verificarCpfDisponivel(String cpf) {
		if (pessoaRepository.existsByCpfValor(cpf)) {
			throw new CpfJaCadastradoException("Erro: cpf já foi cadastrado.");
		}
	}
	
	public Pessoa buscarPorEmail(String email) {
		Pessoa pessoa = pessoaRepository.findByEmailEndereco(email);
		if (pessoa == null) {
			throw new EntityNotFoundException("Erro: usuário com email correspondente não foi encontrado.");
		}
		return pessoa;
	}
	
	public Pessoa buscarPorCpf(String cpf) {
		Pessoa pessoa = pessoaRepository.findByCpfValor(cpf);
		if (pessoa == null) {
			throw new EntityNotFoundException("Erro: usuário com cpf correspondente não foi encontrado.");
		}
		return pessoa;
	}
	
	private Endereco instanciarEndereco(EnderecoDTO enderecoDTO) {
		Estado estado = estadoService.buscarPorId(enderecoDTO.idEstado());
		
		if (estado.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalArgumentException("Erro: não é possível associar um novo endereço a um estado com status inativo.");
		}
		
		return new Endereco(
				enderecoDTO.nomeLogradouro(),
				enderecoDTO.numero(),
				enderecoDTO.complemento(),
				enderecoDTO.bairro(),
				enderecoDTO.cep(),
				enderecoDTO.cidade(),
				estado);
	}
	
	private Email validarEmail(String email) {
		Email emailFormatoValido = new Email(email);
		verificarEmailDisponivel(emailFormatoValido.getEndereco());
		
		return emailFormatoValido;
	}
	
	private Cpf validarCpf(String cpf) {
		Cpf cpfFormatoValido = new Cpf(cpf);
		verificarCpfDisponivel(cpfFormatoValido.getValor());
		
		return cpfFormatoValido;
	}
	
	@Transactional
	public void cadastrarUsuario(RegistroDTO registroDTO) {
		Email email = validarEmail(registroDTO.email());
		
		Cpf cpf = validarCpf(registroDTO.cpf());
		
		Telefone telefone = new Telefone(registroDTO.telefone());
		
		String encryptedPassword = passwordEncoder.encode(registroDTO.senha());
		
		Endereco endereco = instanciarEndereco(registroDTO.endereco());
		
		enderecoService.inserir(endereco);
		
		Pessoa pessoa = new Pessoa(
				registroDTO.nome(),
				cpf,
				registroDTO.sexo(),
				FuncaoUsuario.CLIENTE,
				registroDTO.dtNascimento(),
				LocalDate.now(),
				telefone,
				email,
				encryptedPassword,
				StatusConta.EM_ANALISE_APROVACAO,
				endereco);
				
		inserir(pessoa);
	}
	
	@Transactional
	public void cadastrarUsuarioPorAdmin(PessoaDTO pessoaDTO) {
		Email email = validarEmail(pessoaDTO.email());
		
		Cpf cpf = validarCpf(pessoaDTO.cpf());
		
		Telefone telefone = new Telefone(pessoaDTO.telefone());
		
		String encryptedPassword = passwordEncoder.encode(pessoaDTO.senha());
		
		Endereco endereco = instanciarEndereco(pessoaDTO.endereco());
		
		enderecoService.inserir(endereco);
		
		Pessoa pessoa = new Pessoa(
				pessoaDTO.nome(),
				cpf,
				pessoaDTO.sexo(),
				pessoaDTO.funcao(),
				pessoaDTO.dtNascimento(),
				LocalDate.now(),
				telefone,
				email,
				encryptedPassword,
				StatusConta.ATIVA,
				endereco);
				
		inserir(pessoa);
	}
	
	public void inserir(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
	}
	
	@Transactional
	public void inativar(Long id) {
		Pessoa pessoa = buscarPorId(id);
		if (pessoa.getStatusConta() == StatusConta.INATIVA) {
			throw new IllegalStateException("Erro: usuário já está inativo.");
		}
		enderecoService.inativar(pessoa.getEndereco().getIdEndereco());
		pessoa.inativarConta();
		pessoaRepository.save(pessoa);
	}
	
	private boolean podeAtualizar(Pessoa usuarioAlvo, Pessoa usuarioLogado) {
		if (usuarioLogado.getFuncao() == FuncaoUsuario.ADMINISTRADOR) {
			return true;
		}
		if (usuarioLogado.getFuncao() == FuncaoUsuario.BIBLIOTECARIO) {
			return usuarioLogado.getIdPessoa().equals(usuarioAlvo.getIdPessoa()) || usuarioAlvo.getFuncao() == FuncaoUsuario.CLIENTE;
		}
		return usuarioLogado.getIdPessoa().equals(usuarioAlvo.getIdPessoa());
	}
	
	@Transactional
	public Pessoa atualizar(Long id, PessoaDTO pessoaDTO, Pessoa usuarioLogado) {
		Pessoa pessoa1 = buscarPorId(id);
		
		if (pessoa1.getStatusConta() != StatusConta.ATIVA) {
			throw new IllegalStateException("Erro: não é possível atualizar usuário que não está ativo.");
		}
		if (!podeAtualizar(pessoa1, usuarioLogado)) {
			throw new AcessoNegadoException("Erro: não tem permissão para atualizar este usuário.");
		} 
		if (pessoa1.getFuncao() == FuncaoUsuario.CLIENTE && pessoa1.getFuncao() != pessoaDTO.funcao()) {
			throw new IllegalStateException("Erro: não é possível alterar o nível de acesso do cliente.");
		}
		if (pessoa1.getFuncao() == FuncaoUsuario.BIBLIOTECARIO && pessoaDTO.funcao() == FuncaoUsuario.CLIENTE) {
			throw new IllegalStateException("Erro: não é possível alterar o nível de acesso do funcionário para cliente.");
		}
		if (pessoa1.getFuncao() == FuncaoUsuario.ADMINISTRADOR && pessoaDTO.funcao() == FuncaoUsuario.CLIENTE) {
			throw new IllegalStateException("Erro: não é possível alterar o nível de acesso do administrador para cliente.");
		}
		
		Email email = (pessoa1.getEmail().getEndereco().equals(pessoaDTO.email())) ? pessoa1.getEmail() : validarEmail(pessoaDTO.email());
		
		Cpf cpf = (pessoa1.getCpf().getValor().equals(pessoaDTO.cpf())) ? pessoa1.getCpf() : validarCpf(pessoaDTO.cpf());
		
		Telefone telefone = new Telefone(pessoaDTO.telefone());
		
		String encryptedPassword = passwordEncoder.encode(pessoaDTO.senha());
		
		Pessoa pessoa2 = new Pessoa(
				pessoaDTO.nome(),
				cpf,
				pessoaDTO.sexo(),
				pessoaDTO.funcao(),
				pessoaDTO.dtNascimento(),
				LocalDate.now(),
				telefone,
				email,
				encryptedPassword,
				StatusConta.ATIVA,
				null);
		
		enderecoService.atualizar(pessoa1.getEndereco().getIdEndereco(), pessoaDTO.endereco());
		atualizarDados(pessoa1, pessoa2);
		return pessoaRepository.save(pessoa1);
	}
	
	private void atualizarDados(Pessoa pessoa1, Pessoa pessoa2) {
		pessoa1.setNome(pessoa2.getNome());
		pessoa1.setCpf(pessoa2.getCpf());
		pessoa1.setSexo(pessoa2.getSexo());
		pessoa1.setFuncao(pessoa2.getFuncao());
		pessoa1.definirDataNascimento(pessoa2.getDtNascimento(), LocalDate.now());
		pessoa1.setTelefone(pessoa2.getTelefone());
		pessoa1.setEmail(pessoa2.getEmail());
		pessoa1.setSenhaHash(pessoa2.getSenhaHash());
	}
	
	public void aprovarConta(Long id) {
		Pessoa pessoa = buscarPorId(id);
		if (pessoa.getFuncao() != FuncaoUsuario.CLIENTE) {
			throw new IllegalArgumentException("Erro: apenas clientes podem ser aprovados.");
		}
		pessoa.aprovarConta();
		pessoaRepository.save(pessoa);
	}
	
	@Transactional
	public void rejeitarConta(Long id) {
		Pessoa pessoa = buscarPorId(id);
		if (pessoa.getFuncao() != FuncaoUsuario.CLIENTE) {
			throw new IllegalArgumentException("Erro: apenas clientes podem ser rejeitados.");
		}
		enderecoService.inativar(pessoa.getEndereco().getIdEndereco());
		pessoa.rejeitarConta();
		pessoaRepository.save(pessoa);
	}
	
	public void solicitarExclusaoConta(Long id) {
		Pessoa pessoa = buscarPorId(id);
		if (pessoa.getFuncao() != FuncaoUsuario.CLIENTE) {
			throw new IllegalArgumentException("Erro: a solicitação de exclusão de conta só pode ser realizada em contas de clientes.");
		}
		pessoa.solicitarExclusaoConta();
		pessoaRepository.save(pessoa);
	}
	
	public void rejeitarSolicitacaoExclusao(Long id) {
		Pessoa pessoa = buscarPorId(id);
		if (pessoa.getFuncao() != FuncaoUsuario.CLIENTE) {
			throw new IllegalArgumentException("Erro: a rejeição de exclusão de conta só pode ser realizada em contas de clientes.");
		}
		pessoa.rejeitarSolicitacaoExclusao();
		pessoaRepository.save(pessoa);
	}
}
