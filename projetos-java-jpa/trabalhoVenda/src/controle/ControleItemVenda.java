/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.Cliente;
import modelo.ItemVenda;
import util.GerenciadorConexao;

/**
 *
 * @author Aluno
 */
public class ControleItemVenda {

    public void remover(Cliente cliente) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        Cliente clienteExcluir = gerente.find(Cliente.class, cliente.getIdcliente());
        gerente.getTransaction().begin();
        gerente.remove(clienteExcluir);
        gerente.getTransaction().commit();
        gerente.close();
    }

    public void adicionar(ItemVenda itemvenda) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        gerente.getTransaction().begin();
        gerente.persist(itemvenda);
        gerente.getTransaction().commit();
        gerente.close();

    }

    public List<Object[]> procurar(int idvenda) {

        EntityManager gerente = GerenciadorConexao.getGerente();

        List<Object[]> resultados = gerente.createNamedQuery("ItemVenda.vendaEspecifica")
                .setParameter("idvenda", idvenda)
                .getResultList();

        return resultados;
    }

}
