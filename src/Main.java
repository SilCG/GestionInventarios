import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private final ListaProductos productos;
    private final Scanner entrada;

    public Main(Scanner entrada) {
        this.entrada = Objects.requireNonNull(entrada, "Se requiere una entrada de datos");
        this.productos = new ListaProductos();
    }

    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in, "UTF-8")) {
            new Main(entrada).menu();
        }
    }

    public void menu() {
        try {
            int opcion;
            do {
                mostrarMenu();
                opcion = leerOpcion();
                switch (opcion) {
                    case 1:
                        agregarProducto(true);
                        break;
                    case 2:
                        agregarProducto(false);
                        break;
                    case 3:
                        buscarProducto();
                        break;
                    case 4:
                        modificarProducto();
                        break;
                    case 5:
                        eliminarProducto();
                        break;
                    case 6:
                        agregarImagen();
                        break;
                    case 7:
                        System.out.println("\n--- Reporte de costos ---");
                        productos.reportarCostos();
                        break;
                    case 0:
                        System.out.println("Gracias por utilizar el sistema. Hasta luego.");
                        break;
                    default:
                        System.out.println("Opción inválida. Seleccione un número entre 0 y 7.");
                }
            } while (opcion != 0);
        } catch (NoSuchElementException e) {
            // Si se cierra la entrada, el programa termina sin guardar datos incompletos.
            System.out.println("\nFin de la entrada. El programa ha terminado.");
        }
    }

    private void mostrarMenu() {
        System.out.println("\n===== GESTIÓN DE INVENTARIOS =====");
        System.out.println("1. Agregar producto al inicio");
        System.out.println("2. Agregar producto al final");
        System.out.println("3. Buscar producto");
        System.out.println("4. Modificar producto");
        System.out.println("5. Eliminar producto");
        System.out.println("6. Agregar imagen a un producto");
        System.out.println("7. Generar reporte de costos");
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

    private void agregarProducto(boolean alInicio) {
        System.out.println("\n--- Agregar producto ---");
        String nombre = leerNombre(null);
        double precio = leerPrecio(null);
        String categoria = leerTexto("Categoría: ", null);
        LocalDate vencimiento = leerFecha(null, false);
        int cantidad = leerCantidad(null);
        if (!costoValido(precio, cantidad)) {
            return;
        }

        if (alInicio) {
            productos.insertarInicio(nombre, precio, categoria, vencimiento, cantidad);
        } else {
            productos.insertarFin(nombre, precio, categoria, vencimiento, cantidad);
        }
        System.out.println("Producto registrado correctamente.");
    }

    private void buscarProducto() {
        Producto producto = solicitarProducto();
        if (producto != null) {
            mostrarProducto(producto);
        }
    }

    private Producto solicitarProducto() {
        String nombre = leerTexto("Nombre del producto (tal como fue registrado): ", null);
        return productos.buscar(nombre);
    }

    private void mostrarProducto(Producto producto) {
        System.out.println(producto);
        if (!producto.getListaImagenes().isEmpty()) {
            System.out.println("Rutas de imágenes:");
            for (String ruta : producto.getListaImagenes()) {
                System.out.println("- " + ruta);
            }
        }
    }

    private void modificarProducto() {
        System.out.println("\n--- Modificar producto ---");
        Producto producto = solicitarProducto();
        if (producto == null) {
            return;
        }
        mostrarProducto(producto);
        System.out.println("Presione Enter para conservar el valor actual.");

        // Primero se leen todos los campos para evitar una modificación a medias.
        String nombreAnterior = producto.getNombre();
        String nuevoNombre = leerNombre(nombreAnterior);
        double precio = leerPrecio(producto.getPrecio());
        String categoria = leerTexto("Categoría [" + producto.getCategoria() + "]: ",
                producto.getCategoria());
        LocalDate vencimiento = leerFecha(producto.getFechaVencimiento(), true);
        int cantidad = leerCantidad(producto.getCantidad());
        if (!costoValido(precio, cantidad)) {
            return;
        }

        productos.modificar(nombreAnterior, precio, categoria, vencimiento, cantidad);
        producto.setNombre(nuevoNombre);
        System.out.println("Producto modificado correctamente.");
    }

    private void eliminarProducto() {
        System.out.println("\n--- Eliminar producto ---");
        Producto producto = solicitarProducto();
        if (producto == null) {
            return;
        }
        if (confirmar("¿Desea eliminar '" + producto.getNombre() + "'? (s/n): ")) {
            productos.eliminar(producto.getNombre());
            System.out.println("Producto eliminado correctamente.");
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    private void agregarImagen() {
        System.out.println("\n--- Agregar imagen ---");
        Producto producto = solicitarProducto();
        if (producto == null) {
            return;
        }
        String ruta = leerTexto("Ruta de la imagen (ej. imagenes/arroz.jpg): ", null);
        if (producto.getListaImagenes().contains(ruta)) {
            System.out.println("Esa ruta ya está registrada para este producto.");
            return;
        }
        productos.agregarImagenAProducto(producto.getNombre(), ruta);
        System.out.println("Ruta de imagen registrada correctamente.");
    }

    private String leerNombre(String actual) {
        while (true) {
            String mensaje = actual == null ? "Nombre: " : "Nombre [" + actual + "]: ";
            String nombre = leerTexto(mensaje, actual);
            if (nombre.equals(actual) || !productos.contiene(nombre)) {
                return nombre;
            }
            System.out.println("Ya existe un producto con ese nombre. Ingrese otro.");
        }
    }

    private String leerTexto(String mensaje, String actual) {
        while (true) {
            String texto = leerLinea(mensaje);
            if (!texto.isEmpty()) {
                return texto;
            }
            if (actual != null) {
                return actual;
            }
            System.out.println("Este campo no puede quedar vacío.");
        }
    }

    private double leerPrecio(Double actual) {
        while (true) {
            String mensaje = actual == null ? "Precio en colones: " : "Precio [" + actual + "]: ";
            String texto = leerLinea(mensaje);
            if (texto.isEmpty() && actual != null) {
                return actual;
            }
            // Se acepta punto o coma decimal, sin separadores de miles.
            if (texto.matches("[+]?(?:\\d+(?:[.,]\\d*)?|[.,]\\d+)")) {
                try {
                    double precio = Double.parseDouble(texto.replace(',', '.'));
                    if (Double.isFinite(precio) && precio >= 0) {
                        return precio;
                    }
                } catch (NumberFormatException e) {
                    // Se vuelve a solicitar el precio si el número no se puede convertir.
                }
            }
            System.out.println("Ingrese un precio válido mayor o igual a 0, sin separadores de miles.");
        }
    }

    private int leerCantidad(Integer actual) {
        while (true) {
            String mensaje = actual == null ? "Cantidad: " : "Cantidad [" + actual + "]: ";
            String texto = leerLinea(mensaje);
            if (texto.isEmpty() && actual != null) {
                return actual;
            }
            try {
                int cantidad = Integer.parseInt(texto);
                if (cantidad >= 0) {
                    return cantidad;
                }
            } catch (NumberFormatException e) {
                // La cantidad debe ser un entero dentro del rango de int.
            }
            System.out.println("Ingrese una cantidad entera mayor o igual a 0 (máximo 2147483647).");
        }
    }

    private LocalDate leerFecha(LocalDate actual, boolean modificando) {
        while (true) {
            String mensaje;
            if (modificando) {
                String valor = actual == null ? "No aplica" : actual.toString();
                mensaje = "Vencimiento [" + valor + "] (AAAA-MM-DD; '-' para quitar): ";
            } else {
                mensaje = "Vencimiento (AAAA-MM-DD; Enter si no vence): ";
            }
            String texto = leerLinea(mensaje);
            if (texto.isEmpty()) {
                return actual;
            }
            if (modificando && texto.equals("-")) {
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
            System.out.println("El costo supera el rango permitido. No se guardaron los cambios.");
            return false;
        }
        return true;
    }

    private String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }
}
