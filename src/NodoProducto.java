public class NodoProducto {
    private Producto producto;
    NodoProducto siguiente;
    NodoProducto siguienente;

    public NodoProducto(Producto producto) {
        this.producto = producto;
        this.siguiente = null;
    }

    public Producto getProducto() {
        return producto;
    }

    public NodoProducto getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoProducto siguiente) {
        this.siguiente = siguiente;
    }
}




