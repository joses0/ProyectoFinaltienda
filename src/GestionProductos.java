import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestionProductos {
    private NodoProducto inicio;
    private NodoProducto fin;
    private int tamaño;
    private List<Producto> carrito;
    private Usuario usuarioAutenticado;

    public GestionProductos() {
        inicio = null;
        fin = null;
        tamaño = 0;
        carrito = new ArrayList<>();
        usuarioAutenticado = null;
    }

    public void agregarProducto(Producto producto) {
        NodoProducto nuevoNodo = new NodoProducto(producto);
        if (inicio == null) {
            inicio = nuevoNodo;
            fin = nuevoNodo;
            fin.siguiente = inicio;
        } else {
            fin.siguiente = nuevoNodo;
            nuevoNodo.siguiente = inicio;
            fin = nuevoNodo;
        }
        tamaño++;
    }

    // Resto de los métodos de la clase GestionProductos...
    // (buscarPorId, buscarPorNombre, listarPorMes, calcularPrecioPromedio, etc.)

    public void agregarAlCarrito(Producto producto) {
        carrito.add(producto);
    }

    public List<Producto> verCarrito() {
        return carrito;
    }

    public void realizarCompra() {
        // Lógica para realizar la compra, por ejemplo, limpiar el carrito.
        carrito.clear();
    }

    

    


    public Producto buscarPorId(String id) {
        if (inicio == null) {
            return null;
        }
        NodoProducto actual = inicio;
        do {
            if (actual.getProducto().getIdProducto().equals(id)) {
                return actual.getProducto();
            }
            actual = actual.siguiente;
        } while (actual != inicio);
        return null;
    }

    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        if (inicio != null) {
            NodoProducto actual = inicio;
            do {
                if (actual.getProducto().getNomProducto().equalsIgnoreCase(nombre)) {
                    resultados.add(actual.getProducto());
                }
                actual = actual.siguiente;
            } while (actual != inicio);
        }
        return resultados;
    }

    public List<Producto> listarPorMes(String fecha, boolean esLote) {
        List<Producto> resultados = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaHoy;
        try {
            fechaHoy = sdf.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            return resultados;
        }

        if (inicio != null) {
            NodoProducto actual = inicio;
            do {
                Date fechaProducto = esLote ? parseFecha(actual.getProducto().getFechaLote()) : parseFecha(actual.getProducto().getFechaVence());
                if (fechaProducto != null && fechaProducto.getMonth() == fechaHoy.getMonth() && fechaProducto.getYear() == fechaHoy.getYear()) {
                    resultados.add(actual.getProducto());
                }
                actual = actual.siguiente;
            } while (actual != inicio);
        }
        return resultados;
    }

    public double calcularPrecioPromedio() {
        if (inicio == null) {
            return 0.0;
        }

        double total = 0;
        NodoProducto actual = inicio;
        do {
            total += actual.getProducto().getPrecioU();
            actual = actual.siguiente;
        } while (actual != inicio);

        return total / tamaño;
    }

    public List<Producto> encontrarMayoresMenoresAlPromedio(boolean mayores) {
        List<Producto> resultados = new ArrayList<>();
        double promedio = calcularPrecioPromedio();
        if (inicio != null) {
            NodoProducto actual = inicio;
            do {
                double precio = actual.getProducto().getPrecioU();
                if ((mayores && precio > promedio) || (!mayores && precio < promedio)) {
                    resultados.add(actual.getProducto());
                }
                actual = actual.siguiente;
            } while (actual != inicio);
        }
        return resultados;
    }

    public boolean existeId(String id) {
        if (inicio != null) {
            NodoProducto actual = inicio;
            do {
                if (actual.getProducto().getIdProducto().equals(id)) {
                    return true;
                }
                actual = actual.siguiente;
            } while (actual != inicio);
        }
        return false;
    }

    public boolean existeNombre(String nombre) {
        if (inicio != null) {
            NodoProducto actual = inicio;
            do {
                if (actual.getProducto().getNomProducto().equals(nombre)) {
                    return true;
                }
                actual = actual.siguiente;
            } while (actual != inicio);
        }
        return false;
    }

    private Date parseFecha(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
public double calcularTotal() {
        // Lógica para calcular el total de la compra.
        double total = 0.0;
        for (Producto producto : carrito) {
            total += producto.getPrecioU();
        }
        return total;
    }
public boolean iniciarSesion(String nombreUsuario, String contraseña) {
        // Lógica para verificar las credenciales y autenticar al usuario.
        // Aquí, por simplicidad, se asume que hay un único usuario con credenciales predefinidas.
        if ("usuario".equals(nombreUsuario) && "contraseña".equals(contraseña)) {
            usuarioAutenticado = new Usuario(nombreUsuario, contraseña);
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        // Lógica para cerrar la sesión.
        usuarioAutenticado = null;
    }

    public boolean usuarioAutenticado() {
        return usuarioAutenticado != null;
    }

    

    private double calcularTotalCarrito() {
        double total = 0;
        for (Producto producto : carrito) {
            total += producto.getPrecioU();
        }
        return total;
    }
    
}
