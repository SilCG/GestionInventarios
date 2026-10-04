import java.time.LocalDate;

public class ListaProductos {

    // Atributos (solo tiene primero)
    private NodoProducto primero;

    // Métodos
    // Constructor
    public ListaProductos(){
        primero = null; // vacío porque la lista apenas nace
    }

    // Getter
    private NodoProducto getPrimero (){
        return primero;
    }

    // Setter
    private void setPrimero (NodoProducto primero){
        this.primero = primero;
    }

    // Operaciones
    private boolean estaVacia(){
        return primero == null; // si primero = null = TRUE
    }

    // 1. Insertar inicio
    public void insertarInicio (String nombre, double precio, String categoria, LocalDate fechaVencimiento, int cantidad){
        Producto producto = new Producto(nombre, precio, categoria, fechaVencimiento, cantidad); // Construir producto a partir de esos datos
        NodoProducto nodo = new NodoProducto(producto); // Construir nodo
        nodo.setSiguiente (primero); // El nodo creado le pongo como siguiente el primero que estaba en la lista.
        setPrimero(nodo); // Nuevo nodo como primero
    }

    // Sobrecarga para productos que no vencen (la fecha queda en null)
    public void insertarInicio(String nombre, double precio, String categoria, int cantidad){
        insertarInicio(nombre, precio, categoria, null, cantidad);
    }

    // 2. Insertar fin
    public void insertarFin(String nombre, double precio, String categoria, LocalDate fechaVencimiento, int cantidad){
        Producto producto = new Producto(nombre, precio, categoria, fechaVencimiento, cantidad); // Construir producto a partir de esos datos
        NodoProducto nodo = new NodoProducto(producto); // Construir nodo
        if (estaVacia()){
            setPrimero(nodo); // Poner el nodo como primero
            return;
        }
        NodoProducto temp = primero; // Hago el temporal igual al primero
        while (temp.getSiguiente() != null) temp = temp.getSiguiente(); // Si no es el caso, le digo al Temp que ahora es el Siguiente del Temp.
        temp.setSiguiente(nodo); // Le digo al Temp que el siguiente es ese nodo nuevo que acabo de crear
    }

    // Sobrecarga para productos que no vencen (la fecha queda en null)
    public void insertarFin(String nombre, double precio, String categoria, int cantidad){
        insertarFin(nombre, precio, categoria, null, cantidad);
    }

    // 3. Busqueda
    public Producto buscar(String nombre){ // Buscar un Producto por nombre
        if (estaVacia()){
            System.out.println("\nLa lista está vacía\n");
            return null;
        }
        NodoProducto temp = primero; // Hago el temporal igual al primero
        while (temp != null){ // recorrer la lista
            if (nombre.equals(temp.getProducto().getNombre())) return temp.getProducto();
            temp = temp.getSiguiente(); // si no es, avanzo un paso
        }
        System.out.println("\nEl producto no se encuentra en la lista\n");
        return null;
    }

    // Consulta sin mensajes para validar nombres antes de registrar o modificar.
    public boolean contiene(String nombre){
        NodoProducto temp = primero;
        while (temp != null){
            if (temp.getProducto().getNombre().equals(nombre)) return true;
            temp = temp.getSiguiente();
        }
        return false;
    }

    // 4. Eliminar
    public Producto eliminar(String nombre){
        if (estaVacia()){
            System.out.println("\nLa lista está vacía\n");
            return null;
        }
        NodoProducto anterior = primero; // anterior = primero
        NodoProducto temp = anterior; // temp = anterior = primero
        while (temp != null){
            if (nombre.equals(temp.getProducto().getNombre())) break;
            anterior = temp; // Anterior se pone en línea y ya luego se queda atrás cuando temp avanza
            temp = temp.getSiguiente(); // ya está dando el paso
        }
        if (temp != null){
            if (temp == primero) setPrimero(temp.getSiguiente());
            else anterior.setSiguiente(temp.getSiguiente());
            return temp.getProducto();
        } else {
            System.out.println("\nEl producto no se encuentra en la lista.\n");
            return null;
        }
    }

    // 5. Modificar
    public void modificar(String nombre, double precio, String categoria, LocalDate fechaVencimiento, int cantidad){
        Producto producto = buscar(nombre); // reutilizo buscar para encontrar el producto
        if (producto == null) return; // si no existe, buscar ya imprimió el mensaje, solo salgo
        producto.setPrecio(precio); // le cambio cada dato con los setters de Producto
        producto.setCategoria(categoria);
        producto.setFechaVencimiento(fechaVencimiento);
        producto.setCantidad(cantidad);
    }

    // Sobrecarga para productos que no vencen (la fecha queda en null)
    public void modificar(String nombre, double precio, String categoria, int cantidad){
        modificar(nombre, precio, categoria, null, cantidad);
    }

    // 6. Agregar imagen a un producto que ya existe
    public void agregarImagenAProducto(String nombre, String ruta){
        Producto producto = buscar(nombre); // busco el producto por su nombre
        if (producto == null) return; // si no existe, salgo
        producto.agregarImagen(ruta); // le llamo el método agregarImagen de Producto
    }

    // 7. Reporte de costos
    public void reportarCostos (){ // imprime lo que se va a mostrar
        if (estaVacia()) {
            System.out.println("\nLa lista está vacía\n");
            return;
        }
        double totalAcumulado = 0; // se suma el costo de cada producto
        NodoProducto temp = primero;
        while (temp != null){ // recorrer la lista
            Producto producto = temp.getProducto(); // sacar el producto de la caja
            double costo = producto.calcularCostoTotal(); // precio por cantidad, usando el método de Producto
            System.out.println(producto.getNombre() + ": ₡" + costo);
            totalAcumulado += costo; // Suma combinada
            temp = temp.getSiguiente(); // avanzo un paso
        }
        System.out.println("\nCosto total de la lista: ₡" + totalAcumulado + "\n");
    }

    private class NodoProducto {
        // Atributos
        private Producto producto;
        private NodoProducto siguiente;

        // Métodos
        // Constructor
        public NodoProducto(Producto producto) {
            this.producto = producto;
            siguiente = null;
        }

        // Getters
        public Producto getProducto() {
            return producto;
        }

        public NodoProducto getSiguiente() {
            return siguiente;
        }

        // Setters
        public void setProducto(Producto producto) {
            this.producto = producto;
        }

        public void setSiguiente(NodoProducto siguiente) {
            this.siguiente = siguiente;
        }
    }

}
