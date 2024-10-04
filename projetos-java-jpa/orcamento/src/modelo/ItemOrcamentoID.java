/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author Aluno
 */
public class ItemOrcamentoID implements Serializable {

    private int iditem;
    private int orcamento;

    public int getIditem() {
        return iditem;
    }

    public void setIditem(int iditem) {
        this.iditem = iditem;
    }

    
    public int getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(int orcamento) {
        this.orcamento = orcamento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.iditem;
        hash = 89 * hash + this.orcamento;
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
        final ItemOrcamentoID other = (ItemOrcamentoID) obj;
        if (this.iditem != other.iditem) {
            return false;
        }
        return this.orcamento == other.orcamento;
    }

    

}
