# Sistema de Gerenciamento de Vendas e Clientes

## Sobre o projeto

Este projeto foi desenvolvido utilizando PostgreSQL e tem como objetivo implementar um banco de dados para gerenciar clientes, produtos, vendas, pagamentos e informações de localização.

O banco de dados utiliza recursos do PostgreSQL para automatizar operações, manter a integridade das informações e facilitar o gerenciamento das vendas.

## Tecnologias utilizadas

- PostgreSQL
- SQL
- PL/pgSQL

## Estrutura do banco de dados

O projeto possui as seguintes tabelas:

| Tabela | Descrição |
|---|---|
| `cliente` | Armazena os dados dos clientes. |
| `venda` | Registra as vendas realizadas. |
| `produto` | Armazena informações dos produtos e estoque. |
| `item_venda` | Relaciona produtos às vendas. |
| `estado` | Armazena informações dos estados. |
| `cidade` | Armazena informações das cidades. |
| `bairro` | Registra os bairros. |
| `endereco` | Armazena os endereços e CEPs. |
| `geo` | Armazena informações geográficas. |
| `resumo_diario` | Registra os recebimentos e saldos diários. |

## Funcionalidades implementadas

### 1. Gerenciamento de clientes
- Cadastro de clientes.
- Preenchimento automático de informações de localização a partir do CEP.
- Controle de clientes ativos e bloqueados.
- Acompanhamento da quantidade de vendas e do total comprado.
- Controle de limite de compras a prazo.

### 2. Gerenciamento de vendas
- Registro de vendas.
- Verificação da existência do CEP.
- Impedimento de vendas para clientes bloqueados.
- Atualização automática da data da venda.
- Controle de pagamentos.

### 3. Gerenciamento de produtos e estoque
- Cadastro de produtos.
- Cálculo automático do preço de venda com base no custo e percentual de lucro.
- Atualização do preço quando o custo ou percentual de lucro é alterado.
- Controle automático do estoque conforme a inclusão, alteração ou exclusão de itens de venda.

### 4. Controle financeiro
- Atualização automática do valor total das vendas.
- Controle de compras realizadas a prazo.
- Bloqueio e liberação de clientes conforme o limite de crédito.
- Registro de pagamentos.
- Atualização automática do resumo financeiro diário.

### 5. Funções, procedures e triggers

Foram implementadas funções e procedures em PL/pgSQL para automatizar operações, incluindo:

- Cadastro de clientes, produtos, vendas e endereços.
- Consulta da quantidade de cidades por estado.
- Consulta de cidade e quantidade de CEPs.
- Cálculo de preços.
- Atualização de informações financeiras.

As triggers são utilizadas para executar automaticamente regras de negócio durante operações de inserção, atualização e exclusão de registros.

### 6. Views
Foi criada a view `dados_venda`, que reúne informações das vendas, clientes e endereços de entrega, incluindo cidade, estado, bairro e logradouro.

### 7. Controle de permissões
O projeto também implementa controle de acesso por meio de usuários e permissões:

- `vendas`: acesso às tabelas relacionadas a clientes, vendas e produtos, sem permissão de exclusão.
- `localizacao`: acesso às tabelas de localização, sem permissão de exclusão.
- `user_admin`: acesso às tabelas do banco de dados, incluindo permissão de exclusão.

## Conceitos aplicados

- Modelagem e relacionamento de bancos de dados.
- Chaves primárias e estrangeiras.
- Integridade referencial.
- Consultas SQL.
- Funções e procedures.
- Triggers.
- Views.
- Sequences.
- Controle de acesso e permissões.
- Automação de regras de negócio.

## Objetivo de aprendizagem

O projeto teve como objetivo aprofundar os conhecimentos em banco de dados relacionais, SQL e PL/pgSQL, aplicando conceitos de automação, integridade dos dados, controle de estoque e gerenciamento financeiro em um cenário prático.
