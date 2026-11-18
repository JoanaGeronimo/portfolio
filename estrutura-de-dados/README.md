# Sistema de Gerenciamento de Estacionamento em C

## Sobre o projeto

Este projeto consiste em um sistema de gerenciamento de estacionamento desenvolvido na linguagem C, com o objetivo de aplicar e comparar diferentes estruturas de dados na organização e controle de veículos.

O sistema permite cadastrar, retirar e procurar veículos utilizando suas placas, oferecendo três opções de estruturas de dados: Lista Encadeada, Pilha e Fila.

## Tecnologias utilizadas

- Linguagem C
- Biblioteca stdio.h
- Biblioteca stdlib.h
- Biblioteca string.h
- Alocação dinâmica de memória

## Estruturas de dados utilizadas

### 1. Lista Encadeada
Permite armazenar os veículos em sequência, possibilitando:
- Inserir veículos no final da lista.
- Remover um veículo específico pela placa.
- Buscar veículos cadastrados.
- Percorrer e visualizar os veículos.

### 2. Pilha (Stack)
Utiliza o conceito LIFO (Last In, First Out), ou seja, o último veículo que entra é o primeiro que sai.

Funcionalidades:
- Empilhar veículos.
- Desempilhar veículos.
- Buscar veículos sem perder a organização original da pilha.
- Exibir os veículos utilizando uma pilha auxiliar.

### 3. Fila (Queue)
Utiliza o conceito FIFO (First In, First Out), ou seja, o primeiro veículo que entra é o primeiro que sai.

Funcionalidades:
- Adicionar veículos ao final da fila.
- Retirar veículos do início da fila.
- Buscar veículos sem alterar a ordem original.
- Exibir os veículos utilizando uma fila auxiliar.

## Funcionalidades do sistema

O programa possui um menu interativo que permite ao usuário escolher qual estrutura de dados deseja utilizar.

As operações disponíveis são:

1. **Estacionar carro:** cadastra um veículo utilizando sua placa.
2. **Retirar carro:** remove um veículo conforme o funcionamento da estrutura escolhida.
3. **Olhar carro:** procura um veículo pela placa e informa se ele foi encontrado.
4. **Voltar ao menu principal:** permite escolher outra estrutura de dados.

## Conceitos aplicados

Durante o desenvolvimento foram trabalhados os seguintes conceitos:

- Estruturas de dados.
- Listas encadeadas.
- Pilhas e filas.
- Ponteiros.
- Structs.
- Alocação dinâmica de memória com malloc().
- Liberação de memória com free().
- Manipulação de strings.
- Busca, inserção e remoção de elementos.
- Funções e modularização do código.

## Objetivo de aprendizagem

O objetivo do projeto foi colocar em prática os conceitos de estruturas de dados utilizando a linguagem C, compreendendo como diferentes formas de organização e armazenamento influenciam as operações de inserção, remoção e busca de elementos.

Além disso, o projeto permitiu aprofundar os conhecimentos sobre ponteiros, memória dinâmica e manipulação de estruturas encadeadas.
