public class ColaClientes {

    // Atributos
    private NodoCliente frente;
    private int tamanioActual;

    // Constructor
    public ColaClientes(){
        frente = null; // la cola tiene que nacer vacía
        tamanioActual = 0;
    }

    // Getters
    public int getTamanioActual() {
        return tamanioActual;
    }

    // Operaciones
    public boolean estaVacia(){
        return tamanioActual == 0;
    }

    // 1. Insertar un cliente en la posición que le corresponde según su prioridad
    public void insertar(Cliente cliente) {
        if (cliente == null) {
            System.out.println("\nNo se puede insertar un cliente vacío.\n");
            return;
        }
        NodoCliente nuevoNodo = new NodoCliente(cliente);

        // Caso 1: la cola está vacía o el nuevo tiene MÁS prioridad que el del frente
        if (estaVacia() || cliente.getPrioridad() > frente.getCliente().getPrioridad()) {
            nuevoNodo.setSiguiente(frente); // el nuevo apunta al que era el frente
            frente = nuevoNodo;             // y pasa a ser el nuevo frente
        } else {
            // Caso 2: avanzar mientras el siguiente tenga prioridad MAYOR O IGUAL.
            // El "o igual" hace que el nuevo quede detrás de los que llegaron antes con su misma prioridad.
            NodoCliente nodoTemp = frente;
            while (nodoTemp.getSiguiente() != null
                    && nodoTemp.getSiguiente().getCliente().getPrioridad() >= cliente.getPrioridad()) {
                nodoTemp = nodoTemp.getSiguiente();
            }
            // Solo dos cambios de referencias para insertar en medio (o al final)
            nuevoNodo.setSiguiente(nodoTemp.getSiguiente());
            nodoTemp.setSiguiente(nuevoNodo);
        }
        tamanioActual++;
    }

    // 2. Remover (atender) al cliente del frente, que siempre es el de mayor prioridad
    public Cliente remover() {
        if (estaVacia()) {
            System.out.println("\nNo hay clientes en espera.\n");
            return null; // valor convencional para indicar que la cola está vacía
        }
        Cliente clienteTemp = frente.getCliente();
        frente = frente.getSiguiente(); // el frente avanza al siguiente nodo
        tamanioActual--;
        return clienteTemp;
    }

    // 3. Ver quién sigue, sin sacarlo de la cola
    public Cliente verFrente() {
        if (estaVacia()) {
            System.out.println("\nNo hay clientes en espera.\n");
            return null;
        }
        return frente.getCliente();
    }

    // 4. Mostrar la cola en el orden en que se va a atender
    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("\nNo hay clientes en espera.\n");
            return;
        }
        System.out.println("\nClientes en espera (en orden de atención):");
        NodoCliente nodoTemp = frente;
        int posicion = 1;
        while (nodoTemp != null) {
            System.out.println(posicion + ". " + nodoTemp.getCliente());
            nodoTemp = nodoTemp.getSiguiente();
            posicion++;
        }
        System.out.println();
    }

    // Clase interna: cada nodo guarda un cliente y la referencia al siguiente nodo
    private class NodoCliente {
        // Atributos
        private Cliente cliente;
        private NodoCliente siguiente;

        // Constructor
        public NodoCliente(Cliente cliente) {
            this.cliente = cliente;
            siguiente = null;
        }

        // Getters
        public Cliente getCliente() {
            return cliente;
        }

        public NodoCliente getSiguiente() {
            return siguiente;
        }

        // Setters
        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        public void setSiguiente(NodoCliente siguiente) {
            this.siguiente = siguiente;
        }
    }
}

