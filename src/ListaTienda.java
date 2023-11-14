// ListaTienda.java
import java.util.ArrayList;
import java.util.List;

public class ListaTienda {
    private Nodo cabeza;

    public ListaTienda() {
        this.cabeza = null;
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
                    ", Precio: " + actual.getProducto().getPrecio());
            actual = actual.getSiguiente();
        }
        return listaProductos;
    }
}

