# GestionInventarios-Grupo-4
Desarrollo de una aplicación para la gestión de un sistema de ventas de productos en línea. 

## Ejecutar

Se necesita un JDK de Java 8 o superior. Desde la carpeta del proyecto:

```bash
javac -encoding UTF-8 -d out src/*.java
java -Dfile.encoding=UTF-8 -cp out Main
```

En un IDE, abra la carpeta del proyecto y ejecute `src/Main.java`.

## Menú

| Opción | Operación |
| --- | --- |
| 1 | Agregar producto al inicio de la lista |
| 2 | Agregar producto al final de la lista |
| 3 | Buscar un producto y consultar sus rutas de imágenes |
| 4 | Modificar nombre, precio, categoría, vencimiento y cantidad |
| 5 | Eliminar un producto, con confirmación |
| 6 | Agregar una ruta de imagen a un producto existente |
| 7 | Reportar precio × cantidad de cada producto y el costo total |
| 0 | Salir |

- Los nombres son únicos dentro del menú y se buscan tal como fueron registrados, respetando mayúsculas y minúsculas.
- El precio admite punto o coma decimal, sin separadores de miles. Precio y cantidad deben ser mayores o iguales a cero.
- Las fechas usan el formato `AAAA-MM-DD`. Al crear un producto, Enter indica que no vence.
- Al modificar, Enter conserva cada valor actual; `-` en vencimiento elimina la fecha. Las imágenes se conservan.
- Las imágenes se registran como rutas de texto, por ejemplo `imagenes/arroz.jpg`. No se cargan ni se comprueba que el archivo exista. El menú evita repetir la misma ruta en un producto.
- Los datos permanecen en memoria durante la ejecución; al salir no se guardan en disco.

## Estructura

- `Producto`: datos del producto, imágenes y cálculo de su costo.
- `ListaProductos`: lista enlazada y operaciones. `NodoProducto` es una clase interna privada; no necesita un archivo separado.
- `Main`: menú, lectura y validación de entradas.
- `tests/PruebasInventario.java`: pruebas de la lista y de los flujos del menú, sin dependencias externas.

## Pruebas

```bash
javac -encoding UTF-8 -d out src/*.java tests/PruebasInventario.java
java -Dfile.encoding=UTF-8 -cp out PruebasInventario
```
