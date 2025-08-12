# FórumHub - API RESt com Spring Boot
Projeto desenvolvido durante o curso de Desenvolvimento Back-end em Java, uma iniciativa da ONE do Tech Foundation — uma parceria entre Oracle Next Education e Alura.

## Objetivo do projeto
O FórumHub simula o funcionamento de um fórum real, permitindo que usuários autenticados criem, editem, consultem e excluam tópicos.
O projeto foi construído como um desafio prático para aplicar conceitos de back-end, banco de dados, segurança e boas práticas de desenvolvimento.

### Funcionalidades
- Criar novos tópicos
- Lista todos os tópicos cadastrados
- Buscar tópicos por ID
- Atualizar um tópico
- Excluir (inativar) um tópico
- Autenticação e autorização via JWT
- Restricções;
  - Apenas o Autor do tópico pode editar/deletar
  - Endpoints protegidos por autenticação

## 🛠 Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3 **
- **Spring Data JPA**
- **Spring Security**
- **MYSQL**
- **JWT (JSON Web Token)**
- **Hibernate**
- **Maven**
- **Insomnia (para testes de API)**
- **Swagger (documentação interativa)**

## Exemplo de Uso da API
### 🔑 Login:
O usuário deve estar previamente cadastrado. Caso contrário, não será possível realizar o login

**Erro - Usuário não cadastrado:**
<img width="966" height="247" alt="image" src="https://github.com/user-attachments/assets/cf190892-f02f-4ac0-8068-1fe34720a3bf" />

**Erro - E-mail ou senha inválidos**
<img width="1008" height="305" alt="image" src="https://github.com/user-attachments/assets/3a6b8a68-5e38-43ea-8698-9ced4609d452" />

**Login com sucesso**
- Ao informar e-mail e senha corretos, é gerado um token JWT
- Esse token será obrigatório para todas as demais requisições
<img width="1507" height="285" alt="image" src="https://github.com/user-attachments/assets/3ddd255e-6b81-481b-966c-11a8003ecf45" />

### 📝 Cadastrar Tópico
- É obrigatório informar o token

Sem o token não é possível cadastrar, listar, editar ou excluir tópicos
<img width="989" height="408" alt="image" src="https://github.com/user-attachments/assets/4ff94870-bbd1-4b52-b303-e947cefee53d" />

**Cadastro com token válido:**
<img width="1172" height="316" alt="image" src="https://github.com/user-attachments/assets/6fdc894d-3b4d-4484-bfbe-1fdf1cd446ad" />

**Restrição:** Não é possível cadastrar um tópico com título e mensagem já existentes
<img width="1076" height="252" alt="image" src="https://github.com/user-attachments/assets/3ddb1362-5faf-4d65-9282-f0d4dab58616" />

### ✏️ Editar tópico
- Apenas o **autor** pode editar o tópico
<img width="1343" height="346" alt="image" src="https://github.com/user-attachments/assets/6bc4dd3c-505d-432d-a113-ab551bf4052c" />

### ❌ Deletar tópico
- Apenas o **autor** do tópico pode deleta-lo

**Erro - Deletar tópico não autorizado**
<img width="1207" height="351" alt="image" src="https://github.com/user-attachments/assets/3750dab9-c72b-46f0-a151-7ada43d085d3" />

**Tópico removido**
- O tópico bão é removido do banco, apenas marcado como **inativo**.
<img width="935" height="294" alt="image" src="https://github.com/user-attachments/assets/cc103e99-294d-49c0-b37d-c28548be4b80" />

### 📄 Lsitar tópicos Ativos
- Não é necessário passar nada no **body** da requisição. , mas como todas as requisições é **obrigatório** informar o token.  
<img width="1144" height="901" alt="image" src="https://github.com/user-attachments/assets/04dd3744-038c-4632-bfa1-21ef86559cfe" />

### 🔍 Buscar tópico por ID
<img width="1430" height="354" alt="image" src="https://github.com/user-attachments/assets/9b3c1a61-8dc9-44af-99cf-8df1ca6ccb41" />

### 📋 Listar Todos os tópicos 
<img width="1143" height="880" alt="image" src="https://github.com/user-attachments/assets/a54900f2-af3f-41ce-b5f8-c3ae0a4517c2" />
