# GestionInventarios-Grupo-4
Desarrollo de una aplicación para la gestión de un sistema de ventas de productos en línea (SOFT-10 Estructuras de Datos, segundo avance).

## Menú

| Opción | Operación |
| --- | --- |
| 1 | Agregar un producto al inventario de la tienda (si el nombre ya existe, permite sumar unidades al stock) |
| 2 | Ingresar un cliente a la cola: se piden su nombre y prioridad y se llena su carrito con productos del inventario |
| 3 | Atender al siguiente cliente: imprime la factura de su carrito con el total acumulado |
| 4 | Ver el inventario disponible, ordenado por nombre |
| 5 | Ver la cola de clientes en el orden de atención |
| 0 | Salir |

- Prioridad de los clientes: 1 = básico, 2 = afiliado, 3 = premium. Se atiende primero al de mayor prioridad; si hay empate, al que ingresó antes.
- Al agregar un producto al carrito se descuenta su cantidad del inventario. Se valida que el producto exista y que haya stock suficiente.
- Los nombres de los productos se buscan tal como fueron registrados, respetando mayúsculas y minúsculas.
- El precio admite punto o coma decimal, sin separadores de miles. Las fechas usan el formato `AAAA-MM-DD`; Enter indica que el producto no vence.
- Los datos permanecen en memoria durante la ejecución; al salir no se guardan en disco.

## Estructura

- `Producto`: datos del producto, imágenes y cálculo de su costo.
- `ListaProductos`: lista enlazada de productos; se usa como carrito de cada cliente.
- `ArbolProductos`: árbol binario de búsqueda con el nombre del producto como llave; es el inventario.
- `Cliente`: datos del cliente, su prioridad y su carrito.
- `ColaClientes`: cola de prioridad de clientes.
- `Tienda`: integra el inventario y la cola; gestiona el llenado del carrito y la factura.
- `Main`: `menu()`, lectura y validación de entradas, y la rutina `main()`.
- `PruebaClientes`: prueba independiente de `Cliente` y `ColaClientes`.
