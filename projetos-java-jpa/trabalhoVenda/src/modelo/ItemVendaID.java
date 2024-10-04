package modelo;

import java.io.Serializable;
import java.util.Objects;

public class ItemVendaID implements Serializable {
    
    private int iditem;
    private int venda;

    public int getIditem() {
        return iditem;
    }

    public void setIditem(int iditem) {
        this.iditem = iditem;
    }

    public int getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iditem, venda);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ItemVendaID other = (ItemVendaID) obj;
        return iditem == other.iditem && venda == other.venda;
    }
}
