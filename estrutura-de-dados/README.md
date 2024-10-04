### Estruturas de Dados em C
Este projeto implementa exemplos práticos de manipulação de estruturas de dados, como Lista, Pilha e Fila, utilizando a linguagem C. Cada estrutura possui funções específicas para inserção, remoção e busca de elementos.

### Funcionalidades
Lista: Inserção, remoção e busca de elementos.
Pilha: Empilhamento e desempilhamento, além de verificação de elementos.
Fila: Enfileiramento e desenfileiramento com suporte a busca de elementos.

### Requisitos para Execução
Compilador C (gcc ou outro de sua escolha).
Sistema operacional Windows (devido ao uso da biblioteca winsock2).

### Como Executar
Compile o arquivo principal utilizando o compilador C de sua escolha. Exemplo:
gcc -o estruturas main.c -lws2_32
Execute o programa no terminal:
./estruturas

### Estrutura do Código

Funções para Lista:

inserirNoFinal - Insere um novo elemento no final da lista.
removerCelula - Remove um elemento específico da lista.
buscarLista - Busca um elemento na lista.
Funções para Pilha:

empilhar - Empilha um novo elemento no topo da pilha.
desempilhar - Remove o elemento do topo da pilha.
buscarPilha - Busca um elemento na pilha.
Funções para Fila:

enfileirar - Adiciona um novo elemento ao final da fila.
desenfileirar - Remove o primeiro elemento da fila.
buscarFila - Busca um elemento na fila.

### Estruturas Implementadas

Lista: Implementada utilizando células encadeadas para manipulação de carros estacionados.
Pilha: Manipulação baseada no LIFO (Last In, First Out).
Fila: Manipulação baseada no FIFO (First In, First Out).
