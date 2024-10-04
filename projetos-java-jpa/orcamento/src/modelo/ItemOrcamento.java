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
import javax.persistence.Table;

@Entity
@Table(name = "itemorcamento")
@IdClass(ItemOrcamentoID.class)
public class ItemOrcamento implements Serializable{
    
    @Id
    @ManyToOne
    @JoinColumn(name="idorcamento",referencedColumnName = "idorcamento")
    private Orcamento orcamento;

    @Id
    @Column(name="iditem")
    private Integer iditem;

    @Column(name="qtde")
    private Integer qtde;
    
    @Column(name="preco")
    private Double preco;
    
    @ManyToOne
    @JoinColumn(name="idpecas",referencedColumnName = "idpecas")
    private Pecas pecas;

    public ItemOrcamento() {
        
    }

    public Integer getIditem() {
        return iditem;
    }

    public void setIditem(Integer iditem) {
        this.iditem = iditem;
    }
 
    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public Pecas getPecas() {
        return pecas;
    }

    public void setPecas(Pecas pecas) {
        this.pecas = pecas;
    }


    public Integer getQtde() {
        return qtde;
    }

    public void setQtde(Integer qtde) {
        this.qtde = qtde;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.orcamento);
        hash = 41 * hash + Objects.hashCode(this.iditem);
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
        final ItemOrcamento other = (ItemOrcamento) obj;
        if (!Objects.equals(this.orcamento, other.orcamento)) {
            return false;
        }
        return Objects.equals(this.iditem, other.iditem);
    }

    
}
