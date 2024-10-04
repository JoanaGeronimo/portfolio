/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="venda")

@NamedQueries({
    @NamedQuery(name = "Venda.todos", query = "SELECT v FROM Venda v"), 
    @NamedQuery(name = "Venda.porDatas", query = "SELECT v FROM Venda v WHERE v.datavenda BETWEEN :dataInicio AND :dataFim"),
    
    
})
public class Venda implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idvenda")
    private int idvenda;
    
    @Temporal(TemporalType.DATE)
    @Column(name="datavenda")
    private Date datavenda;
    
    @Column(name="valortotal")
    private Double valorTotal;
    
    @JoinColumn(name="idcliente",referencedColumnName = "idcliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", orphanRemoval = true, cascade = CascadeType.ALL )
    List <ItemVenda> listaItens = new ArrayList<>();

    public Venda() {
    }

    public int getIdvenda() {
        return idvenda;
    }

    public void setIdvenda(int idvenda) {
        this.idvenda = idvenda;
    }

    public Date getDatavenda() {
        return datavenda;
    }

    public void setDatavenda(Date datavenda) {
        this.datavenda = datavenda;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemVenda> getListaItens() {
        return listaItens;
    }

    public void setListaItens(List<ItemVenda> listaItens) {
        this.listaItens = listaItens;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.idvenda;
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
        final Venda other = (Venda) obj;
        return this.idvenda == other.idvenda;
    }
  
    
}
