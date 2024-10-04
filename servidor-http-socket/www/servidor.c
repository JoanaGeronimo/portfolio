#include <io.h>
#include <stdio.h>
#include <winsock2.h>
#include <string.h>
#include <process.h>
#pragma comment(lib, "ws2_32.lib")

#define PORT 8888
#define LOCATION "C:\\www\\"
#define BUFFER_SIZE 8192

WSADATA wsa;
SOCKET servidorSocket;
struct sockaddr_in servidor, cliente;

void enviaResposta404(SOCKET clienteSocket) {
    const char *resposta =
        "HTTP/1.1 404 Not Found\r\n"
        "Content-Type: text/html\r\n"
        "Content-Length: 98\r\n\r\n"
        "<html><head><title>404 Not Found</title></head>"
        "<body><h1>404 - Página não encontrada</h1></body></html>";
    printf("Cabeçalho da Resposta 404:\n%s\n", resposta);
    send(clienteSocket, resposta, strlen(resposta), 0);
}

void enviaResposta405(SOCKET clienteSocket) {
    const char *resposta =
        "HTTP/1.1 405 Method Not Allowed\r\n"
        "Content-Type: text/html\r\n"
        "Content-Length: 108\r\n\r\n"
        "<html><head><title>405 Method Not Allowed</title></head>"
        "<body><h1>405 - Método não permitido</h1></body></html>";
    printf("Cabeçalho da Resposta 405:\n%s\n", resposta);
    send(clienteSocket, resposta, strlen(resposta), 0);
}

void enviaResposta400(SOCKET clienteSocket) {
    const char *resposta =
        "HTTP/1.1 400 Bad Request\r\n"
        "Content-Type: text/html\r\n"
        "Content-Length: 113\r\n\r\n"
        "<html><head><title>400 Bad Request</title></head>"
        "<body><h1>400 - Solicitação inválida</h1></body></html>";
    printf("Cabeçalho da Resposta 400:\n%s\n", resposta);
    send(clienteSocket, resposta, strlen(resposta), 0);
}

void inicializaWinsock() {
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0) {
        printf("Falha na inicialização da Winsock. Código de erro: %d\n", WSAGetLastError());
        exit(1);
    }
}

SOCKET criaSocket() {
    SOCKET s = socket(AF_INET, SOCK_STREAM, 0);
    if (s == INVALID_SOCKET) {
        printf("Não foi possível criar o socket. Código de erro: %d\n", WSAGetLastError());
        WSACleanup();
        exit(1);
    }
    return s;
}

void configuraServidor() {
    servidor.sin_family = AF_INET;
    servidor.sin_addr.s_addr = INADDR_ANY;
    servidor.sin_port = htons(PORT);

    if (bind(servidorSocket, (struct sockaddr *)&servidor, sizeof(servidor)) == SOCKET_ERROR) {
        printf("Bind falhou. Código de erro: %d\n", WSAGetLastError());
        closesocket(servidorSocket);
        WSACleanup();
        exit(1);
    }

    if (listen(servidorSocket, 5) == SOCKET_ERROR) {
        printf("Erro ao escutar na porta. Código de erro: %d\n", WSAGetLastError());
        closesocket(servidorSocket);
        WSACleanup();
        exit(1);
    }

    printf("Servidor escutando na porta %d\n", PORT);
}

const char* determinaTipoMIME(const char *caminho) {
    const char *extensao = strrchr(caminho, '.');
    if (extensao != NULL) {
        if (strcmp(extensao, ".html") == 0) return "text/html";
        if (strcmp(extensao, ".css") == 0) return "text/css";
        if (strcmp(extensao, ".js") == 0) return "application/javascript";
    }
    return NULL;
}

int calculaTamanhoEHeader(const char *caminhoArquivo, char *header, int tamanhoMaximoHeader) {
    FILE *arquivo = fopen(caminhoArquivo, "rb");
    if (arquivo == NULL) {
        return 0;
    }

    fseek(arquivo, 0, SEEK_END);
    long tamanhoArquivo = ftell(arquivo);
    fseek(arquivo, 0, SEEK_SET);

    const char *tipoMIME = determinaTipoMIME(caminhoArquivo);
    if (tipoMIME == NULL) {
        fclose(arquivo);
        return -1;
    }

    snprintf(header, tamanhoMaximoHeader,
             "HTTP/1.1 200 OK\r\n"
             "Content-Type: %s\r\n"
             "Content-Length: %ld\r\n\r\n",
             tipoMIME, tamanhoArquivo);

    fclose(arquivo);
    return tamanhoArquivo;
}

void trataRequisicao(void *param) {
    SOCKET clienteSocket = *(SOCKET *)param;
    char buffer[BUFFER_SIZE], caminhoArquivo[512];
    int tamanhoRecebido;

    tamanhoRecebido = recv(clienteSocket, buffer, sizeof(buffer) - 1, 0);
    if (tamanhoRecebido <= 0) {
        closesocket(clienteSocket);
        _endthread();
    }

    buffer[tamanhoRecebido] = '\0';

    if (strncmp(buffer, "GET ", 4) != 0) {
        enviaResposta405(clienteSocket);
        closesocket(clienteSocket);
        _endthread();
    }

    if (sscanf(buffer, "GET %s HTTP", caminhoArquivo) != 1) {
        enviaResposta400(clienteSocket);
        closesocket(clienteSocket);
        _endthread();
    }

    if (strcmp(caminhoArquivo, "/") == 0) {
        strcpy(caminhoArquivo, "/index.html");
    }

    char caminhoCompleto[BUFFER_SIZE];
    snprintf(caminhoCompleto, sizeof(caminhoCompleto), "%s%s", LOCATION, caminhoArquivo + 1);

    printf("Caminho do Arquivo: %s\n", caminhoCompleto);

    char header[BUFFER_SIZE];
    int tamanhoArquivo = calculaTamanhoEHeader(caminhoCompleto, header, sizeof(header));

    if (tamanhoArquivo == 0) {
        enviaResposta404(clienteSocket);
    } else if (tamanhoArquivo == -1) {
        enviaResposta400(clienteSocket);
    } else {
        printf("Cabeçalho da Resposta:\n%s\n", header);
        send(clienteSocket, header, strlen(header), 0);

        FILE *arquivo = fopen(caminhoCompleto, "rb");
        if (arquivo != NULL) {
            char conteudo[BUFFER_SIZE];
            int lido;
            while ((lido = fread(conteudo, 1, sizeof(conteudo), arquivo)) > 0) {
                send(clienteSocket, conteudo, lido, 0);
            }
            fclose(arquivo);
        }
    }

    closesocket(clienteSocket);
    _endthread();
}

int main() {
    inicializaWinsock();
    servidorSocket = criaSocket();
    configuraServidor();

    printf("Aguardando conexões...\n");
    while (1) {
        int clienteTamanho = sizeof(cliente);
        SOCKET clienteSocket = accept(servidorSocket, (struct sockaddr *)&cliente, &clienteTamanho);
        if (clienteSocket == INVALID_SOCKET) {
            printf("Aceitação de conexão falhou. Código de erro: %d\n", WSAGetLastError());
            continue;
        }
        _beginthread(trataRequisicao, 0, &clienteSocket);
    }

    closesocket(servidorSocket);
    WSACleanup();
    return 0;
}
