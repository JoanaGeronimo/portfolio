/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="pecas")
@NamedQueries({
    @NamedQuery(name = "Pecas.todos", query = "SELECT p FROM Pecas p"),     
})
public class Pecas implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idpecas")
    private Integer idpecas;

    @Column(name="descricao")
    private String descricao;
        
    @Column(name="preco")
    private Double preco;
            
    @Column(name="qtdeestoque")
    private Integer qtdeestoque;

    
    public Pecas() {
    }
    
    public Integer getIdpecas() {
        return idpecas;
    }

    public void setIdpecas(Integer idpecas) {
        this.idpecas = idpecas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQtdeestoque() {
        return qtdeestoque;
    }

    public void setQtdeestoque(Integer qtdeestoque) {
        this.qtdeestoque = qtdeestoque;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.idpecas);
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
        final Pecas other = (Pecas) obj;
        return Objects.equals(this.idpecas, other.idpecas);
    }
   
}
