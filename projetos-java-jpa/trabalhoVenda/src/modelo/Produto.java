/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="produto")

@NamedQueries({
    @NamedQuery(name = "Produto.todos", query = "SELECT p FROM Produto p"),
    @NamedQuery(name = "Produto.especifico",
                query = "SELECT p FROM Produto p "
                        + "WHERE p.nome =:produto"),
    
})
public class Produto implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idproduto")
    private int idproduto;
    
    @Column(name="nome")
    private String nome;
    
    @Column(name="preco")
    private double preco;
    
    @Column(name="qtde_estoque")
    private double qtde_estoque;

    public Produto() {
    }

    public int getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(int idproduto) {
        this.idproduto = idproduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getQtde_estoque() {
        return qtde_estoque;
    }

    public void setQtde_estoque(double qtde_estoque) {
        this.qtde_estoque = qtde_estoque;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.idproduto;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        return this.idproduto == other.idproduto;
    }
    
    
    
}
