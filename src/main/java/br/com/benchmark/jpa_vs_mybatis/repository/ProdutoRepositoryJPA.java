package br.com.benchmark.jpa_vs_mybatis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.benchmark.jpa_vs_mybatis.model.Produto;

public interface ProdutoRepositoryJPA extends JpaRepository<Produto, Long> {
}