package br.com.benchmark.jpa_vs_mybatis;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import br.com.benchmark.jpa_vs_mybatis.mapper.ProdutoMapper;
import br.com.benchmark.jpa_vs_mybatis.model.Produto;
import br.com.benchmark.jpa_vs_mybatis.repository.ProdutoRepositoryJPA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 1)
@Measurement(iterations = 2)
public class PerformanceBenchmark {

    private ConfigurableApplicationContext context;
    private ProdutoRepositoryJPA produtoRepositoryJPA;
    private ProdutoMapper produtoMapper;

    private List<Long> idsParaBusca = new ArrayList<>();
    private final Random random = new Random();

    @Setup(Level.Trial)
    public void setup() {
        context = new SpringApplicationBuilder(JpaVsMybatisBenchmarkApplication.class)
                .web(WebApplicationType.NONE)
                .run();
        produtoRepositoryJPA = context.getBean(ProdutoRepositoryJPA.class);
        produtoMapper = context.getBean(ProdutoMapper.class);

        System.out.println("Populando o banco para testes de busca...");
        produtoRepositoryJPA.deleteAll();
        List<Produto> produtosIniciais = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Produto p = new Produto();
            p.setNome("Produto Teste " + i);
            p.setPreco(1.99 + i);
            p.setQuantidade(100);
            produtosIniciais.add(p);
        }
        produtoRepositoryJPA.saveAll(produtosIniciais).forEach(p -> idsParaBusca.add(p.getId()));
        System.out.println("Banco populado com " + idsParaBusca.size() + " registros.");
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        context.close();
    }

    @Setup(Level.Invocation)
    public void prepare() {
        produtoRepositoryJPA.deleteAll();
        produtoMapper.deleteAll();
    }

    // --- BENCHMARKS ---

    @Benchmark
    public void testeInsercaoJPA() {
        Produto p = new Produto();
        p.setNome("Produto JPA");
        p.setPreco(100.0);
        p.setQuantidade(10);
        produtoRepositoryJPA.save(p);
    }

    @Benchmark
    public void testeInsercaoMyBatis() {
        Produto p = new Produto();
        p.setNome("Produto MyBatis");
        p.setPreco(100.0);
        p.setQuantidade(10);
        produtoMapper.save(p);
    }

    @Benchmark
    public void testeBatchInsertJPA() {
        List<Produto> produtos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Produto p = new Produto();
            p.setNome("Produto Batch JPA");
            p.setPreco(50.0);
            p.setQuantidade(5);
            produtos.add(p);
        }
        produtoRepositoryJPA.saveAll(produtos);
    }

    @Benchmark
    public void testeBatchInsertMyBatis() {
        List<Produto> produtos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Produto p = new Produto();
            p.setNome("Produto Batch MyBatis");
            p.setPreco(50.0);
            p.setQuantidade(5);
            produtos.add(p);
        }
        produtoMapper.saveAll(produtos);
    }

    @Benchmark
    public void testeFindByIdJPA() {
        Long idAleatorio = idsParaBusca.get(random.nextInt(idsParaBusca.size()));
        produtoRepositoryJPA.findById(idAleatorio);
    }

    @Benchmark
    public void testeFindByIdMyBatis() {
        Long idAleatorio = idsParaBusca.get(random.nextInt(idsParaBusca.size()));
        produtoMapper.findById(idAleatorio);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}