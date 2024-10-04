/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.Produto;
import util.GerenciadorConexao;

/**
 *
 * @author Aluno
 */
public class ControleProduto {
    
    public List<Produto> getTodosProdutos() {

        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Produto> consulta = gerente.createNamedQuery("Produto.todos", Produto.class);

        return consulta.getResultList();

    }
     public void remover(Produto produto) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        Produto produtoExcluir = gerente.find(Produto.class, produto.getIdproduto());
        gerente.getTransaction().begin();
        gerente.remove(produtoExcluir);
        gerente.getTransaction().commit();
        gerente.close();
    }
     public void adicionar(Produto produto)
    {
        EntityManager gerente = GerenciadorConexao.getGerente();
        gerente.getTransaction().begin();
        gerente.persist(produto);
        gerente.getTransaction().commit();
        gerente.close();
        
    }
     public void alterar(Produto produto)
    {
        // crio um gerente de entidades para fazer a alteração no
        // banco de dados
        EntityManager gerente = GerenciadorConexao.getGerente();
        
        // inicio uma transação com o banco de dados
        gerente.getTransaction().begin();
        
        // alterar a categoria no banco de dados
        gerente.merge(produto);
        
        // finalizar a transação
        gerente.getTransaction().commit();
        
        // finalizo a conexao
        gerente.close();

    }
     
     public List<Produto> obterProduto(String nome) {
        EntityManager gerente = GerenciadorConexao.getGerente();

        TypedQuery<Produto> query = gerente.createNamedQuery("Produto.especifico", Produto.class);
        query.setParameter("produto", nome);
        
        return query.getResultList();
    }
     
     public Produto obterProdutoPorId(int idProduto) {
        EntityManager gerente = GerenciadorConexao.getGerente();
        Produto produto = gerente.find(Produto.class, idProduto);
        gerente.close();
        return produto;
    }
}
