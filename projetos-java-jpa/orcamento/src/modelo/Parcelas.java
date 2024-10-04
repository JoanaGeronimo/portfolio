/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "parcelas")
@IdClass(ParcelasID.class)
public class Parcelas implements Serializable{
    
    @Id
    @Column(name="idparcelas")
    private Integer idparcelas;
    
    @Id
    @ManyToOne
    @JoinColumn(name="idorcamento", referencedColumnName = "idorcamento")
    private Orcamento orcamento;
    
    @Temporal(TemporalType.DATE)
    @Column(name="vencimento")
    private Date vencimento;
    
    @Column(name="valor")
    private Double valor;
    
    @Temporal(TemporalType.DATE)
    @Column(name="datapagamento")
    private Date datapagamento;
    
    @Column(name="descricao")
    private String descricao;

    public Parcelas() {
        
    }
    
    public Integer getIdparcelas() {
        return idparcelas;
    }

    public void setIdparcelas(Integer idparcelas) {
        this.idparcelas = idparcelas;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDatapagamento() {
        return datapagamento;
    }

    public void setDatapagamento(Date datapagamento) {
        this.datapagamento = datapagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.idparcelas);
        hash = 29 * hash + Objects.hashCode(this.orcamento);
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
        final Parcelas other = (Parcelas) obj;
        if (!Objects.equals(this.idparcelas, other.idparcelas)) {
            return false;
        }
        return Objects.equals(this.orcamento, other.orcamento);
    }
    
    
    
    
    
}
