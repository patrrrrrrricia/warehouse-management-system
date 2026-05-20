package model;

/**
 * BILL - record imutabil(fara setter) utilizat pentru jurnalizarea comenzilor efectuate
 *      - stocheaza detaliile unei facturi, inclusiv id ul comenzii, numele clientului,
 * numele produsului, cantitatea si suma totala
 */
public record Bill(int orderId, String clientName, String productName, int quantity, double totalAmount)
{

}