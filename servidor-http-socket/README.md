# Servidor HTTP Simples em C

Este projeto implementa um servidor HTTP básico utilizando a biblioteca `Winsock` no ambiente Windows. O servidor é capaz de lidar com requisições HTTP do tipo GET e responder com arquivos estáticos, como páginas HTML, estilos CSS e scripts JavaScript. O objetivo deste projeto é demonstrar a criação de um servidor web simples para entender a comunicação cliente-servidor e o protocolo HTTP.


## Funcionalidades do Servidor

O servidor possui as seguintes funcionalidades:

- **Tratamento de Requisições GET**: Responde a requisições GET, servindo arquivos estáticos como HTML, CSS e JS.
- **Respostas de Erro HTTP**:
  - `404 - Not Found`: Retorna uma página HTML padrão se o recurso solicitado não for encontrado.
  - `405 - Method Not Allowed`: Retorna uma página HTML padrão para métodos HTTP não suportados (por exemplo, POST).
  - `400 - Bad Request`: Retorna uma mensagem de erro para solicitações malformadas.
- **Tipos MIME Suportados**: Retorna o tipo MIME correto com base na extensão do arquivo solicitado (.html, .css, .js).
- **Suporte a Multithreading**: O servidor pode lidar com várias conexões simultaneamente usando _threads_ para cada solicitação de cliente.

## Requisitos

Para compilar e executar o servidor, é necessário ter o seguinte:

- **Sistema Operacional**: Windows (devido ao uso da biblioteca `Winsock`).
- **Compilador C**: Recomendado usar MinGW ou Visual Studio com suporte para `Winsock2`.
- **Permissões**: Acesso para executar o servidor na porta especificada e ler arquivos do diretório `www`.

## Compilação e Execução

### Passo 1: Configuração do Ambiente

1. Instale o [MinGW](http://mingw-w64.org/doku.php) ou o [Visual Studio](https://visualstudio.microsoft.com/pt-br/downloads/) com suporte para desenvolvimento em C/C++.
2. Certifique-se de que a biblioteca `Winsock2` esteja incluída no seu ambiente.

### Passo 2: Compilação do Código
1. Navegue até o diretório onde o arquivo `servidor.c` está localizado.
2. Compile o código usando o seguinte comando (no caso de MinGW):
   ```bash
   gcc servidor.c -o servidor.exe -lws2_32
Esse comando compila o arquivo servidor.c e gera um executável chamado servidor.exe, linkando a biblioteca Winsock2.

### Passo 3: Execução do Servidor
Execute o servidor com o comando:
./servidor.exe
O servidor começará a escutar conexões na porta 8888.

### Passo 4: Acesso ao Servidor
Abra o navegador de sua preferência (Chrome, Firefox, etc.).
Digite o seguinte endereço na barra de navegação:
http://localhost:8888

Você deverá ver a página inicial index.html carregada a partir do diretório configurado (www).

### Modificação do Projeto

Se desejar alterar o diretório onde os arquivos estáticos estão armazenados:
Abra o arquivo servidor.c.

Modifique a linha que define o diretório base:
#define LOCATION "C:\\www\\"
Altere para o caminho desejado, por exemplo:
#define LOCATION "D:\\projetos\\meu-servidor\\"

Recompile o projeto usando o comando descrito anteriormente.
