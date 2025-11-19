package com.projeto.sistemabiblioteca.dashboard.schedulers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCliente;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEdicao;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEditora;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimExemplar;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimIdioma;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimTitulo;
import com.projeto.sistemabiblioteca.dashboard.services.DimAutorService;
import com.projeto.sistemabiblioteca.dashboard.services.DimCategoriaService;
import com.projeto.sistemabiblioteca.dashboard.services.DimClienteService;
import com.projeto.sistemabiblioteca.dashboard.services.DimEdicaoService;
import com.projeto.sistemabiblioteca.dashboard.services.DimEditoraService;
import com.projeto.sistemabiblioteca.dashboard.services.DimExemplarService;
import com.projeto.sistemabiblioteca.dashboard.services.DimIdiomaService;
import com.projeto.sistemabiblioteca.dashboard.services.DimTituloService;
import com.projeto.sistemabiblioteca.dashboard.services.FatoEmprestimoService;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.services.EmprestimoService;

import jakarta.transaction.Transactional;

@Component
public class DashboardUpdateScheduler {
		
		private EmprestimoService emprestimoService;
		
		private FatoEmprestimoService fatoEmprestimoService;
		
		private DimAutorService dimAutorService;
		
		private DimCategoriaService dimCategoriaService;
		
		private DimClienteService dimClienteService;
		
		private DimEdicaoService dimEdicaoService;
		
		private DimEditoraService dimEditoraService;
		
		private DimExemplarService dimExemplarService;
		
		private DimIdiomaService dimIdiomaService;
		
		private DimTituloService dimTituloService;
		
		
		public DashboardUpdateScheduler(EmprestimoService emprestimoService,
				FatoEmprestimoService fatoEmprestimoService, DimAutorService dimAutorService,
				DimCategoriaService dimCategoriaService, DimClienteService dimClienteService,
				DimEdicaoService dimEdicaoService, DimEditoraService dimEditoraService,
				DimExemplarService dimExemplarService, DimIdiomaService dimIdiomaService,
				DimTituloService dimTituloService) {
			this.emprestimoService = emprestimoService;
			this.fatoEmprestimoService = fatoEmprestimoService;
			this.dimAutorService = dimAutorService;
			this.dimCategoriaService = dimCategoriaService;
			this.dimClienteService = dimClienteService;
			this.dimEdicaoService = dimEdicaoService;
			this.dimEditoraService = dimEditoraService;
			this.dimExemplarService = dimExemplarService;
			this.dimIdiomaService = dimIdiomaService;
			this.dimTituloService = dimTituloService;
		}
		
		@Scheduled(fixedDelay = 5000)
		@Transactional
		public void atualizarDashBoard() {
			List<Emprestimo> emprestimosReais = emprestimoService.buscarTodosParaDashboard();
			
			for (Emprestimo empReal : emprestimosReais) {
				DimCliente dimCliente = dimClienteService.atualizar(empReal.getPessoa());
				DimExemplar dimExemplar = dimExemplarService.atualizar(empReal.getExemplar());
				DimEdicao dimEdicao = dimEdicaoService.atualizar(empReal.getExemplar().getEdicao());
				DimEditora dimEditora = dimEditoraService.atualizar(empReal.getExemplar().getEdicao().getEditora());
				DimIdioma dimIdioma = dimIdiomaService.atualizar(empReal.getExemplar().getEdicao().getIdioma());
				
				Titulo tituloReal = empReal.getExemplar().getEdicao().getTitulo();
				
				Set<DimAutor> dimAutores = new HashSet<>();
				Set<DimCategoria> dimCategorias = new HashSet<>();
				
				tituloReal.getAutores().forEach(autorReal -> dimAutores.add(dimAutorService.atualizar(autorReal)));
				tituloReal.getCategorias().forEach(categoriaReal -> dimCategorias.add(dimCategoriaService.atualizar(categoriaReal)));
				
				DimTitulo dimTitulo = dimTituloService.atualizar(tituloReal, dimCategorias, dimAutores);
				
				fatoEmprestimoService.atualizar(
						empReal,
						dimCliente.getSurrogateKey(),
						dimExemplar.getSurrogateKey(),
						dimEdicao.getSurrogateKey(),
						dimIdioma.getSurrogateKey(),
						dimEditora.getSurrogateKey(),
						dimTitulo.getSurrogateKey());
			}
		}
}
