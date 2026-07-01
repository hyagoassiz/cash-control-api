# Objetivo

Gere código consistente com o restante do projeto.

Sempre priorize:

1. Simplicidade.
2. Legibilidade.
3. Reutilização.
4. Consistência com os padrões existentes.

Nunca implemente soluções mais complexas do que o necessário para atender ao requisito.

---

# Antes de implementar

Antes de escrever qualquer código:

- Analise como funcionalidades semelhantes foram implementadas no projeto.
- Reutilize classes, services, repositories, DTOs, entidades e utilitários existentes sempre que possível.
- Mantenha consistência com a nomenclatura e os padrões já existentes no projeto.
- Não crie novos arquivos se a alteração puder ser realizada mantendo a organização existente.
- Prefira evoluir implementações existentes antes de criar novas.
- Não crie abstrações sem necessidade.
- Não altere código não relacionado à solicitação.
- Caso exista dúvida sobre alguma regra de negócio, peça esclarecimentos antes de implementar.

---

# Arquitetura

Este projeto utiliza arquitetura orientada a módulos (feature-first).

Organize o código por domínio de negócio, e não por camada técnica global.

Cada classe deve possuir apenas uma responsabilidade.

Fluxo padrão:

Controller

↓

Service

↓

Repository

Regras:

- Cada funcionalidade deve ficar dentro de `modules/<feature>`.
- Não crie pacotes globais como `controller`, `service`, `repository`, `entity` ou `dto` na raiz do projeto.
- Cada módulo deve conter apenas as pastas necessárias.
- Não crie pacotes vazios.
- Não antecipe módulos para funcionalidades futuras.
- Código compartilhado entre módulos deve ficar em `common`.
- Configurações globais devem ficar em `config`.
- Tratamento global de exceções deve ficar em `exception`.
- Controllers recebem requisições e retornam respostas.
- Services concentram regras de negócio.
- Repositories acessam o banco de dados.
- Não coloque regra de negócio em Controllers.
- Não coloque regra de negócio em Repositories.
- Não acesse o banco diretamente em Controllers.
- Não utilize Services chamando Controllers.

---

# Estrutura do Projeto

Utilize a seguinte organização:

```txt
src/main/java
├── config
├── common
├── exception
└── modules
    ├── auth
    │   ├── controller
    │   ├── service
    │   ├── repository
    │   ├── entity
    │   ├── dto
    │   └── mapper
    │
    ├── users
    ├── accounts
    ├── categories
    └── transactions
```

Regras:

- Cada domínio deve possuir seu próprio módulo.
- Crie apenas as pastas necessárias para cada módulo.
- Não crie pastas vazias.
- Não antecipe estruturas para funcionalidades futuras.
- Extraia código reutilizável apenas quando houver benefício claro de reutilização ou legibilidade.
- Priorize evoluir implementações existentes antes de criar novas abstrações.

---

# Java

- Nunca utilize tipos genéricos sem necessidade.
- Declare explicitamente os tipos.
- Evite casts desnecessários.
- Evite duplicação de código.
- Evite valores mágicos.
- Utilize constantes para valores reutilizados.
- Utilize interfaces apenas quando houver benefício claro.

---

# Spring Boot

- Utilize injeção de dependência por construtor.
- Nunca utilize field injection.
- Não utilize `@Autowired` em atributos.
- Utilize `@RestController`, `@Service` e `@Repository` conforme suas responsabilidades.
- Utilize configuração via `application.yml`.
- Prefira composição em vez de herança.

---

# Controllers

- Controllers devem apenas:
  - receber a requisição;
  - validar a entrada;
  - chamar o Service;
  - retornar a resposta.

- Não implemente regras de negócio.
- Utilize DTOs para entrada e saída.
- Nunca exponha entidades pela API.
- Utilize `ResponseEntity` quando necessário.

---

# Services

- Centralize toda regra de negócio.
- Não coloque regras de negócio em Controllers ou Repositories.
- Services não devem conhecer detalhes da camada HTTP.
- Não retorne `ResponseEntity`.
- Não utilize `HttpStatus`.
- Não manipule `HttpServletRequest` ou `HttpServletResponse`.

---

# Repositories

- Utilize Spring Data JPA.
- Prefira métodos derivados do Spring Data antes de criar queries customizadas.
- Utilize `@Query` apenas quando necessário.
- Não implemente regra de negócio.

---

# Entidades

- Utilize entidades apenas para persistência.
- Não utilize entidades como DTO.
- Utilize relacionamentos apenas quando necessários.
- Prefira `FetchType.LAZY`.
- Evite carregamentos desnecessários.

---

# DTOs

- Utilize DTOs para entrada e saída da API.
- Não reutilize entidades como Request ou Response.
- Mantenha DTOs simples.
- Não coloque regras de negócio em DTOs.

---

# Validação

- Utilize Bean Validation.
- Prefira validações declarativas.

Exemplos:

- `@NotNull`
- `@NotBlank`
- `@Email`
- `@Size`
- `@Positive`

Não replique validações que podem ser realizadas pelas anotações.

---

# Tratamento de Exceções

- Centralize o tratamento utilizando `@ControllerAdvice`.
- Utilize exceções específicas.
- Não capture `Exception` genericamente sem necessidade.
- Não retorne mensagens de erro diretamente da regra de negócio.

---

# Persistência

- Utilize `@Transactional` apenas quando necessário.
- Prefira utilizar transações na camada de Service.
- Evite consultas desnecessárias.
- Evite consultas repetidas para os mesmos dados.

---

# Métodos

- Declare explicitamente o tipo de retorno.
- Mantenha métodos pequenos.
- Cada método deve possuir apenas uma responsabilidade.
- Utilize nomes claros e descritivos.

---

# Nomenclatura

Utilize inglês para todos os identificadores do código.

Inclui:

- módulos;
- pacotes;
- classes;
- interfaces;
- entidades;
- DTOs;
- controllers;
- services;
- repositories;
- métodos;
- variáveis;
- enums;
- constantes.

Não misture português e inglês no mesmo identificador.

Utilize nomes claros, descritivos e consistentes.

Sempre que possível, utilize a nomenclatura adotada pelo ecossistema Java e Spring Boot.

Os textos exibidos ao usuário devem utilizar o idioma da aplicação.

Exemplos corretos:

- UserController
- UserService
- UserRepository
- CreateUserRequest
- UserResponse
- createUser
- findByEmail

Exemplos incorretos:

- UsuarioController
- CadastroUsuarioService
- salvarUsuario
- buscarPorEmail

---

# Projeto

Este projeto segue uma arquitetura incremental.

- Implemente apenas o necessário para atender ao requisito atual.
- Não antecipe funcionalidades.
- Evolua classes e abstrações apenas quando houver necessidade real.
- Sempre priorize simplicidade, legibilidade e facilidade de manutenção.
