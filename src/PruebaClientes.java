public class PruebaClientes {

    // Prueba de Cliente y ColaClientes, sin depender del menú.
    public static void main(String[] args) {
        ColaClientes cola = new ColaClientes();

        // Llegan en este orden (nombre, prioridad)
        cola.insertar(new Cliente("Raquel", 1));
        cola.insertar(new Cliente("Fernando", 3));
        cola.insertar(new Cliente("Enrique", 2));
        cola.insertar(new Cliente("Marcelo", 3));
        cola.insertar(new Cliente("Cindy", 1));
        cola.insertar(new Cliente("Alma", 2));

        System.out.println("Clientes en cola: " + cola.getTamanioActual()); // esperado: 6
        System.out.println("Sigue: " + cola.verFrente().getNombre());       // esperado: Fernando
        cola.mostrarCola();

        // Esperado: Beto, Dani, Carla, Fito, Ana, Eva
        System.out.println("Orden de atención:");
        while (!cola.estaVacia()) {
            Cliente atendido = cola.remover();
            System.out.println("- " + atendido.getNombre() + " (" + atendido.getTipo() + ")");
        }

        // Cola vacía: debe mostrar el mensaje y devolver null
        System.out.println("Remover en cola vacía: " + cola.remover());

        // Carrito: agregar el mismo producto dos veces debe sumar la cantidad
        Producto arroz = new Producto("Arroz", 1500, "Granos", 20);
        Cliente sofia = new Cliente("Sofía", 2);
        sofia.agregarAlCarrito(arroz, 2);
        sofia.agregarAlCarrito(arroz, 3);
        System.out.println(sofia); // esperado: 1 producto(s) en el carrito
        sofia.getCarrito().reportarCostos(); // esperado: Arroz: ₡7500.0

        // Prioridad inválida: debe lanzar error
        try {
            new Cliente("Error", 5);
            System.out.println("FALLÓ: se aceptó prioridad 5");
        } catch (IllegalArgumentException e) {
            System.out.println("Prioridad 5 rechazada: " + e.getMessage());
        }
    }
}
