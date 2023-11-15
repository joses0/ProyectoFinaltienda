// Producto.java
public class Producto {
    private String nombre;
    private double precio;
    private String descripcion;
    private String categoria;  // Nueva propiedad para la categoría

    public Producto(String nombre, double precio, String descripcion, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    // Otros getters y setters según sea necesario
}

