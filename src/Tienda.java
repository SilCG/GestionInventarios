import java.util.List;

public class Tienda {

    // Resultados posibles al intentar poner un producto en el carrito
    public static final int COMPRA_OK = 0;
    public static final int PRODUCTO_NO_EXISTE = 1;
    public static final int STOCK_INSUFICIENTE = 2;
    public static final int CANTIDAD_INVALIDA = 3;

    // Atributos
    private final String nombre;
    private final ArbolProductos inventario; // inventario de productos (árbol binario de búsqueda)
    private final ColaClientes colaClientes; // clientes en espera (cola de prioridad)

    // Constructor
    public Tienda(String nombre) {
        this.nombre = nombre;
        this.inventario = new ArbolProductos();
        this.colaClientes = new ColaClientes();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    // Operaciones del inventario

    // Inserta un producto nuevo en el inventario; devuelve false si el nombre ya existe o es inválido
    public boolean agregarProducto(Producto producto) {
        return inventario.insertar(producto);
    }

    public boolean existeProducto(String nombreProducto) {
        return inventario.buscar(nombreProducto) != null;
    }

    // Suma unidades al stock de un producto que ya existe
    public boolean reabastecer(String nombreProducto, int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        return inventario.ajustarCantidad(nombreProducto, cantidad);
    }

    public List<Producto> obtenerDisponibles() {
        return inventario.listarDisponibles();
    }

    public boolean hayProductosDisponibles() {
        return !inventario.listarDisponibles().isEmpty();
    }

    // Muestra los productos con stock, ordenados por nombre (recorrido inorden del árbol)
    public void mostrarInventario() {
        List<Producto> disponibles = inventario.listarDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("\nNo hay productos disponibles en el inventario.");
            return;
        }
        System.out.println();
        System.out.printf("%-22s %-15s %12s %8s %-12s%n", "Producto", "Categoría", "Precio", "Stock", "Vence");
        System.out.println("-------------------------------------------------------------------------");
        for (Producto producto : disponibles) {
            String vencimiento = producto.getFechaVencimiento() == null
                    ? "No aplica" : producto.getFechaVencimiento().toString();
            System.out.printf("%-22s %-15s %12.2f %8d %-12s%n", producto.getNombre(),
                    producto.getCategoria(), producto.getPrecio(), producto.getCantidad(), vencimiento);
        }
    }

    // Operaciones de compra

    // Pasa un producto del inventario al carrito del cliente y descuenta el stock
    public int agregarAlCarrito(Cliente cliente, String nombreProducto, int cantidad) {
        if (cantidad <= 0) {
            return CANTIDAD_INVALIDA;
        }
        Producto enInventario = inventario.buscar(nombreProducto);
        if (enInventario == null) {
            return PRODUCTO_NO_EXISTE;
        }
        if (cantidad > enInventario.getCantidad()) {
            return STOCK_INSUFICIENTE;
        }
        if (!cliente.agregarAlCarrito(enInventario, cantidad)) {
            return CANTIDAD_INVALIDA;
        }
        inventario.ajustarCantidad(nombreProducto, -cantidad);
        return COMPRA_OK;
    }

    // Operaciones de la cola

    public void encolarCliente(Cliente cliente) {
        colaClientes.insertar(cliente);
    }

    public boolean hayClientesEnCola() {
        return !colaClientes.estaVacia();
    }

    public void mostrarCola() {
        colaClientes.mostrarCola();
    }

    // Atiende al cliente de mayor prioridad (el frente de la cola) e imprime su factura
    public Cliente atenderSiguiente() {
        Cliente cliente = colaClientes.remover(); // si la cola está vacía, remover() avisa y devuelve null
        if (cliente != null) {
            imprimirFactura(cliente);
        }
        return cliente;
    }

    // Imprime la factura del carrito del cliente, con subtotal por producto y total acumulado
    private void imprimirFactura(Cliente cliente) {
        System.out.println();
        System.out.println("=====================================================================");
        System.out.printf("FACTURA - %s%n", nombre);
        System.out.printf("Cliente: %s (%s)%n", cliente.getNombre(), cliente.getTipo());
        System.out.println("=====================================================================");
        System.out.printf("%-22s %8s %16s %16s%n", "Producto", "Cantidad", "Precio unitario", "Subtotal");
        System.out.println("---------------------------------------------------------------------");

        double total = 0; // total acumulado de la compra
        for (Producto producto : cliente.getCarrito().obtenerProductos()) {
            double subtotal = producto.calcularCostoTotal();
            System.out.printf("%-22s %8d %16.2f %16.2f%n", producto.getNombre(), producto.getCantidad(),
                    producto.getPrecio(), subtotal);
            total += subtotal;
        }

        System.out.println("---------------------------------------------------------------------");
        System.out.printf("%-48s %16.2f%n", "TOTAL A PAGAR (colones):", total);
        System.out.println("=====================================================================");
    }
}
