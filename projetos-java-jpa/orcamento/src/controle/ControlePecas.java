/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.ItemOrcamento;
import modelo.Pecas;
import util.GerenciadorConexao;

/**
 *
 * @author Aluno
 */
public class ControlePecas {

    public List<Pecas> getTodosPecas() {

        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Pecas> consulta = gerente.createNamedQuery("Pecas.todos", Pecas.class);

        return consulta.getResultList();

    }

    public void remover(Pecas pecas) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        gerente.getTransaction().begin();

        Pecas pecasExcluir = gerente.find(Pecas.class, pecas.getIdpecas());

        if (pecasExcluir != null) {
            TypedQuery<ItemOrcamento> query = gerente.createQuery(
                    "SELECT i FROM ItemOrcamento i WHERE i.pecas = :pecas", ItemOrcamento.class);
            query.setParameter("pecas", pecasExcluir);
            List<ItemOrcamento> itensOrcamento = query.getResultList();

            for (ItemOrcamento item : itensOrcamento) {
                gerente.remove(item);
            }

            gerente.remove(pecasExcluir);
        }

        gerente.getTransaction().commit();
        gerente.close();
    }

    public void adicionar(Pecas pecas) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        gerente.getTransaction().begin();
        gerente.persist(pecas);
        gerente.getTransaction().commit();
        gerente.close();

    }

    public void alterar(Pecas pecas) {
        // crio um gerente de entidades para fazer a alteração no
        // banco de dados
        EntityManager gerente = GerenciadorConexao.getGerente();

        // inicio uma transação com o banco de dados
        gerente.getTransaction().begin();

        // alterar a categoria no banco de dados
        gerente.merge(pecas);

        // finalizar a transação
        gerente.getTransaction().commit();

        // finalizo a conexao
        gerente.close();

    }

    public Pecas obterPecasPorId(int idpecas) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        Pecas pecas = gerente.find(Pecas.class, idpecas);
        gerente.close();
        return pecas;
    }

}
