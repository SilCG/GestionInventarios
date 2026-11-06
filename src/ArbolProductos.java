import java.util.ArrayList;
import java.util.List;

public class ArbolProductos {

    private NodoProducto raiz;

    private static class NodoProducto {
        private final Producto producto;
        private NodoProducto izquierdo;
        private NodoProducto derecho;

        private NodoProducto(Producto producto) {
            this.producto = producto;
        }
    }

    public boolean insertar(Producto producto) {
        if (producto == null || producto.getNombre() == null
                || producto.getNombre().trim().isEmpty()) {
            return false;
        }

        if (raiz == null) {
            raiz = new NodoProducto(copiarProducto(producto));
            return true;
        }

        NodoProducto actual = raiz;
        while (true) {
            int comparacion = producto.getNombre().compareTo(actual.producto.getNombre());
            if (comparacion == 0) {
                return false;
            }

            if (comparacion < 0) {
                if (actual.izquierdo == null) {
                    actual.izquierdo = new NodoProducto(copiarProducto(producto));
                    return true;
                }
                actual = actual.izquierdo;
            } else {
                if (actual.derecho == null) {
                    actual.derecho = new NodoProducto(copiarProducto(producto));
                    return true;
                }
                actual = actual.derecho;
            }
        }
    }

    public Producto buscar(String nombre) {
        if (nombre == null) {
            return null;
        }

        NodoProducto actual = raiz;
        while (actual != null) {
            int comparacion = nombre.compareTo(actual.producto.getNombre());
            if (comparacion == 0) {
                return copiarProducto(actual.producto);
            }
            actual = comparacion < 0 ? actual.izquierdo : actual.derecho;
        }
        return null;
    }

    public List<Producto> listarDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        agregarDisponibles(raiz, disponibles);
        return disponibles;
    }

    private void agregarDisponibles(NodoProducto nodo, List<Producto> disponibles) {
        if (nodo == null) {
            return;
        }

        agregarDisponibles(nodo.izquierdo, disponibles);
        if (nodo.producto.getCantidad() > 0) {
            disponibles.add(copiarProducto(nodo.producto));
        }
        agregarDisponibles(nodo.derecho, disponibles);
    }

    private Producto copiarProducto(Producto producto) {
        Producto copia = new Producto(producto.getNombre(), producto.getPrecio(),
                producto.getCategoria(), producto.getFechaVencimiento(), producto.getCantidad());
        copia.getListaImagenes().addAll(producto.getListaImagenes());
        return copia;
    }
}
