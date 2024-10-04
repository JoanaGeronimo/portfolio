/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "itemvenda")
@IdClass(ItemVendaID.class)
@NamedQueries({
    @NamedQuery(
        name = "ItemVenda.vendaEspecifica",
        query = "SELECT i, p.nome FROM ItemVenda i JOIN i.produto p WHERE i.venda.idvenda = :idvenda"
    )
})
public class ItemVenda implements Serializable{
    
    @Id
    @Column(name="iditem")
    private int iditem;
    
    @Id
    @ManyToOne
    @JoinColumn(name="idvenda", referencedColumnName = "idvenda")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name="idproduto",referencedColumnName = "idproduto")
    private Produto produto;
    
    @Column(name="qtde")
    private int qtde;
    
    @Column(name="preco")
    private double preco;

    public ItemVenda() {
    }

    public int getIditem() {
        return iditem;
    }

    public void setIditem(int iditem) {
        this.iditem = iditem;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.iditem;
        hash = 79 * hash + Objects.hashCode(this.venda);
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
        final ItemVenda other = (ItemVenda) obj;
        if (this.iditem != other.iditem) {
            return false;
        }
        return Objects.equals(this.venda, other.venda);
    }
 
}
