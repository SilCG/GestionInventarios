import java.time.LocalDate;
import java.util.ArrayList;

public class Producto {

    // Atributos
    private String nombre;
    private double precio;
    private String categoria;
    private LocalDate fechaVencimiento; // puede quedar en null si el producto no vence
    private int cantidad;
    private ArrayList<String> listaImagenes;

    // Constructor para productos que SÍ tienen fecha de vencimiento
    public Producto(String nombre, double precio, String categoria, LocalDate fechaVencimiento, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidad = cantidad;
        this.listaImagenes = new ArrayList<>();
    }

    // Constructor para productos que NO vencen (ej. electrónicos, ropa)
    public Producto(String nombre, double precio, String categoria, int cantidad) {
        this(nombre, precio, categoria, null, cantidad);
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public ArrayList<String> getListaImagenes() {
        return listaImagenes;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Operaciones

    // Agrega una ruta de imagen a la lista de imágenes del producto
    public void agregarImagen(String rutaImagen) {
        listaImagenes.add(rutaImagen);
    }

    // Calcula el costo total de este producto según la cantidad (precio x cantidad)
    public double calcularCostoTotal() {
        return precio * cantidad;
    }

    // toString()
    @Override
    public String toString() {
        String vencimiento = (fechaVencimiento == null) ? "No aplica" : fechaVencimiento.toString();
        return "\nNombre: " + nombre
                + "\nPrecio: ₡" + precio
                + "\nCategoría: " + categoria
                + "\nFecha de vencimiento: " + vencimiento
                + "\nCantidad: " + cantidad
                + "\nImágenes registradas: " + listaImagenes.size()
                + "\nCosto total: ₡" + calcularCostoTotal() + "\n";
    }
}