package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
	
	List<Exemplar> findAllByStatusEquals(StatusExemplar statusExemplar);
	
	List<Exemplar> findAllByEdicaoIdEdicao(Long id);
	
	@Query("""
			 SELECT e
			 FROM Exemplar e
			 WHERE e.edicao.idEdicao = :idEdicao
			 	AND e.status = :status
			 ORDER BY 
			   	CASE e.estadoFisico
			    	WHEN 'EXCELENTE' THEN 1
			     	WHEN 'OTIMO' THEN 2
			     	WHEN 'BOM' THEN 3
			     	WHEN 'RUIM' THEN 4
			     	WHEN 'MUITO_RUIM' THEN 5
			    END
			""")
	List<Exemplar> findAllByEdicaoOrderByEstadoFisicoCustom(@Param("idEdicao") Long idEdicao, @Param("status") StatusExemplar status);
}
