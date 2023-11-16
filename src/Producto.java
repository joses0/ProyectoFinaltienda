 import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class Producto {
    private String idProducto;
    private String nomProducto;
    private String FechaLote;
    private String FechaVence;
    private double precioU;

    public Producto(String idProducto, String nomProducto, String FechaLote, String FechaVence, double precioU) {
        this.idProducto = idProducto;
        this.nomProducto = nomProducto;
        this.FechaLote = FechaLote;
        this.FechaVence = FechaVence;
        this.precioU = precioU;
    }

    // Getters y Setters
    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public String getFechaLote() {
        return FechaLote;
    }

    public void setFechaLote(String FechaLote) {
        this.FechaLote = FechaLote;
    }

    public String getFechaVence() {
        return FechaVence;
    }

    public void setFechaVence(String FechaVence) {
        this.FechaVence = FechaVence;
    }

    public double getPrecioU() {
        return precioU;
    }

    public void setPrecioU(double precioU) {
        this.precioU = precioU;
    }
    @Override
public String toString() {
    return "Producto{" +
            "id='" + idProducto + '\'' +
            ", nombre='" + nomProducto + '\'' +
            ", fechaLote='" + FechaLote + '\'' +
            ", fechaVence='" + FechaVence + '\'' +
            ", precioUnitario=" + precioU +
            '}';
}

    
}
