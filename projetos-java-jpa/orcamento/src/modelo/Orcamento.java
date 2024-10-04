package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "orcamento")
@NamedQueries({
    @NamedQuery(name = "Orcamento.todos", query = "SELECT o FROM Orcamento o"),     
})
public class Orcamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idorcamento")
    private Integer idorcamento;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataorcamento")
    private Date dataorcamento;

    @Column(name = "nome")
    private String nome;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "telefone")
    private String telefone;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataaprovacao")
    private Date dataaprovacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "datavalidade")
    private Date datavalidade;

    @Column(name = "valortotal")
    private Double valortotal;

    @OneToMany(mappedBy = "orcamento", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Parcelas> listaParcelas = new ArrayList<>();

    @OneToMany(mappedBy = "orcamento", orphanRemoval = true, cascade = CascadeType.ALL)
    List<ItemOrcamento> listaItens = new ArrayList<>();

    @OneToMany(mappedBy = "orcamento", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Servicos> listaServicos = new ArrayList<>();
    
    public Orcamento() {

    }

    public Integer getIdorcamento() {
        return idorcamento;
    }

    public void setIdorcamento(Integer idorcamento) {
        this.idorcamento = idorcamento;
    }

    public Date getDataorcamento() {
        return dataorcamento;
    }

    public void setDataorcamento(Date dataorcamento) {
        this.dataorcamento = dataorcamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataaprovacao() {
        return dataaprovacao;
    }

    public void setDataaprovacao(Date dataaprovacao) {
        this.dataaprovacao = dataaprovacao;
    }

    public Date getDatavalidade() {
        return datavalidade;
    }

    public void setDatavalidade(Date datavalidade) {
        this.datavalidade = datavalidade;
    }

    public Double getValortotal() {
        return valortotal;
    }

    public void setValortotal(Double valortotal) {
        this.valortotal = valortotal;
    }

    public List<Parcelas> getListaParcelas() {
        return listaParcelas;
    }

    public void setListaParcelas(List<Parcelas> listaParcelas) {
        this.listaParcelas = listaParcelas;
    }

    public List<ItemOrcamento> getListaItens() {
        return listaItens;
    }

    public void setListaItens(List<ItemOrcamento> listaItens) {
        this.listaItens = listaItens;
    }

    public List<Servicos> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<Servicos> listaServicos) {
        this.listaServicos = listaServicos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.idorcamento);
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
        final Orcamento other = (Orcamento) obj;
        return Objects.equals(this.idorcamento, other.idorcamento);
    }

}
