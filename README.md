# Sistema de Gerenciamento de Biblioteca

Este projeto tem como objetivo implementar um sistema de gerenciamento de biblioteca utilizando **Java** e **Spring Boot**.  
O sistema permite o controle de usuários, livros, empréstimos e multas, garantindo uma gestão simples e eficiente.

---

## Tecnologias utilizadas
- Java 17+
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

---

## Ferramentas de Desenvolvimento
- **Spring Tool Suite (STS)**
- **Eclipse IDE**
- Git/GitHub para versionamento de código

---

## Repositório do Front-End
O front-end deste projeto está disponível em:  
[Repositório Front-End](https://github.com/vynnyss/biblioteca-front)

---

## Documentação
A documentação completa do sistema está disponível em PDF:  
[Documentação PDF](./docs/documentacao_gerenciamento_de_biblioteca.pdf)

---

## Integrantes do Projeto
- [Caio Felix](https://github.com/Caio-Felix1) 
- [Vinicius Sousa](https://github.com/vynnyss) 
- [Diego Fonseca](https://github.com/Diegopkg100)
- [Gabriel Vicente](https://github.com/Gabriel-Aiala)

---

## Pré-requisitos

Antes de executar a aplicação, certifique-se de que os seguintes requisitos estão atendidos:

- **Java JDK 17+** instalado e configurado
- **Apache Maven** instalado e configurado
- **MySQL** em execução na porta padrão (3306) ou conforme configurado no `application.properties`

> É necessário que o servidor MySQL esteja rodando antes de iniciar a aplicação, caso contrário ocorrerão erros de conexão.

---

## Como executar a aplicação

### Passo 1 — Clonar o repositório
```bash
git clone https://github.com/Caio-Felix1/projeto-biblioteca-springboot.git
```

### Passo 2 — Configurar application.properties a partir do exemplo
Use o modelo:
[modelo de application.properties](./src/main/resources/application.properties.example)

- **Banco de dados (MySQL):** configure URL, usuário e senha.
- **SMTP:** defina host, porta, usuário e senha.
- **Perfil ativo (`spring.profiles.active`):** `dev`.

Exemplo:
```properties
spring.profiles.active=dev

spring.datasource.url=jdbc:mysql://localhost:3306/sistemabiblioteca?useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

spring.mail.host=smtp.exemplo.com
spring.mail.port=587
spring.mail.username=usuario_exemplo
spring.mail.password=senha_exemplo
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Passo 3 — Executar o projeto
```bash
mvn spring-boot:run
```

### Passo 4 — A aplicação estará disponível na porta **8080** ou conforme configurado no application.properties: 
```text
http://localhost:8080
```
> A maioria dos endpoints estão protegidos por autenticação.  
> Para acessá-los, é necessário realizar login ou fornecer um token válido.  
> Por isso, o ideal é acessar a aplicação pelo front-end, seguindo o passo a passo do repositório a seguir:  
> [Repositório Front-End](https://github.com/vynnyss/biblioteca-front)

---

## Como executar os testes

Este projeto utiliza **JUnit** para testes unitários e de integração.  

### Passo 1 — Certifique-se de estar com o perfil `test` ativo
No arquivo `application.properties`, defina:
```properties
spring.profiles.active=test
```

### Passo 2 — Garanta que o banco de dados esteja vazio
Os testes dependem de um banco limpo para funcionarem corretamente.  
Você pode garantir isso configurando no `application.properties` do perfil `test`:
```properties
spring.jpa.hibernate.ddl-auto=create-drop
```
Essa opção recria o schema a cada execução, garantindo que o banco esteja vazio no início dos testes.

### Passo 3 — Executar todos os testes
```bash
mvn test
```
