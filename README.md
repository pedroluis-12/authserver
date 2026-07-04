# AuthServer - Servidor de Autenticação com Spring Security e JWT

Este projeto é um servidor de autenticação desenvolvido com Spring Boot, Spring Security e JSON Web Tokens (JWT). Ele demonstra a implementação de um sistema de segurança robusto para aplicações backend, incluindo registro de usuários, login, geração e validação de tokens de acesso.

## Funcionalidades

*   **Autenticação de Usuários**: Permite que usuários se autentiquem usando credenciais (usuário/senha).
*   **Autorização Baseada em JWT**: Utiliza JWT para proteger endpoints da API, garantindo que apenas usuários autenticados e autorizados possam acessá-los.
*   **Registro de Usuários**: Funcionalidade para criar novas contas de usuário.
*   **Gerenciamento de Avatar**:
    *   Usuários agora possuem um `avatarUrl` que é gerado automaticamente no cadastro.
    *   O sistema tenta buscar um avatar no Gravatar com base no e-mail do usuário.
    *   Caso não encontre no Gravatar, um avatar é gerado via UI-Avatars com as iniciais do nome do usuário.
    *   O avatar gerado é salvo no AWS S3 e sua URL é persistida no perfil do usuário.
*   **Spring Security**: Configuração completa do Spring Security para gerenciamento de segurança.
*   **JWT**: Integração da biblioteca JJWT para manipulação de JSON Web Tokens.

## Tecnologias Utilizadas

*   **Spring Boot**: Framework para construção de aplicações Java robustas e escaláveis.
*   **Spring Security**: Framework de segurança para aplicações Spring.
*   **Spring WebFlux**: Para requisições HTTP não bloqueantes (WebClient).
*   **AWS SDK**: Integração com serviços AWS, especificamente S3 para armazenamento de avatares.
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
*   `files`: Classes relacionadas ao armazenamento de arquivos, incluindo integração com AWS S3.
*   `config`: Classes de configuração adicionais, como a do `WebClient`.

## Endpoints Principais

### RoleController
*   `POST /roles`: Cria uma nova role.
*   `GET /roles`: Lista todas as roles.

### UserController
*   `GET /users`: Lista todos os usuários, incluindo o `avatarUrl`.
*   `POST /users`: Cria um novo usuário, gerando e salvando automaticamente um `avatarUrl`.
*   `POST /users/login`: Autentica um usuário e retorna um JWT.
*   `GET /users/{id}`: Retorna um usuário pelo ID, incluindo o `avatarUrl`.
*   `PATCH /users/{id}`: Atualiza um usuário pelo ID.
*   `DELETE /users/{id}`: Deleta um usuário pelo ID.
*   `DELETE /users/{id}/avatar`: Reseta o avatar de um usuário, gerando um novo e atualizando no S3 e no banco de dados.
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

## Vídeo Explicativo Avatar S3
https://youtu.be/sEgPdEcoeBE

Para detalhes específicos sobre a implementação, consulte o código-fonte nos pacotes mencionados.