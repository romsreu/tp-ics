package ar.edu.utn.frsf.wms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepositoTest {

    private Deposito deposito;
    private Producto producto;

    @BeforeEach
    void setUp() {
        deposito = new Deposito("Test");
        producto = new Producto("SKU-001", "Auriculares");
    }

    @Test
    void recibirAumentaElStock() {
        deposito.recibir(producto, 100);
        deposito.recibir(producto, 50);
        assertEquals(150, deposito.getStock("SKU-001"));
    }

    @Test
    void despacharDescuentaElStock() {
        deposito.recibir(producto, 100);
        deposito.despachar("SKU-001", 40);
        assertEquals(60, deposito.getStock("SKU-001"));
    }

    @Test
    void despacharSinStockSuficienteFalla() {
        deposito.recibir(producto, 10);
        assertThrows(IllegalStateException.class, () -> deposito.despachar("SKU-001", 11));
    }

    @Test
    void despacharProductoInexistenteFalla() {
        assertThrows(IllegalArgumentException.class, () -> deposito.despachar("SKU-999", 1));
    }

    @Test
    void recibirCantidadInvalidaFalla() {
        assertThrows(IllegalArgumentException.class, () -> deposito.recibir(producto, 0));
    }
}
