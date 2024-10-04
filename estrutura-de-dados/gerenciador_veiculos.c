#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_PLACA 15

typedef struct Celula {
    char placa[MAX_PLACA];
    struct Celula* next;
} Celula;

typedef struct Pilha {
    Celula* topo;
} Pilha;

typedef struct Fila {
    Celula* inicio;
    Celula* fim;
} Fila;

Celula* criarCelula(char* placa) {
    Celula* novaCelula = (Celula*)malloc(sizeof(Celula));
    strncpy(novaCelula->placa, placa, MAX_PLACA);
    novaCelula->placa[MAX_PLACA - 1] = '\0'; // Garantir null-terminação
    novaCelula->next = NULL;
    return novaCelula;
}

void inserirNoFinal(Celula** lista, char* placa) {
    Celula* novaCelula = criarCelula(placa);
    if (*lista == NULL) {
        *lista = novaCelula;
    } else {
        Celula* auxiliar = *lista;
        while (auxiliar->next != NULL) {
            auxiliar = auxiliar->next;
        }
        auxiliar->next = novaCelula;
    }
}

void removerCelula(Celula** lista, char* placa) {
    Celula* auxiliar = *lista;
    Celula* anterior = NULL;
    while (auxiliar != NULL && strcmp(auxiliar->placa, placa) != 0) {
        anterior = auxiliar;
        auxiliar = auxiliar->next;
    }
    if (auxiliar == NULL) {
        printf("Carro com placa %s não encontrado.\n", placa);
        return;
    }
    if (anterior == NULL) {
        *lista = auxiliar->next;
    } else {
        anterior->next = auxiliar->next;
    }
    free(auxiliar);
}

int buscarLista(Celula* lista, char* placa) {
    Celula* auxiliar = lista;
    while (auxiliar != NULL) {
        if (strcmp(auxiliar->placa, placa) == 0) {
            return 1;
        }
        auxiliar = auxiliar->next;
    }
    return 0;
}

void imprimirLista(Celula* lista) {
    Celula* auxiliar = lista;
    while (auxiliar != NULL) {
        printf("%s ", auxiliar->placa);
        auxiliar = auxiliar->next;
    }
    printf("\n");
}

void inicializarPilha(Pilha* pilha) {
    pilha->topo = NULL;
}

void empilhar(Pilha* pilha, char* placa) {
    Celula* novaCelula = criarCelula(placa);
    novaCelula->next = pilha->topo;
    pilha->topo = novaCelula;
}

char* desempilhar(Pilha* pilha) {
    if (pilha->topo == NULL) {
        return NULL;
    }
    Celula* auxiliar = pilha->topo;
    char* placa = (char*)malloc(MAX_PLACA * sizeof(char));
    strcpy(placa, auxiliar->placa);
    pilha->topo = auxiliar->next;
    free(auxiliar);
    return placa;
}

int buscarPilha(Pilha* pilha, char* placa) {
    Pilha pilhaTemp;
    inicializarPilha(&pilhaTemp);
    int encontrado = 0;
    char* placaRetirada;
    while ((placaRetirada = desempilhar(pilha)) != NULL) {
        if (strcmp(placaRetirada, placa) == 0) {
            encontrado = 1;
        }
        empilhar(&pilhaTemp, placaRetirada);
        free(placaRetirada);
    }
    while ((placaRetirada = desempilhar(&pilhaTemp)) != NULL) {
        empilhar(pilha, placaRetirada);
        free(placaRetirada);
    }
    return encontrado;
}

void imprimirPilha(Pilha* pilha) {
    Pilha pilhaTemp;
    inicializarPilha(&pilhaTemp);
    char* placa;
    while ((placa = desempilhar(pilha)) != NULL) {
        printf("%s ", placa);
        empilhar(&pilhaTemp, placa);
        free(placa);
    }
    while ((placa = desempilhar(&pilhaTemp)) != NULL) {
        empilhar(pilha, placa);
        free(placa);
    }
    printf("\n");
}

void inicializarFila(Fila* fila) {
    fila->inicio = NULL;
    fila->fim = NULL;
}

void enfileirar(Fila* fila, char* placa) {
    Celula* novaCelula = criarCelula(placa);
    if (fila->fim == NULL) {
        fila->inicio = novaCelula;
        fila->fim = novaCelula;
    } else {
        fila->fim->next = novaCelula;
        fila->fim = novaCelula;
    }
}

char* desenfileirar(Fila* fila) {
    if (fila->inicio == NULL) {
        return NULL;
    }
    Celula* auxiliar = fila->inicio;
    char* placa = (char*)malloc(MAX_PLACA * sizeof(char));
    strcpy(placa, auxiliar->placa);
    fila->inicio = auxiliar->next;
    if (fila->inicio == NULL) {
        fila->fim = NULL;
    }
    free(auxiliar);
    return placa;
}

