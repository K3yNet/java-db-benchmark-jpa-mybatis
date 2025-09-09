# Análise de Performance: JPA/Hibernate vs. MyBatis

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)
![Maven](https://img.shields.io/badge/Maven-3.9-red.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)
![Docker](https://img.shields.io/badge/Docker-blue.svg)

## 🎯 Objetivo

Este projeto realiza uma análise de performance detalhada comparando duas das mais populares ferramentas de persistência de dados no ecossistema Java: **Spring Data JPA (com Hibernate)** e **MyBatis**.

O objetivo é fornecer dados concretos, obtidos através de benchmarks precisos, para entender os trade-offs de desempenho em cenários comuns de CRUD (Create, Read, Update, Delete).

## 🛠️ Stack de Tecnologias

Para garantir um ambiente de teste robusto, moderno e reproduzível, foram utilizadas as seguintes tecnologias:
- **Java 21** e **Spring Boot 3**
- **Maven** para gerenciamento de dependências e build
- **PostgreSQL 15** como banco de dados, rodando em um contêiner **Docker**
- **JMH (Java Microbenchmark Harness)** para a execução dos benchmarks com precisão científica
- **Testcontainers** para a suíte de testes de integração automatizados

## 🚀 Como Executar o Projeto

Para replicar os testes, siga os passos abaixo.

### Pré-requisitos
- JDK 21 (ou superior) instalado
- Apache Maven instalado
- Docker e Docker Compose instalados e em execução

### Passo a Passo

1.  **Clone o Repositório**
    ```bash
    git clone [https://github.com/K3yNet/java-db-benchmark-jpa-mybatis.git](https://github.com/K3yNet/java-db-benchmark-jpa-mybatis.git)
    cd java-db-benchmark-jpa-mybatis
    ```

2.  **Inicie o Banco de Dados**
    Com o Docker em execução, suba o contêiner do PostgreSQL usando o Docker Compose.
    ```bash
    docker compose up -d
    ```
    Isso irá iniciar o banco de dados em segundo plano.

3.  **Construa o Projeto**
    Use o Maven para compilar o código e copiar todas as dependências necessárias.
    ```bash
    mvn clean package
    ```
    Ao final, a pasta `target/` será criada, contendo o JAR do projeto e uma subpasta `lib/` com todas as dependências.

4.  **Execute o Benchmark**
    Use o seguinte comando para iniciar a suíte de testes de performance do JMH.
    
    * **Para Linux/macOS:**
        ```bash
        java -cp "target/jpa-vs-mybatis-0.0.1-SNAPSHOT.jar:target/lib/*" br.com.benchmark.jpa_vs_mybatis.PerformanceBenchmark
        ```
    * **Para Windows (PowerShell/CMD):**
        ```bash
        java -cp "target/jpa-vs-mybatis-0.0.1-SNAPSHOT.jar;target/lib/*" br.com.benchmark.jpa_vs_mybatis.PerformanceBenchmark
        ```
    O processo pode levar alguns minutos. Ao final, uma tabela com os resultados será exibida no console.

## 🏆 Resultados Obtidos

Os benchmarks foram executados medindo o tempo médio por operação (ms/op). Valores menores indicam melhor performance.

| Teste | Framework | Tempo Médio (Score) | Vencedor e Vantagem |
| :--- | :--- | :--- | :--- |
| **Inserção Única** | JPA/Hibernate | `1,781 ms/op` | **MyBatis (134% mais rápido)** |
| **Inserção Única** | **MyBatis** | **`0,761 ms/op`** | |
| **Busca por ID** | JPA/Hibernate | `0,718 ms/op` | **MyBatis (147% mais rápido)** |
| **Busca por ID** | **MyBatis** | **`0,290 ms/op`** | |
| **Inserção em Lote (100 itens)** | JPA/Hibernate | `13,125 ms/op` | **MyBatis (450% mais rápido)** |
| **Inserção em Lote (100 itens)**| **MyBatis**| **`2,416 ms/op`** | |


## 💡 Análise e Conclusão

Os números mostram um padrão claro de desempenho nos cenários testados:

-   ⚡ **CRUD Simples (Insert/FindById):** O MyBatis foi consistentemente **mais de 2 vezes mais rápido**. Seu baixo overhead e a execução direta do SQL, sem as camadas de gerenciamento de estado do Hibernate, provam ser mais eficientes para operações diretas.

-   🔥 **Inserção em Lote (Batch):** A diferença foi massiva. O MyBatis se mostrou **6x mais rápido**. A sua capacidade de gerar um único comando `INSERT` otimizado com múltiplos `VALUES` (via tag `<foreach>`) é extremamente performática e superior à abordagem de batching do Hibernate neste cenário.

### Conclusão Final

Embora os números favoreçam o **MyBatis** em performance bruta, a escolha de um framework de persistência envolve trade-offs.

-   **JPA/Hibernate** é uma ferramenta fantástica que maximiza a **produtividade**. A abstração do SQL e a simplicidade dos `JpaRepository` aceleram o desenvolvimento da maioria das aplicações CRUD.
-   **MyBatis** é a escolha ideal quando a **performance é o requisito mais crítico**. Ele oferece controle total sobre o SQL, permitindo otimizações finas que são cruciais para sistemas de alta carga, relatórios e operações em lote.

A questão final não é "qual é o melhor?", mas sim: **"Qual ferramenta é a mais adequada para o meu problema específico?"**

## 📝 Licença
Este projeto está sob a licença MIT.