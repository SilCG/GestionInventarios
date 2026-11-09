import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private final Tienda tienda;
    private final Scanner entrada;

    public Main(Scanner entrada) {
        this.entrada = Objects.requireNonNull(entrada, "Se requiere una entrada de datos");
        this.tienda = new Tienda("Tienda en línea");
    }

    // Rutina principal: crea la entrada de datos y lanza el menú
    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in, "UTF-8")) {
            new Main(entrada).menu();
        }
    }

    // Menú de consola que controla todo el flujo del programa
    public void menu() {
        try {
            int opcion;
            do {
                mostrarMenu();
                opcion = leerOpcion();
                switch (opcion) {
                    case 1:
                        agregarProducto();
                        break;
                    case 2:
                        ingresarCliente();
                        break;
                    case 3:
                        atenderSiguiente();
                        break;
                    case 4:
                        System.out.println("\n--- Inventario disponible ---");
                        tienda.mostrarInventario();
                        break;
                    case 5:
                        tienda.mostrarCola();
                        break;
                    case 0:
                        System.out.println("Gracias por utilizar el sistema. Hasta luego.");
                        break;
                    default:
                        System.out.println("Opción inválida. Seleccione un número entre 0 y 5.");
                }
            } while (opcion != 0);
        } catch (NoSuchElementException e) {
            // Si se cierra la entrada, el programa termina sin guardar datos incompletos.
            System.out.println("\nFin de la entrada. El programa ha terminado.");
        }
    }

    private void mostrarMenu() {
        System.out.println("\n===== " + tienda.getNombre().toUpperCase() + " =====");
        System.out.println("1. Agregar producto al inventario");
        System.out.println("2. Ingresar cliente a la cola");
        System.out.println("3. Atender al siguiente cliente");
        System.out.println("4. Ver inventario disponible");
        System.out.println("5. Ver cola de clientes");
        System.out.println("0. Salir");
    }

    private int leerOpcion() {
        while (true) {
            try {
                return Integer.parseInt(leerLinea("Seleccione una opción: "));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero para elegir una opción.");
            }
        }
    }

    // Opción 1: inserta un producto en el inventario (o suma stock si ya existe)
    private void agregarProducto() {
        System.out.println("\n--- Agregar producto al inventario ---");
        String nombre = leerTexto("Nombre: ");

        if (tienda.existeProducto(nombre)) {
            System.out.println("Ya existe un producto con ese nombre en el inventario.");
            if (confirmar("¿Desea sumar unidades a su stock? (s/n): ")) {
                int unidades = leerCantidad("Unidades a sumar: ");
                if (tienda.reabastecer(nombre, unidades)) {
                    System.out.println("Stock actualizado correctamente.");
                } else {
                    System.out.println("No se pudo actualizar el stock: supera el máximo permitido.");
                }
            }
            return;
        }

        double precio = leerPrecio();
        String categoria = leerTexto("Categoría: ");
        LocalDate vencimiento = leerFecha();
        int cantidad = leerCantidad("Cantidad inicial: ");
        if (!costoValido(precio, cantidad)) {
            return;
        }

        if (tienda.agregarProducto(new Producto(nombre, precio, categoria, vencimiento, cantidad))) {
            System.out.println("Producto agregado al inventario correctamente.");
        } else {
            System.out.println("No se pudo agregar el producto.");
        }
    }

    // Opción 2: crea un cliente, llena su carrito desde el inventario y lo inserta en la cola
    private void ingresarCliente() {
        System.out.println("\n--- Ingresar cliente a la cola ---");
        if (!tienda.hayProductosDisponibles()) {
            System.out.println("No hay productos disponibles. Agregue productos al inventario primero.");
            return;
        }

        String nombre = leerTexto("Nombre del cliente: ");
        int prioridad = leerPrioridad();
        Cliente cliente = new Cliente(nombre, prioridad);

        if (!llenarCarrito(cliente)) {
            System.out.println("Ingreso de cliente cancelado.");
            return;
        }
        tienda.encolarCliente(cliente);
        System.out.println("Cliente ingresado a la cola correctamente.");
    }

    // Llena el carrito con productos del inventario; devuelve false si se canceló con el carrito vacío
    private boolean llenarCarrito(Cliente cliente) {
        while (true) {
            if (!tienda.hayProductosDisponibles()) {
                System.out.println("\nYa no quedan productos disponibles en el inventario.");
                return !cliente.carritoVacio();
            }

            System.out.println("\nProductos disponibles:");
            tienda.mostrarInventario();

            String nombreProducto = leerLinea("\nNombre del producto a comprar (Enter para terminar): ");
            if (nombreProducto.isEmpty()) {
                if (!cliente.carritoVacio()) {
                    return true;
                }
                if (confirmar("El carrito está vacío. ¿Cancelar el ingreso del cliente? (s/n): ")) {
                    return false;
                }
                continue;
            }

            int cantidad = leerCantidad("Cantidad: ");
            int resultado = tienda.agregarAlCarrito(cliente, nombreProducto, cantidad);
            switch (resultado) {
                case Tienda.COMPRA_OK:
                    System.out.println("Producto agregado al carrito.");
                    break;
                case Tienda.PRODUCTO_NO_EXISTE:
                    System.out.println("Ese producto no existe en el inventario (revise mayúsculas y minúsculas).");
                    break;
                case Tienda.STOCK_INSUFICIENTE:
                    System.out.println("No hay stock suficiente de ese producto.");
                    break;
                default:
                    System.out.println("La cantidad indicada no es válida.");
            }
        }
    }

    // Opción 3: atiende al cliente de mayor prioridad e imprime su factura
    private void atenderSiguiente() {
        System.out.println("\n--- Atender al siguiente cliente ---");
        tienda.atenderSiguiente();
    }

    private int leerPrioridad() {
        while (true) {
            String texto = leerLinea("Prioridad (1 = básico, 2 = afiliado, 3 = premium): ");
            try {
                int prioridad = Integer.parseInt(texto);
                if (Cliente.esPrioridadValida(prioridad)) {
                    return prioridad;
                }
            } catch (NumberFormatException e) {
                // Se vuelve a solicitar la prioridad.
            }
            System.out.println("Ingrese 1, 2 o 3.");
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje);
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este campo no puede quedar vacío.");
        }
    }

    private double leerPrecio() {
        while (true) {
            String texto = leerLinea("Precio en colones: ");
            // Se acepta punto o coma decimal, sin separadores de miles.
            if (texto.matches("[+]?(?:\\d+(?:[.,]\\d*)?|[.,]\\d+)")) {
                try {
                    double precio = Double.parseDouble(texto.replace(',', '.'));
                    if (Double.isFinite(precio) && precio > 0) {
                        return precio;
                    }
                } catch (NumberFormatException e) {
                    // Se vuelve a solicitar el precio si el número no se puede convertir.
                }
            }
            System.out.println("Ingrese un precio válido mayor que 0, sin separadores de miles.");
        }
    }

    private int leerCantidad(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje);
            try {
                int cantidad = Integer.parseInt(texto);
                if (cantidad > 0) {
                    return cantidad;
                }
            } catch (NumberFormatException e) {
                // La cantidad debe ser un entero dentro del rango de int.
            }
            System.out.println("Ingrese una cantidad entera mayor que 0.");
        }
    }

    private LocalDate leerFecha() {
        while (true) {
            String texto = leerLinea("Vencimiento (AAAA-MM-DD; Enter si no vence): ");
            if (texto.isEmpty()) {
                return null;
            }
            if (texto.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    return LocalDate.parse(texto);
                } catch (DateTimeParseException e) {
                    // LocalDate comprueba días, meses y años bisiestos.
                }
            }
            System.out.println("Ingrese una fecha real con formato AAAA-MM-DD, por ejemplo 2026-12-31.");
        }
    }

    private boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leerLinea(mensaje);
            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Responda s para confirmar o n para cancelar.");
        }
    }

    private boolean costoValido(double precio, int cantidad) {
        if (!Double.isFinite(precio * cantidad)) {
            System.out.println("El costo supera el rango permitido. No se guardó el producto.");
            return false;
        }
        return true;
    }

    private String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }
}
