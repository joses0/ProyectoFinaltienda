// ListaTienda.java
import java.util.ArrayList;
import java.util.List;

public class ListaTienda {
    private Nodo cabeza;

    public ListaTienda() {
        this.cabeza = null;
        // Agregar algunos productos de ejemplo
        agregarProducto(new Producto("Camiseta", 19.99, "Camiseta de algodón", "Ropa"));
        agregarProducto(new Producto("Zapatos Deportivos", 49.99, "Zapatos para correr", "Zapatos"));
        agregarProducto(new Producto("Gorra", 14.99, "Gorra con logo", "Accesorios"));
    }

    public void agregarProducto(Producto producto) {
        Nodo nuevoNodo = new Nodo(producto);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
    }

    public List<String> obtenerListaProductos() {
        List<String> listaProductos = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            listaProductos.add("Producto: " + actual.getProducto().getNombre() +
                    ", Precio: " + actual.getProducto().getPrecio() +
                    ", Categoría: " + actual.getProducto().getCategoria());
            actual = actual.getSiguiente();
        }
        return listaProductos;
    }

    public Producto obtenerProductoPorNombre(String nombre) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.getProducto().getNombre().equals(nombre)) {
                return actual.getProducto();
            }
            actual = actual.getSiguiente();
        }
        return null; // Producto no encontrado
    }

    // Otros métodos según sea necesario
    public void eliminarProductos(List<Producto> productosAEliminar) {
        for (Producto producto : productosAEliminar) {
            Nodo actual = cabeza;
            Nodo anterior = null;

            while (actual != null) {
                if (actual.getProducto().equals(producto)) {
                    if (anterior == null) {
                        cabeza = actual.getSiguiente();
                    } else {
                        anterior.setSiguiente(actual.getSiguiente());
                    }
                    break;
                }

                anterior = actual;
                actual = actual.getSiguiente();
            }
        }
    }
}
