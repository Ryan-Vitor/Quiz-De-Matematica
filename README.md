# Moveniba - Quiz de Matemática

## 1. Objetivo do APP

O Moveniba é um aplicativo de quiz de matemática desenvolvido para Android. O objetivo principal é permitir que os usuários testem seus conhecimentos em matemática através de um quiz de 10 perguntas, enquanto competem por uma pontuação mais alta em um ranking local.

O aplicativo permite que os jogadores se cadastrem, joguem o quiz e tenham suas pontuações salvas, incentivando a competição e a melhoria contínua.

## 2. Tecnologias Utilizadas

O projeto foi construído utilizando tecnologias essenciais do ecossistema Android:

*   **RecyclerView**: Utilizado para exibir de forma eficiente a lista de jogadores cadastrados e suas respectivas pontuações na tela de ranking.
*   **SQLite**: Empregado para a persistência de dados. O app utiliza o banco de dados SQLite nativo do Android para salvar todas as informações dos jogadores (nome e pontuação), garantindo que os dados não se percam ao fechar o aplicativo.

## 3. Estrutura do Banco de Dados

O banco de dados do aplicativo é simples e consiste em uma única tabela para armazenar os dados dos jogadores.

*   **Tabela:** `ranking`

| Campo     | Tipo      | Descrição                                         |
| --------- | --------- | --------------------------------------------------- |
| `_id`     | `INTEGER` | Chave primária autoincrementável (ID único do jogador). |
| `name`    | `TEXT`    | Armazena o nome do jogador.                         |
| `score`   | `INTEGER` | Armazena a maior pontuação alcançada pelo jogador.  |

## 4. Funcionalidades Implementadas (CRUD)

O aplicativo possui um sistema completo de gerenciamento de usuários (CRUD - Create, Read, Update, Delete), permitindo total controle sobre os perfis dos jogadores.

*   **Create (Criar):** Na tela principal, é possível digitar um nome e clicar em "Salvar Usuário" para criar um novo perfil de jogador. O jogador é criado com uma pontuação inicial de 0.

*   **Read (Ler):** 
    *   A lista de todos os jogadores cadastrados, junto com suas pontuações, é exibida na tela principal.
    *   Ao clicar no botão "Jogar", o app lê a lista de usuários e exibe um menu de seleção para que o jogador escolha com qual perfil deseja jogar.

*   **Update (Atualizar):**
    *   É possível editar o nome de um jogador existente clicando no botão "Editar" ao lado do seu nome.
    *   A pontuação de um jogador é atualizada automaticamente ao final de um quiz, mas apenas se a nova pontuação for maior que a anterior registrada.

*   **Delete (Deletar):** Um jogador pode ser permanentemente removido do banco de dados ao se clicar no botão "Excluir" correspondente.
