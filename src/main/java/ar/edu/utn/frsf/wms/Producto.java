package ar.edu.utn.frsf.wms;

public class Producto {

    private final String sku;
    private final String nombre;

    public Producto(String sku, String nombre) {
        this.sku = sku;
        this.nombre = nombre;
    }

    public String getSku() {
        return sku;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return sku + " - " + nombre;
    }
}
