# AuthServer - Servidor de Autenticação com Spring Security e JWT

Este projeto é um servidor de autenticação desenvolvido com Spring Boot, Spring Security e JSON Web Tokens (JWT). Ele demonstra a implementação de um sistema de segurança robusto para aplicações backend, incluindo registro de usuários, login, geração e validação de tokens de acesso.

## Funcionalidades

*   **Autenticação de Usuários**: Permite que usuários se autentiquem usando credenciais (usuário/senha).
*   **Autorização Baseada em JWT**: Utiliza JWT para proteger endpoints da API, garantindo que apenas usuários autenticados e autorizados possam acessá-los.
*   **Registro de Usuários**: Funcionalidade para criar novas contas de usuário.
*   **Spring Security**: Configuração completa do Spring Security para gerenciamento de segurança.
*   **JJWT**: Integração da biblioteca JJWT para manipulação de JSON Web Tokens.

## Tecnologias Utilizadas

*   **Spring Boot**: Framework para construção de aplicações Java robustas e escaláveis.
*   **Spring Security**: Framework de segurança para aplicações Spring.
*   **JJWT**: Biblioteca para criação e consumo de JSON Web Tokens.
*   **Gradle**: Ferramenta de automação de build.

## Como Rodar o Projeto

1.  **Clone o repositório**:
    ```bash
    git clone <URL_DO_REPOSITORIO>
    cd authserver
    ```
2.  **Construa o projeto**:
    ```bash
    ./gradlew build
    ```
3.  **Execute a aplicação**:
    ```bash
    java -jar build/libs/authserver-0.0.1-SNAPSHOT.jar
    ```
    Ou, se estiver usando uma IDE como o IntelliJ IDEA, você pode executar a classe principal `AuthserverApplication`.

## Estrutura do Projeto (Pacotes Chave)

*   `security`: Contém as classes de configuração do Spring Security, filtros JWT e provedores de autenticação.
*   `controller`: Define os endpoints da API para registro, login e recursos protegidos.
*   `service`: Lógica de negócio para gerenciamento de usuários e autenticação.
*   `repository`: Interfaces para acesso a dados.

## Endpoints Principais

### RoleController
*   `POST /roles`: Cria uma nova role.
*   `GET /roles`: Lista todas as roles.

### UserController
*   `GET /users`: Lista todos os usuários.
*   `POST /users`: Cria um novo usuário.
*   `POST /users/login`: Autentica um usuário e retorna um JWT.
*   `GET /users/{id}`: Retorna um usuário pelo ID.
*   `PATCH /users/{id}`: Atualiza um usuário pelo ID.
*   `DELETE /users/{id}`: Deleta um usuário pelo ID.
*   `PUT /users/{id}/roles/{role}`: Adiciona uma role a um usuário.

### LoanController
*   `POST /loans`: Cria um novo empréstimo.
*   `GET /loans/{id}`: Retorna um empréstimo pelo ID.
*   `GET /loans`: Lista todos os empréstimos com filtros opcionais.
*   `PUT /loans/{id}`: Atualiza um empréstimo pelo ID.
*   `DELETE /loans/{id}`: Deleta um empréstimo pelo ID.

### BookController
*   `POST /books`: Cria um novo livro.
*   `GET /books/{id}`: Retorna um livro pelo ID.
*   `GET /books`: Lista todos os livros com filtros opcionais.
*   `PUT /books/{id}`: Atualiza um livro pelo ID.
*   `DELETE /books/{id}`: Deleta um livro pelo ID.

Para detalhes específicos sobre a implementação, consulte o código-fonte nos pacotes mencionados.