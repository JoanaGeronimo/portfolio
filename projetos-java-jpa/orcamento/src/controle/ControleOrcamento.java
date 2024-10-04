/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import modelo.Orcamento;
import modelo.Parcelas;
import modelo.Servicos;
import modelo.ServicosID;
import util.GerenciadorConexao;

/**
 *
 * @author Aluno
 */
public class ControleOrcamento {

    public List<Orcamento> getTodosOrcamento() {

        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Orcamento> consulta = gerente.createNamedQuery("Orcamento.todos", Orcamento.class);

        return consulta.getResultList();

    }

    public void remover(Orcamento orcamento) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        Orcamento orcamentoExcluir = gerente.find(Orcamento.class, orcamento.getIdorcamento());
        gerente.getTransaction().begin();
        gerente.remove(orcamentoExcluir);
        gerente.getTransaction().commit();
        gerente.close();
    }

    public void adicionar(Orcamento orcamento) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        gerente.getTransaction().begin();
        gerente.persist(orcamento);
        gerente.getTransaction().commit();
        gerente.close();

    }

    public void alterarParcela(Parcelas parcelas) {
        EntityManager gerente = GerenciadorConexao.getGerente();

        try {
            gerente.getTransaction().begin();

            gerente.merge(parcelas);

            gerente.getTransaction().commit();
        } catch (Exception e) {

            if (gerente.getTransaction().isActive()) {
                gerente.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {

            if (gerente.isOpen()) {
                gerente.close();
            }
        }

    }

    public void alterar(Orcamento orcamento) {
        // crio um gerente de entidades para fazer a alteração no
        // banco de dados
        EntityManager gerente = GerenciadorConexao.getGerente();

        // inicio uma transação com o banco de dados
        gerente.getTransaction().begin();

        // alterar a categoria no banco de dados
        gerente.merge(orcamento);

        // finalizar a transação
        gerente.getTransaction().commit();

        // finalizo a conexao
        gerente.close();

    }

}
