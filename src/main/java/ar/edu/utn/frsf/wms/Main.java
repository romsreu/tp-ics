package ar.edu.utn.frsf.wms;

public class Main {

    public static void main(String[] args) {
        Deposito deposito = new Deposito("Central Santa Fe");

        Producto auriculares = new Producto("SKU-001", "Auriculares Bluetooth");
        Producto mouse = new Producto("SKU-002", "Mouse inalambrico");

        deposito.recibir(auriculares, 500);
        deposito.recibir(mouse, 200);

        deposito.despachar("SKU-001", 30);

        deposito.imprimirInventario();
    }
}
