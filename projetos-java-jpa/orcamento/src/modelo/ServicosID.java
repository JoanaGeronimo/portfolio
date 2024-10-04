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
public class ServicosID implements Serializable{
    
    private int idservico;
    private int orcamento;

    public int getIdservico() {
        return idservico;
    }

    public void setIdservico(int idservico) {
        this.idservico = idservico;
    }

    public int getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(int orcamento) {
        this.orcamento = orcamento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.idservico;
        hash = 37 * hash + this.orcamento;
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
        final ServicosID other = (ServicosID) obj;
        if (this.idservico != other.idservico) {
            return false;
        }
        return this.orcamento == other.orcamento;
    }
   
}
