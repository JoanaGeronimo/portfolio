/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.Cliente;
import modelo.Venda;
import util.GerenciadorConexao;

/**
 *
 * @author Aluno
 */
public class ControleVenda {

    public List<Venda> getTodosVenda() {

        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Venda> consulta = gerente.createNamedQuery("Venda.todos", Venda.class);

        return consulta.getResultList();

    }

    public void remover(Venda venda) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        Venda vendaExcluir = gerente.find(Venda.class, venda.getIdvenda());
        gerente.getTransaction().begin();
        gerente.remove(vendaExcluir);
        gerente.getTransaction().commit();
        gerente.close();
    }

    public void adicionar(Venda venda) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        gerente.getTransaction().begin();
        gerente.persist(venda);
        gerente.getTransaction().commit();
        gerente.close();

    }

    public List<Venda> obterVendasEntreDatas(Date dataInicio, Date dataFim) {
        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Venda> query = gerente.createNamedQuery("Venda.porDatas", Venda.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        return query.getResultList();
    }

}
