import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private Usuario usuario;
    private List<Producto> productos;

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Producto producto : productos) {
            total += producto.getPrecio();
        }
        return total;
    }

    public void realizarPedido() {
        // Lógica para realizar el pedido (puedes definir tu propia implementación)
    }
}
