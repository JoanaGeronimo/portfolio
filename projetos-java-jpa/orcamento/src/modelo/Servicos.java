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
@Table(name = "servicos")
@IdClass(ServicosID.class)
public class Servicos implements Serializable{
    
    @Id
    @Column(name="idservico")
    private int idservico;
    
    @Id
    @ManyToOne
    @JoinColumn(name="idorcamento",referencedColumnName = "idorcamento")
    private Orcamento orcamento;

    @Column(name="servicorealizado")
    private String servicorealizado;
    
    @Column(name="preco")
    private Double preco;

    public Servicos() {
        
    }

    public int getIdservico() {
        return idservico;
    }

    public void setIdservico(int idservico) {
        this.idservico = idservico;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public String getServicorealizado() {
        return servicorealizado;
    }

    public void setServicorealizado(String servicorealizado) {
        this.servicorealizado = servicorealizado;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.idservico;
        hash = 89 * hash + Objects.hashCode(this.orcamento);
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
        final Servicos other = (Servicos) obj;
        if (this.idservico != other.idservico) {
            return false;
        }
        return Objects.equals(this.orcamento, other.orcamento);
    }
 
    
    
}
