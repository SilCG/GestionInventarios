import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebasInventario {

    private static int pruebas;

    public static void main(String[] args) throws Exception {
        probarLista();
        probarMenu();
        System.out.println("Pruebas completadas correctamente: " + pruebas);
    }

    private static void probarLista() throws Exception {
        ListaProductos lista = new ListaProductos();
        verificar(!lista.contiene("Arroz"), "Consulta en lista vacía");
        capturar(() -> {
            verificar(lista.buscar("Arroz") == null, "Búsqueda en lista vacía");
            verificar(lista.eliminar("Arroz") == null, "Eliminación en lista vacía");
        });
        verificar(capturar(lista::reportarCostos).contains("La lista está vacía"),
                "Reporte vacío");

        lista.insertarFin("Leche", 800, "Lácteos", LocalDate.of(2026, 12, 31), 3);
        lista.insertarInicio("Arroz", 1000, "Granos", 2);
        lista.insertarFin("Pan", 500, "Panadería", 4);
        String reporte = capturar(lista::reportarCostos);
        verificar(reporte.indexOf("Arroz:") < reporte.indexOf("Leche:")
                && reporte.indexOf("Leche:") < reporte.indexOf("Pan:"),
                "Orden de inserción al inicio y al final");
        verificar(reporte.contains("Costo total de la lista: ₡6400.0"),
                "Costo acumulado de varios productos");

        lista.agregarImagenAProducto("Leche", "imagenes/leche.jpg");
        lista.modificar("Leche", 900, "Bebidas", null, 5);
        Producto leche = lista.buscar("Leche");
        verificar(leche.getPrecio() == 900 && leche.getCantidad() == 5
                && leche.getCategoria().equals("Bebidas")
                && leche.getFechaVencimiento() == null
                && leche.getListaImagenes().contains("imagenes/leche.jpg"),
                "Modificación de campos conserva imágenes");

        verificar(lista.eliminar("Leche") == leche && lista.contiene("Pan")
                && lista.contiene("Arroz") && !lista.contiene("Leche"),
                "Eliminación del nodo intermedio");
        verificar(lista.eliminar("Arroz") != null && lista.contiene("Pan"),
                "Eliminación del primer nodo");
        lista.insertarFin("Sal", 200, "Condimentos", 1);
        verificar(lista.eliminar("Sal") != null && lista.contiene("Pan"),
                "Eliminación del último nodo");
        verificar(lista.eliminar("Pan") != null && !lista.contiene("Pan"),
                "Eliminación del único nodo");
        lista.insertarFin("Nuevo", 10, "Otros", 1);
        verificar(lista.contiene("Nuevo"), "Reinserción después de vaciar la lista");
        verificar(capturar(() -> {
            lista.modificar("Ausente", 1, "Otros", 1);
            lista.agregarImagenAProducto("Ausente", "imagenes/a.jpg");
            verificar(lista.eliminar("Ausente") == null, "Eliminar nombre inexistente");
        }).contains("no se encuentra"), "Operaciones sobre un producto inexistente");
    }

    private static void probarMenu() throws Exception {
        String salida = ejecutar("texto", "2147483648", "9", "-1", "7", "3", "Ausente",
                "4", "Ausente", "5", "Ausente", "6", "Ausente", "0");
        verificar(salida.contains("Ingrese un número entero")
                && salida.contains("Opción inválida")
                && salida.contains("La lista está vacía")
                && salida.contains("Hasta luego"), "Opciones inválidas y lista vacía");

        salida = ejecutar("1", "", "   ", "Arroz", "NaN", "Infinity", "-1", "1.000,50", "12,50",
                "", "Granos", "2026-02-30", "2026-2-01", "2026-02-28",
                "-1", "2.5", "2147483648", "2", "3", "Arroz", "7", "0");
        verificar(salida.contains("Este campo no puede quedar vacío")
                && salida.contains("Ingrese un precio válido")
                && salida.contains("Ingrese una fecha real")
                && salida.contains("Ingrese una cantidad entera")
                && salida.contains("Fecha de vencimiento: 2026-02-28")
                && salida.contains("Costo total de la lista: ₡25.0"),
                "Validación de textos, precios, fechas, cantidades y coma decimal");

        salida = ejecutar("2", "Leche", "800", "Lácteos", "", "3",
                "1", "Arroz", "1000", "Granos", "", "2",
                "2", "Pan", "500", "Panadería", "", "4", "7", "0");
        verificar(salida.indexOf("Arroz: ₡2000.0") < salida.indexOf("Leche: ₡2400.0")
                && salida.indexOf("Leche: ₡2400.0") < salida.indexOf("Pan: ₡2000.0")
                && salida.contains("Costo total de la lista: ₡6400.0"),
                "Menú conectado a inserción y reporte");

        salida = ejecutar("1", "Arroz", "10", "Granos", "", "2",
                "2", "Arroz", "Pan", "5", "Panadería", "", "1",
                "4", "Pan", "Arroz", "Pan nuevo", "", "", "", "",
                "3", "Pan nuevo", "7", "0");
        verificar(contar(salida, "Ya existe un producto con ese nombre") == 2
                && salida.contains("Nombre: Pan nuevo")
                && salida.contains("Costo total de la lista: ₡25.0"),
                "Nombres duplicados al crear y al renombrar");

        salida = ejecutar("1", "Leche", "10", "Lácteos", "2028-02-29", "2",
                "6", "Leche", "", "imagenes/leche.jpg",
                "6", "Leche", "imagenes/leche.jpg",
                "4", "Leche", "", "", "", "", "",
                "3", "Leche", "4", "Leche", "Bebida", "12.5", "Bebidas", "-", "3",
                "3", "Bebida", "3", "Leche", "7", "0");
        verificar(salida.contains("Esa ruta ya está registrada")
                && salida.contains("Fecha de vencimiento: 2028-02-29")
                && salida.contains("Nombre: Bebida\nPrecio: ₡12.5\nCategoría: Bebidas\nFecha de vencimiento: No aplica")
                && contar(salida, "Imágenes registradas: 1") >= 3
                && salida.contains("- imagenes/leche.jpg")
                && salida.contains("Costo total de la lista: ₡37.5")
                && salida.contains("El producto no se encuentra"),
                "Modificación conserva valores e imágenes, renombra y quita vencimiento");

        salida = ejecutar("1", "Arroz", "10", "Granos", "", "1",
                "5", "Arroz", "quizá", "n", "3", "Arroz", "5", "Arroz", "S", "7", "0");
        verificar(salida.contains("Responda s") && salida.contains("Eliminación cancelada")
                && salida.contains("Nombre: Arroz")
                && salida.contains("Producto eliminado correctamente")
                && salida.contains("La lista está vacía"), "Confirmación y cancelación de eliminación");

        salida = ejecutar("1", "Gratis", "0", "Otros", "", "0", "7", "0");
        verificar(salida.contains("Costo total de la lista: ₡0.0"), "Precio y cantidad cero");
        verificar(ejecutar().contains("Fin de la entrada"), "Cierre de entrada en el menú");
        salida = ejecutar("1", "Incompleto", "10");
        verificar(salida.contains("Fin de la entrada")
                && !salida.contains("Producto registrado correctamente"), "Alta interrumpida sin guardar");
        salida = ejecutar("1", "Arroz", "10", "Granos", "", "1", "4", "Arroz", "Nuevo", "20");
        verificar(salida.contains("Fin de la entrada")
                && !salida.contains("Producto modificado correctamente"), "Modificación interrumpida");
        salida = ejecutar("1", "Grande", "1" + repetir("0", 308), "Otros", "", "2", "7", "0");
        verificar(salida.contains("El costo supera el rango permitido")
                && salida.contains("La lista está vacía"), "Rechazo del desbordamiento del costo");
    }

    private static String ejecutar(String... lineas) throws Exception {
        String datos = lineas.length == 0 ? "" : String.join("\n", lineas) + "\n";
        try (Scanner entrada = new Scanner(datos)) {
            return capturar(() -> new Main(entrada).menu());
        }
    }

    private static String capturar(Runnable accion) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream salida = new PrintStream(buffer, true, "UTF-8")) {
            System.setOut(salida);
            accion.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString("UTF-8").replace("\r\n", "\n");
    }

    private static int contar(String texto, String fragmento) {
        int cantidad = 0;
        int posicion = 0;
        while ((posicion = texto.indexOf(fragmento, posicion)) != -1) {
            cantidad++;
            posicion += fragmento.length();
        }
        return cantidad;
    }

    private static String repetir(String texto, int veces) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            resultado.append(texto);
        }
        return resultado.toString();
    }

    private static void verificar(boolean condicion, String caso) {
        if (!condicion) {
            throw new AssertionError("Falló: " + caso);
        }
        pruebas++;
    }
}
