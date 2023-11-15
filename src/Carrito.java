import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private List<Producto> productos;
    private Usuario usuario;

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
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    public void vaciarCarrito() {
        productos.clear();
    }

    public void realizarPedido() {
        if (!productos.isEmpty()) {
            System.out.println("Pedido realizado por " + usuario.getUsername() +
                    ". Total: $" + calcularTotal());
            vaciarCarrito();
        } else {
            System.out.println("Error: El carrito está vacío.");
        }
    }
}


