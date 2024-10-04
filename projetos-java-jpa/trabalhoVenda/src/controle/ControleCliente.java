/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.Cliente;
import util.GerenciadorConexao;

/**
 *
 * @author Aluno
 */
public class ControleCliente {

    public List<Cliente> getTodosClientes() {

        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Cliente> consulta = gerente.createNamedQuery("Cliente.todos", Cliente.class);

        return consulta.getResultList();

    }

    public void remover(Cliente cliente) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        Cliente clienteExcluir = gerente.find(Cliente.class, cliente.getIdcliente());
        gerente.getTransaction().begin();
        gerente.remove(clienteExcluir);
        gerente.getTransaction().commit();
        gerente.close();
    }

    public void adicionar(Cliente cliente) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        gerente.getTransaction().begin();
        gerente.persist(cliente);
        gerente.getTransaction().commit();
        gerente.close();

    }

    public void alterar(Cliente cliente) {
        // crio um gerente de entidades para fazer a alteração no
        // banco de dados
        EntityManager gerente = GerenciadorConexao.getGerente();

        // inicio uma transação com o banco de dados
        gerente.getTransaction().begin();

        // alterar a categoria no banco de dados
        gerente.merge(cliente);

        // finalizar a transação
        gerente.getTransaction().commit();

        // finalizo a conexao
        gerente.close();

    }

    public List<Cliente> obterCliente(String nome) {
        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Cliente> query = gerente.createNamedQuery("Cliente.especifico", Cliente.class);
        query.setParameter("nome", nome);

        return query.getResultList();
    }

    public void atualizarTotalComprado(int idCliente, double valorCompra) {
        EntityManager gerente = null;
        try {
            gerente = GerenciadorConexao.getGerente(); // Obter o EntityManager
            gerente.getTransaction().begin(); // Iniciar a transação

            Cliente cliente = gerente.find(Cliente.class, idCliente);
            if (cliente != null) {
                cliente.setTotalComprado(cliente.getTotalComprado() + valorCompra);
                gerente.merge(cliente); // Atualizar o cliente
                gerente.getTransaction().commit(); // Confirmar a transação
            } else {
                System.out.println("Cliente não encontrado!");
            }
        } catch (Exception e) {
            if (gerente != null && gerente.getTransaction().isActive()) {
                gerente.getTransaction().rollback(); // Reverter a transação em caso de erro
            }
            e.printStackTrace(); // Exibir detalhes do erro
        } finally {
            if (gerente != null) {
                gerente.close(); // Fechar o EntityManager
            }
        }
    }

}
