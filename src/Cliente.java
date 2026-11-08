public class Cliente {

    // Constantes
    public static final int PRIORIDAD_BASICO = 1;
    public static final int PRIORIDAD_AFILIADO = 2;
    public static final int PRIORIDAD_PREMIUM = 3;

    // Atributos
    private String nombre;
    private int prioridad;
    private final ListaProductos carrito; // carrito personal del cliente
    private int cantidadProductosCarrito; // cuántos productos distintos tiene el carrito

    // Constructor
    public Cliente(String nombre, int prioridad) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede quedar vacío.");
        }
        if (!esPrioridadValida(prioridad)) {
            throw new IllegalArgumentException("La prioridad debe ser 1 (básico), 2 (afiliado) o 3 (premium).");
        }
        this.nombre = nombre.trim();
        this.prioridad = prioridad;
        this.carrito = new ListaProductos(); // el carrito nace vacío
        this.cantidadProductosCarrito = 0;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public ListaProductos getCarrito() {
        return carrito;
    }

    public int getCantidadProductosCarrito() {
        return cantidadProductosCarrito;
    }

    // Setters
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede quedar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public void setPrioridad(int prioridad) {
        if (!esPrioridadValida(prioridad)) {
            throw new IllegalArgumentException("La prioridad debe ser 1 (básico), 2 (afiliado) o 3 (premium).");
        }
        this.prioridad = prioridad;
    }

    // Operaciones
    public static boolean esPrioridadValida(int prioridad) {
        return prioridad >= PRIORIDAD_BASICO && prioridad <= PRIORIDAD_PREMIUM;
    }

    // Devuelve el nombre del tipo de cliente según su prioridad.
    public static String nombrePrioridad(int prioridad) {
        switch (prioridad) {
            case PRIORIDAD_PREMIUM:
                return "Premium";
            case PRIORIDAD_AFILIADO:
                return "Afiliado";
            case PRIORIDAD_BASICO:
                return "Básico";
            default:
                return "Desconocido";
        }
    }

    // Tipo de este cliente en texto (Básico, Afiliado o Premium).
    public String getTipo() {
        return nombrePrioridad(prioridad);
    }

    public boolean agregarAlCarrito(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return false;
        }
        String nombreProducto = producto.getNombre();
        if (carrito.contiene(nombreProducto)) {
            // El producto ya está en el carrito: se acumula la cantidad
            Producto enCarrito = carrito.buscar(nombreProducto);
            long nuevaCantidad = (long) enCarrito.getCantidad() + cantidad;
            if (nuevaCantidad > Integer.MAX_VALUE) {
                return false; // evita desbordar el entero
            }
            enCarrito.setCantidad((int) nuevaCantidad);
        } else {
            carrito.insertarFin(nombreProducto, producto.getPrecio(), producto.getCategoria(),
                    producto.getFechaVencimiento(), cantidad);
            cantidadProductosCarrito++;
        }
        return true;
    }

    // Indica si el carrito todavía no tiene productos.
    public boolean carritoVacio() {
        return cantidadProductosCarrito == 0;
    }

    // toString()
    @Override
    public String toString() {
        return nombre + " (" + getTipo() + ", prioridad " + prioridad + ") - "
                + cantidadProductosCarrito + " producto(s) en el carrito";
    }
}
