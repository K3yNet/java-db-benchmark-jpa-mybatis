package br.com.benchmark.jpa_vs_mybatis.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import br.com.benchmark.jpa_vs_mybatis.model.Produto;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProdutoMapper {

    @Insert("INSERT INTO produto (nome, preco, quantidade) VALUES(#{nome}, #{preco}, #{quantidade})")
    void save(Produto produto);

    @Select("SELECT * FROM produto WHERE id = #{id}")
    Optional<Produto> findById(Long id);

    @Select("SELECT * FROM produto")
    List<Produto> findAll();

    @Delete("DELETE FROM produto")
    void deleteAll();

    void saveAll(List<Produto> produtos);
}