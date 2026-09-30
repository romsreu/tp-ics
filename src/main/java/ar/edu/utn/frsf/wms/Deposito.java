package ar.edu.utn.frsf.wms;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Deposito simple: permite recibir mercaderia de proveedores,
 * despachar pedidos, registrar devoluciones y consultar el stock de cada producto.
 */
public class Deposito {

    private final String nombre;
    private final Map<String, Integer> stock = new HashMap<>();
    private final Map<String, Producto> productos = new TreeMap<>();

    public Deposito(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void recibir(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        productos.put(producto.getSku(), producto);
        stock.merge(producto.getSku(), cantidad, Integer::sum);
    }

    public void despachar(String sku, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (!productos.containsKey(sku)) {
            throw new IllegalArgumentException("Producto inexistente: " + sku);
        }
        int disponible = getStock(sku);
        if (disponible < cantidad) {
            throw new IllegalStateException(
                    "Stock insuficiente para " + sku + ": disponible " + disponible + ", pedido " + cantidad);
        }
        stock.put(sku, disponible - cantidad);
    }

    public void registrarDevolucion(String sku, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (!productos.containsKey(sku)) {
            throw new IllegalArgumentException("Producto inexistente: " + sku);
        }
        stock.merge(sku, cantidad, Integer::sum);
    }

    public int getStock(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public void imprimirInventario() {
        System.out.println("Inventario del deposito " + nombre + ":");
        productos.values().forEach(p ->
                System.out.println("  " + p + " -> " + getStock(p.getSku()) + " unidades"));
    }
}