int buscarFila(Fila* fila, char* placa) {
    Fila filaTemp;
    inicializarFila(&filaTemp);
    int encontrado = 0;
    char* placaRetirada;
    while ((placaRetirada = desenfileirar(fila)) != NULL) {
        if (strcmp(placaRetirada, placa) == 0) {
            encontrado = 1;
        }
        enfileirar(&filaTemp, placaRetirada);
        free(placaRetirada);
    }
    while ((placaRetirada = desenfileirar(&filaTemp)) != NULL) {
        enfileirar(fila, placaRetirada);
        free(placaRetirada);
    }
    return encontrado;
}

void imprimirFila(Fila* fila) {
    Fila filaTemp;
    inicializarFila(&filaTemp);
    char* placa;
    while ((placa = desenfileirar(fila)) != NULL) {
        printf("%s ", placa);
        enfileirar(&filaTemp, placa);
        free(placa);
    }
    while ((placa = desenfileirar(&filaTemp)) != NULL) {
        enfileirar(fila, placa);
        free(placa);
    }
    printf("\n");
}

void menuPrincipal() {
    printf("\nEscolha a técnica de estrutura de dados:\n");
    printf("1. Lista\n");
    printf("2. Pilha\n");
    printf("3. Fila\n");
    printf("4. Sair\n");
}

void menuAcao() {
    printf("\nEscolha uma ação:\n");
    printf("1. Estacionar carro\n");
    printf("2. Retirar carro\n");
    printf("3. Olhar carro\n");
    printf("4. Voltar ao menu principal\n");
}

int main() {
    int opcaoEstrutura;
    int opcaoAcao;
    char placa[MAX_PLACA];

    Celula* lista = NULL;
    Pilha pilhaPrincipal;
    Pilha pilhaSecundaria;
    Fila fila;

    while (1) {
        menuPrincipal();
        scanf("%d", &opcaoEstrutura);

        if (opcaoEstrutura == 4) {
            printf("Saindo...\n");
            break;
        }

        switch (opcaoEstrutura) {
            case 1:
                printf("Estrutura de Dados: Lista\n");
                break;
            case 2:
                inicializarPilha(&pilhaPrincipal);
                inicializarPilha(&pilhaSecundaria);
                printf("Estrutura de Dados: Pilha\n");
                break;
            case 3:
                inicializarFila(&fila);
                printf("Estrutura de Dados: Fila\n");
                break;
            default:
                printf("Opção inválida. Volte ao menu principal.\n");
                continue;
        }

        while (1) {
            menuAcao();
            scanf("%d", &opcaoAcao);

            switch (opcaoAcao) {
                case 1:
                    printf("Digite a placa do carro para estacionar: ");
                    scanf("%s", placa);
                    if (opcaoEstrutura == 1) {
                        inserirNoFinal(&lista, placa);
                    } else if (opcaoEstrutura == 2) {
                        empilhar(&pilhaPrincipal, placa);
                    } else if (opcaoEstrutura == 3) {
                        enfileirar(&fila, placa);
                    }
                    break;

                case 2:
                    printf("Digite a placa do carro para retirar: ");
                    scanf("%s", placa);
                    if (opcaoEstrutura == 1) {
                        removerCelula(&lista, placa);
                    } else if (opcaoEstrutura == 2) {
                        char* placaRetirada = desempilhar(&pilhaPrincipal);
                        if (placaRetirada) {
                            printf("Carro com placa %s retirado.\n", placaRetirada);
                            free(placaRetirada);
                        } else {
                            printf("Pilha vazia.\n");
                        }
                    } else if (opcaoEstrutura == 3) {
                        char* placaRetirada = desenfileirar(&fila);
                        if (placaRetirada) {
                            printf("Carro com placa %s retirado.\n", placaRetirada);
                            free(placaRetirada);
                        } else {
                            printf("Fila vazia.\n");
                        }
                    }
                    break;

                case 3:
                    printf("Digite a placa do carro para procurar: ");
                    scanf("%s", placa);
                    if (opcaoEstrutura == 1) {
                        if (buscarLista(lista, placa)) {
                            printf("Placa %s encontrada na lista.\n", placa);
                        } else {
                            printf("Placa %s não encontrada na lista.\n", placa);
                        }
                    } else if (opcaoEstrutura == 2) {
                        if (buscarPilha(&pilhaPrincipal, placa)) {
                            printf("Placa %s encontrada na pilha.\n", placa);
                        } else {
                            printf("Placa %s não encontrada na pilha.\n", placa);
                        }
                    } else if (opcaoEstrutura == 3) {
                        if (buscarFila(&fila, placa)) {
                            printf("Placa %s encontrada na fila.\n", placa);
                        } else {
                            printf("Placa %s não encontrada na fila.\n", placa);
                        }
                    }
                    break;

                case 4:
                    break;

                default:
                    printf("Ação inválida.\n");
                    break;
            }

            if (opcaoAcao == 4) {
                break;
            }
        }
    }

    return 0;
}
