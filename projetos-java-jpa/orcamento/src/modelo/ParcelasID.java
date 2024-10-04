/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;


public class ParcelasID implements Serializable{
    
    private int idparcelas;
    private int orcamento;

    public int getIdparcelas() {
        return idparcelas;
    }

    public void setIdparcelas(int idparcelas) {
        this.idparcelas = idparcelas;
    }

    public int getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(int orcamento) {
        this.orcamento = orcamento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.idparcelas;
        hash = 29 * hash + this.orcamento;
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
        final ParcelasID other = (ParcelasID) obj;
        if (this.idparcelas != other.idparcelas) {
            return false;
        }
        return this.orcamento == other.orcamento;
    }

    
   
}
